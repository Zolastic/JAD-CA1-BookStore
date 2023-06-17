package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Author;

public class VerifyUserDAO {
	// Function to validate user id
	public String validateUserID(Connection connection, String userID) {
		if (userID != null) {
			String sqlStr = "SELECT COUNT(*) FROM users WHERE users.userID=?";
			try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
				ps.setString(1, userID);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						int rowCount = rs.getInt(1);
						if (rowCount < 1) {
							userID = null;
						}
					}
				}
			} catch (SQLException e) {
				userID = null;
				System.err.println("Error: " + e.getMessage());
			}
		}
		return userID;
	}
}
