package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/jad_ca1?user=root&password=root&serverTimezone=UTC";
    
    static {
    	try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
    }

    public static Connection getConnection() throws SQLException {
    	return DriverManager.getConnection(DB_URL);
    }
}
