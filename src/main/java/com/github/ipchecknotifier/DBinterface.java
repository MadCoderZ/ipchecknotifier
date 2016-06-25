package com.github.ipchecknotifier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBinterface {
    public static final String DBPATH = "jdbc:sqlite:ipchecker.sqlite.db";

    DBinterface() throws SQLException, ClassNotFoundException
    {
        createTableIfNotExists();
    }

    /**
     * connect() to establish a DB connection (sqlite)
     */
    private Connection connect() throws SQLException, ClassNotFoundException {
        Connection conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DBPATH);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Fetch everything from the database and output.
     * @throws ClassNotFoundException
     */
    public void fetchAll() throws ClassNotFoundException{
        final String sql = "SELECT ID, IP, DATE, COMMENTS FROM info";

        try (Connection connection = this.connect();
             Statement stmt  = connection.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                System.out.println(rs.getInt("ID") +  "\t\t" +
                                   rs.getString("IP") + "\t\t" +
                                   rs.getString("DATE") + "\t\t" +
                                   rs.getString("COMMENTS"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Fetch the IP only from the last DB record.
     * @return
     * @throws ClassNotFoundException
     */
    public String fetchIP() throws ClassNotFoundException {
        final String sql = "SELECT IP FROM info ORDER BY DATE DESC LIMIT 1";
        String ip = null;

        try (Connection connection = this.connect();
             Statement stmt  = connection.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                ip = rs.getString("IP");
                //System.out.println("Getting the IP from the last record on DB: " + rs.getString("IP"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ip;
    }

    /**
     * Insert a full record into the DB.
     * @param IP
     * @param date
     * @param comments
     * @throws ClassNotFoundException
     */
    public void insertDB(String IP, String date, String comments) throws ClassNotFoundException {
        final String sql = "INSERT INTO info(IP, DATE, COMMENTS) VALUES(?,?,?)";

        try (Connection conn = this.connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, IP);
            pstmt.setString(2, date);
            pstmt.setString(3, comments);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Returns true if the IP is different than the last one recorded into the DB.
     * @param publicIP
     * @return
     * @throws ClassNotFoundException
     */
    public boolean istheIPnew(String publicIP) throws ClassNotFoundException {
        final String sql = "SELECT IP FROM info ORDER BY DATE DESC LIMIT 1";
        String lastip = null;

        try (Connection connection = this.connect();
             Statement stmt  = connection.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                lastip = rs.getString("IP");
                //System.out.println("Getting the IP from the last record on DB: " + rs.getString("IP"));
            }

            if (lastip == null)
                return true;

            if (!lastip.equals(publicIP))
                return true;

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
        return false;
    }

    private void createTableIfNotExists() throws SQLException, ClassNotFoundException
    {
        final String sql =  "CREATE TABLE IF NOT EXISTS `info` (\n" +
                            "	`ID`	INTEGER NOT NULL,\n" +
                            "	`IP`	TEXT NOT NULL,\n" +
                            "	`DATE`	TEXT NOT NULL,\n" +
                            "	`COMMENTS`	TEXT,\n" +
                            "	PRIMARY KEY(ID)\n" +
                            ");";

            Connection connection = this.connect();
             Statement stmt  = connection.createStatement();
             stmt.execute(sql);
    }
}