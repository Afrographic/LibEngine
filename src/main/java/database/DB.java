/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import java.sql.*;

/**
 *
 * @author X M G
 */
public class DB {

    String urldriver = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/libengine";
    Connection con;

    public DB() {
        try {
            con = DriverManager.getConnection(url, "root", "");
        } catch (SQLException ex) {
            System.out.println("Unable to connect");
        }

    }

    public Connection getConnection() {

        return con;
    }

}
