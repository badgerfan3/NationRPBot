package Sql;

import java.sql.*;

public class SQLFunctions {
    Connection conn = null;
    public void getConnection(){
        PreparedStatement statement;

        try {
            conn = this.connect();

            DatabaseMetaData dbm = conn.getMetaData();
            ResultSet tables = dbm.getTables(null, null, "GAME", null);
            if (!tables.next()) {
                System.out.println("Should be working");
                String tableSQL = "CREATE TABLE GAME " +
                        "(SERVER_ID INT  KEY    NOT NULL," +
                        " ID             INT    NOT NULL, " +
                        " NATION_NAME    TEXT   NOT NULL, " +
                        " FLAG           TEXT   NOT NULL, " +
                        " POP_SCORE      FLOAT  NOT NULL, " +
                        " POP            INT    NOT NULL, " +
                        " ECONOMY_SCORE  INT    NOT NULL, " +
                        " STABILITY      FLOAT  NOT NULL)";
                statement = conn.prepareStatement(tableSQL);
                statement.executeUpdate();

                tableSQL = "CREATE TABLE SERVERS " +
                        "(ID INT PRIMARY KEY    NOT NULL," +
                        " SERVER_NAME   TEXT    NOT NULL)";
                statement = conn.prepareStatement(tableSQL);
                statement.executeUpdate();

                conn.commit();
            }
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Opened database successfully");
        System.out.println("Table Created Successfully");
    }

    public void createNation(long ID, String Name, String Flag_URL, long Server_ID) throws SQLException {

        try {
            System.out.println("Opened Database For Insertion Correctly (CREATENATION)");

            String sql = "INSERT INTO GAME (SERVER_ID, ID, NATION_NAME, FLAG, POP_SCORE, POP, ECONOMY_SCORE, STABILITY) VALUES (?,?,?,?,0.0,15000,0,0.0);";


            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setLong(1, Server_ID);
            statement.setLong(2, ID);
            statement.setString(3, Name);
            statement.setString(4, Flag_URL);

            statement.executeUpdate();
            conn.commit();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            System.exit(0);
        }
        System.out.println("Created Nation Successfully!");
    }

    private Connection connect() {

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:./misc/server.db");
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void deleteNation(Number ID, Number Server_ID) {
        PreparedStatement ps = null;

        try {
            String sql;
            if(ID != null) {
                if(returnNation(ID.intValue(),Server_ID) != null) {
                    sql = "DELETE FROM GAME WHERE ID = ? AND SERVER_ID = ?";
                    ps = conn.prepareStatement(sql);
                    ps.setLong(1, ID.longValue());
                    ps.setLong(2, Server_ID.longValue());
                    System.out.println("Deleted ID: " + ID + " from the database sucessfully!");
                }
            } else {
                sql = "DELETE FROM GAME WHERE SERVER_ID = ?";
                ps = conn.prepareStatement(sql);
                ps.setLong(1, Server_ID.longValue());
                System.out.println("Deleted All Values from the Database, in server " + Server_ID + "!!");
            }
            assert ps != null;
            ps.executeUpdate();
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet returnNation(Number ID, Number Server_ID) {
        PreparedStatement ps;
        ResultSet rs = null;
        if(ID != null) {
            try {
                String sql = "SELECT * FROM GAME WHERE ID = ? AND SERVER_ID = ?";
                ps = conn.prepareStatement(sql);

                ps.setLong(1, ID.longValue());
                ps.setLong(2, Server_ID.longValue());
                rs = ps.executeQuery();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try {
                String sql = "SELECT * FROM GAME WHERE SERVER_ID = ?";
                ps = conn.prepareStatement(sql);
                ps.setLong(1, Server_ID.longValue());

                rs = ps.executeQuery();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return rs;
    }

    public void changeStat(String key, Number ID, String valueIn, Number Server_ID){
        PreparedStatement ps;
        Number value;
        String sql;
        try {
            if (ID != null) {
                System.out.println("Updating the " + key + " where ID = " + ID);
                if (key.equalsIgnoreCase("POP") || key.equalsIgnoreCase("ECONOMY_SCORE")) {
                    sql = "UPDATE GAME SET " + key.toUpperCase() + " = ? WHERE ID = ? AND SERVER_ID = ?";
                    ps = conn.prepareStatement(sql);
                    value = Integer.parseInt(valueIn);

                    ps.setInt(1, value.intValue());
                    ps.setLong(2, ID.longValue());
                    ps.setLong(3, Server_ID.longValue());

                } else if(key.equalsIgnoreCase("POP_SCORE") || key.equalsIgnoreCase("STABILITY")) {
                    sql = "UPDATE GAME SET " + key.toUpperCase() + " = ? WHERE ID = ? AND SERVER_ID = ?";
                    ps = conn.prepareStatement(sql);
                    value = Float.parseFloat(valueIn);

                    ps.setFloat(1,value.floatValue());
                    ps.setLong(2,ID.longValue());
                    ps.setLong(3, Server_ID.longValue());
                } else if(key.equalsIgnoreCase("NATION_NAME") || key.equalsIgnoreCase("FLAG")){
                    sql = "UPDATE GAME SET " + key.toUpperCase() + " = ? WHERE ID = ? AND SERVER_ID = ?";
                    ps = conn.prepareStatement(sql);

                    ps.setString(1,valueIn);
                    ps.setLong(2,ID.longValue());
                    ps.setLong(3, Server_ID.longValue());
                } else if (key.equalsIgnoreCase("ID")) {
                    sql = "UPDATE GAME SET " + key.toUpperCase() + " = ? WHERE ID = ? AND SERVER_ID = ?";
                    ps = conn.prepareStatement(sql);
                    value = Long.parseLong(valueIn);

                    ps.setLong(1, value.longValue());
                    ps.setLong(2,ID.longValue());
                    ps.setLong(3, Server_ID.longValue());
                } else {
                    throw new SQLException("Key not valid");
                }
                ps.executeUpdate();
                conn.commit();
            }
        } catch (NumberFormatException | SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean setupServer(Number ID, String name) {
        PreparedStatement ps;
        String sql;
        ResultSet rs;
        boolean flag = false;
        try{
            if(ID != null) {
                System.out.println("Adding server to database");

                sql = "SELECT * FROM SERVERS WHERE ID = ?";

                ps = conn.prepareStatement(sql);

                ps.setLong(1,ID.longValue());
                rs = ps.executeQuery();

                if(!rs.next()){
                    flag = true;

                    sql = "INSERT INTO SERVERS (ID,SERVER_NAME) VALUES (?,?)";

                    ps = conn.prepareStatement(sql);

                    ps.setLong(1,ID.longValue());
                    ps.setString(2,name);

                    ps.executeUpdate();
                    conn.commit();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    public boolean deleteServer(Number ID) {
        PreparedStatement ps;
        String sql;
        ResultSet rs;
        boolean flag = false;
        try {
            if(ID != null) {
                System.out.println("Deleting server from database");

                sql = "SELECT * FROM SERVERS WHERE ID = ?";

                ps = conn.prepareStatement(sql);

                ps.setLong(1,ID.longValue());
                rs = ps.executeQuery();

                if(rs.next()) {
                    flag = true;

                    sql = "DELETE FROM SERVERS WHERE ID = ?";

                    ps = conn.prepareStatement(sql);

                    ps.setLong(1,ID.longValue());

                    ps.executeUpdate();

                    sql = "DELETE FROM GAME WHERE SERVER_ID = ?";

                    ps = conn.prepareStatement(sql);

                    ps.setLong(1, ID.longValue());

                    ps.executeUpdate();

                    conn.commit();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
}
