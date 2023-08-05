package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exception.DAOException;

public class DBConnection {
    private static final String DB_URL;
    static {
    	String profile = System.getenv("APP_PROFILE");
    	
    	if ("cloud".equals(profile)) {
    		DB_URL = "jdbc:mysql://inkwell.mysql.database.azure.com:3306/jad_ca2?user=jad&password=ca2123!?&serverTimezone=UTC";
    	} else {
    		DB_URL = "jdbc:mysql://localhost:3306/jad_ca1?user=root&password=root&serverTimezone=UTC";
    	}
    	
    }
    
    
    static {
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
    }

    public static Connection getConnection() {
    	try {
			return DriverManager.getConnection(DB_URL);
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new DAOException("Cannot get the connection", ex);
		}
    }
    
    public static void rollback(Connection connection) {
		try {
			connection.rollback();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new DAOException("Cannot rollback", ex);
		}
    }
    
    public static void setAutoCommit(Connection connection, boolean autoCommit) {
		try {
			connection.setAutoCommit(autoCommit);
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new DAOException("Cannot setAutoCommit", ex);
		}
    }
    
    public static void close(Connection connection) {
		try {
			connection.close();
		} catch (SQLException ex) {
			ex.printStackTrace();
			throw new DAOException("Cannot close connection", ex);
		}
    }
}
