/**
 * This is a simple abstraction for creating a connection with a MySQL DB-based
 *
 * It also provides methods for querying
 *
 * @author Gabriele-P03
 */

package com.greenhouse.cloud;

import java.sql.*;

public class MySQLConnection {

    private String USER;
    private String PASSWORD;
    private String URL;
    private boolean isConnected = false;

    Connection connection;
    Statement stmt;
    PreparedStatement pstmt;

    public MySQLConnection(String ip, String port, String dbName) {
        this(ip, port, dbName, "root@localhost", "root");
    }

    public MySQLConnection(String ip, String port, String dbName, String USER, String PASSWORD) {
        this.URL = "jbdc:mysql://" + ip + ":" + port + "/" + dbName;
        this.connect();
    }

    private void connect(){
        try{
            Class.forName("com.mysql.jbdc.Driver");
            this.connection = DriverManager.getConnection(this.URL, this.USER, this.PASSWORD);
            this.stmt = this.connection.createStatement();
            ResultSet rs = this.stmt.executeQuery("show databases;");

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return this.isConnected;
    }
}
