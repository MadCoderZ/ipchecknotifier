package com.github.ipchecknotifier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBInterface {
    private final String DBPATH;

    DBInterface(String path) throws SQLException, ClassNotFoundException
    {
        this.DBPATH = "jdbc:sqlite:" + path;
        createTableIfNotExists();
    }

    /**
     * connect() to establish a DB connection (sqlite)
     */
    private Connection getConnection() throws SQLException, ClassNotFoundException
    {
        Connection conn = null;

        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(this.DBPATH);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    /**
     * Fetch everything from the database and output.
     * @return List list of IP records
     * @throws ClassNotFoundException
     */
    public List<IPRecord> fetchAll() throws ClassNotFoundException
    {
        final String sql =
                "SELECT ID, IP, DATE, COMMENTS FROM info ORDER BY DATE DESC";

        List<IPRecord> records = new ArrayList<>();
        try (Connection connection = this.getConnection();
             Statement stmt  = connection.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            while (rs.next()) {
                IPRecord myIP = new IPRecord();

                myIP.setIP(rs.getString("IP"));
                myIP.setDate(rs.getString("DATE"));
                myIP.setComments(rs.getString("COMMENTS"));

                records.add(myIP);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return records;
    }

    /**
     * Fetch the IP only from the last DB record.
     * @return
     * @throws ClassNotFoundException
     */
    public String fetchIP()
    {
        final String sql = "SELECT IP FROM info ORDER BY DATE DESC LIMIT 1";
        String ip = null;

        try (Connection connection = this.getConnection();
             Statement stmt  = connection.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){

            // loop through the result set
            while (rs.next()) {
                ip = rs.getString("IP");
                //System.out.println("Getting the IP from the last record on DB: " + rs.getString("IP"));
            }
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBInterface.class.getName()).log(Level.SEVERE, null, ex);
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
    public void insertDB(String IP, String date, String comments)
    {
        final String sql = "INSERT INTO info(IP, DATE, COMMENTS) VALUES(?,?,?)";

        try (Connection conn = this.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, IP);
            pstmt.setString(2, date);
            pstmt.setString(3, comments);
            pstmt.executeUpdate();
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Returns true if the IP is different than the last one recorded into the DB.
     * @param publicIP
     * @return
     * @throws ClassNotFoundException
     */
    public boolean istheIPnew(String publicIP)
    {
        final String sql = "SELECT IP FROM info ORDER BY DATE DESC LIMIT 1";
        String lastip = null;

        try (Connection connection = this.getConnection();
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
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBInterface.class.getName()).log(Level.SEVERE, null, ex);
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

            Connection connection = this.getConnection();
             Statement stmt  = connection.createStatement();
             stmt.execute(sql);
    }

    public void cleanDB()
    {
        final String sql = "DELETE FROM info";

        try {
            Statement stmt = this.getConnection().createStatement();
            stmt.execute(sql);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(DBInterface.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}