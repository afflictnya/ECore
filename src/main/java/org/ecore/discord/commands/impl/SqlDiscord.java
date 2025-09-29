package org.ecore.discord.commands.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.ecore.PVars;
import org.ecore.commands.CommandException;
import org.ecore.database.Cache;
import org.ecore.discord.commands.DiscordCommand;
import org.ecore.discord.commands.DiscordCommandEx;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import static net.dv8tion.jda.api.interactions.commands.OptionType.BOOLEAN;
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

        options.add(new OptionData(BOOLEAN, "clearcache", "clear cache or not.", false));

        allowedRoles.add(1144575307609804940L);
    }

    @Override
    public void run(SlashCommandInteractionEvent event) throws DiscordCommandEx {
        if (Optional.ofNullable(event.getOption("clearcache")).map(OptionMapping::getAsBoolean).orElse(false)) Cache.clear();
        try(Statement stmt = PVars.database.getCon().createStatement()) {
            if (stmt.execute(event.getOption("query").getAsString())) {
                event.reply(rsToJson(stmt.getResultSet()))
                     .setEphemeral(true)
                     .queue();
            } else  {
                throw new DiscordCommandEx("Query does not return results");
            }
        } catch (Exception e) {
            throw new DiscordCommandEx(e.getMessage());
        }
    }
}
