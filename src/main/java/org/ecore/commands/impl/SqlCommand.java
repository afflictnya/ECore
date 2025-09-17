package org.ecore.commands.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import mindustry.gen.Player;
import org.ecore.PVars;
import org.ecore.commands.AbstractCommand;
import org.ecore.commands.CommandException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SqlCommand extends AbstractCommand {

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


    public SqlCommand(){
        super("sql", "exec sql code", "<sql...>");
        adminOnly = true;
        requiredRank = "ultra";
    }

    @Override
    public void run(Player player, String[] args) throws CommandException {
        try(PreparedStatement stmt = PVars.database.getCon().prepareStatement(args[0])) {
            player.sendMessage(rsToJson(stmt.executeQuery()));
        } catch (Exception e) {
            throw new CommandException(e.getMessage());
        }
    }
}
