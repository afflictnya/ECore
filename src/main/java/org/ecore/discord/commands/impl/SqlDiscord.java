package org.ecore.discord.commands.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.ecore.PVars;
import org.ecore.commands.CommandException;
import org.ecore.discord.commands.DiscordCommand;
import org.ecore.discord.commands.DiscordCommandEx;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

public class SqlDiscord extends DiscordCommand {
    @SneakyThrows(SQLException.class)
    public static String rsToJson(ResultSet rs) {
        var mapper = new ObjectMapper();
        List<Map<String, Object>> list = new ArrayList<>();
        var metadata = rs.getMetaData();
        int count = metadata.getColumnCount();
        while (rs.next()) {
            Map<String, Object> row = new LinkedHashMap<>();
            for (int i = 1; i <= count; i++) {
                String columnName = metadata.getColumnLabel(i);
                Object value = rs.getObject(i);
                row.put(columnName, value);
            }
            list.add(row);
        }

        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(list);
        } catch (Exception ignored) {
            return "";
        }
    }
    public SqlDiscord() {
        super("sql", "Execute SQL query");
        options.add(new OptionData(STRING, "query", "sql query")
                .setRequired(true));
        options.add(new OptionData(STRING, "server", "name of server")
                .setRequired(true));
        allowedRoles.add(1390448182420439160L);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws DiscordCommandEx {
        try(PreparedStatement stmt = PVars.database.getCon().prepareStatement(event.getOption("query").getAsString())) {
            event.reply(rsToJson(stmt.executeQuery())).setEphemeral(true).queue();
        } catch (Exception e) {
            throw new DiscordCommandEx(e.getMessage());
        }
    }
}
