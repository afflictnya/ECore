package org.ecore.database;

import arc.util.Log;
import lombok.Getter;
import lombok.SneakyThrows;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;

public class Database {

    @Getter
    private final Connection con;
    public Database(String path) throws SQLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class.forName("org.sqlite.JDBC");
        con = DriverManager.getConnection("jdbc:sqlite:" + path);
        Log.info("Connected to database.");
    }

    @SneakyThrows(SQLException.class)
    public void create()  {
        try(Statement statement = con.createStatement()) {
            statement.execute("""
                    CREATE TABLE IF NOT EXISTS players(
                        'id' INTEGER PRIMARY KEY AUTOINCREMENT,
                        'uuid' VARCHAR(24),
                        'blockplaced' INTEGER,
                        'blockbroken' INTEGER,
                        'timeplayed' INTEGER,
                        'messagesent' INTEGER,
                        'banneduntil' INTEGER,
                        'banned' BOOLEAN,
                        'rank' VARCHAR(10)
                    )""");
        }
    }
    @SneakyThrows(SQLException.class)
    public void add(String uuid) {
        try(PreparedStatement st = con.prepareStatement("""
                INSERT INTO players(uuid,blockplaced,blockbroken,timeplayed,messagesent,banneduntil,banned,rank)
                VALUES(?,0,0,0,0,0,false,"def")
                """)) {
            st.setString(1, uuid);
            st.execute();
        }
    }
    @SneakyThrows(SQLException.class)
    public void add(String uuid, boolean banned, String rank, long blockplaced,long blockbroken,long timeplayed,long messagesent,long banneduntil)  {
        try(PreparedStatement st = con.prepareStatement("""
                INSERT INTO players(uuid,blockplaced,blockbroken,timeplayed,messagesent,banneduntil,banned,rank)
                VALUES(?,?,?,?,?,?,?)
                """)) {
                        st.setString(1, uuid);
            st.setLong(2, blockplaced);
            st.setLong(3, blockbroken);
            st.setLong(4, timeplayed);
            st.setLong(5, messagesent);
            st.setLong(6, banneduntil);
            st.setBoolean(7, banned);
            st.setString(8, rank);
            st.execute();
        }
    }
    @SneakyThrows(SQLException.class)
    public void update(int id, String uuid, boolean banned, String rank, long blockplaced,long blockbroken,long timeplayed,long messagesent,long banneduntil) {
        try(PreparedStatement st = con.prepareStatement("""
                UPDATE players SET uuid=?,blockplaced=?,blockbroken=?,timeplayed=?,messagesent=?,banneduntil=?,banned=?,rank=? WHERE id=?
                """)) {
            st.setString(1, uuid);
            st.setLong(2, blockplaced);
            st.setLong(3, blockbroken);
            st.setLong(4, timeplayed);
            st.setLong(5, messagesent);
            st.setLong(6, banneduntil);
            st.setBoolean(7, banned);
            st.setString(8, rank);
            st.setInt(9, id);
            st.execute();
        }
    }
    @SneakyThrows(SQLException.class)
    public void setParam(int id, Object data, Param param) {
        try(PreparedStatement statement = con.prepareStatement("UPDATE players SET " + param.name() + "=? WHERE id=?")) {
            statement.setInt(2, id);
            if (param == Param.uuid) statement.setString(1, (String) data);
            else if (param == Param.banned) statement.setBoolean(1, (boolean) data);
            else statement.setInt(2, (int) data);
            statement.execute();
        }
    }
    @SneakyThrows(Exception.class)
    private PlayerData get(Param pname, Object val)  {
       try(PreparedStatement statement = con.prepareStatement("SELECT * FROM players WHERE " + pname.name() + "=?", ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY,ResultSet.CLOSE_CURSORS_AT_COMMIT)) {
           if (val instanceof String s) statement.setString(1, s);
           else statement.setLong(1, (long) val);
           var resp = statement.executeQuery();
           if (!resp.next()) return null;
           return new PlayerData(resp.getInt("id"), resp.getString("uuid"), resp.getInt("blockplaced"),
                   resp.getInt("blockbroken"), resp.getInt("timeplayed"), resp.getInt("messagesent"),
                   resp.getInt("banneduntil"), resp.getBoolean("banned"), resp.getString("rank"));
       }
    }

    public PlayerData getByUUID(String uuid) {
       return get(Param.uuid, uuid);
    }
    public PlayerData getByID(int id)  {
        return get(Param.id, id);
    }

    public enum Param{
        uuid,blockplaced,blockbroken,timeplayed,messagesent,banneduntil,banned,id,rank
    }
}
