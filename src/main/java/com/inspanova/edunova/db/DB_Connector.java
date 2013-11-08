/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.inspanova.edunova.db;
import java.sql.Connection;
import java.sql.DriverManager;
import com.inspanova.edunova.settings.Constants;

/**
 *
 * @author krishna
 */
public class DB_Connector {
    public static Connection getMysqlConnection() throws Exception {
		Connection conn = null;
		String dburl = "jdbc:mysql://" + Constants.DB_HOST + ":"
				+ Constants.DB_PORT + "/";
		String dbName = Constants.DB_NAME;
		String dbuser = Constants.DB_USER;
		String dbpass = Constants.DB_PASSWORD;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(dburl + dbName, dbuser, dbpass);
			return conn;
		} catch (Exception e) {
                    
			throw new Exception(e.getMessage());
		}

	}
    
}
