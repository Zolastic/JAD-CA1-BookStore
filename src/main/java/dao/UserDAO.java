package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import model.User;

public class UserDAO {

	public User getUserInfo(Connection connection, String userID) throws SQLException {
		String sqlStr = "SELECT * FROM users WHERE userID = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, userID);

			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String password = resultSet.getString("password");
				String role = resultSet.getString("role");
				String img = resultSet.getString("img");
				String secret = resultSet.getString("secret");
				resultSet.close();
				User user = new User(userID, name, email, password, role, img, secret);
				return user;
			}
			return null;
		}
	}

	public int updateUser(Connection connection, String name, String email, String image, String userID) throws SQLException {
		String sql = "UPDATE users SET name = ?, email = ?, img = ? WHERE userID = ?;";
		String sqlWithoutImage = "UPDATE users SET name = ?, email = ? WHERE userID = ?;";

		boolean noImage = image == null;

		String sqlUpdate = noImage ? sqlWithoutImage : sql;

		PreparedStatement ps = connection.prepareStatement(sqlUpdate);

		ps.setString(1, name);
		ps.setString(2, email);
		if (noImage) {
			ps.setString(3, userID);
		} else {
			ps.setString(3, image);
			ps.setString(4, userID);
		}

		int affectedRows = ps.executeUpdate();

		return affectedRows > 0 ? 200 : 500;
	}
	
	public int updateUserPassword(Connection connection, String userID, String currentPassword, String newPassword, String confirmNewPassword) throws SQLException {
		String currentPasswordValidationSqlStr = "SELECT password FROM users WHERE userID = ? AND password = ?;";
		String updatePasswordSqlStr = "UPDATE users SET password = ? WHERE userID = ?;";
		
		PreparedStatement currentPasswordValidationPS = connection.prepareStatement(currentPasswordValidationSqlStr);
		PreparedStatement updatePasswordPS = connection.prepareStatement(updatePasswordSqlStr);
		
		if (!newPassword.equals(confirmNewPassword)) {
			return 400;
		}
		
		currentPasswordValidationPS.setString(1, userID);
		currentPasswordValidationPS.setString(2, currentPassword);
		ResultSet currentPasswordValidationRS = currentPasswordValidationPS.executeQuery();
		
		if (!currentPasswordValidationRS.next()) {
			return 401;
		}
		
		updatePasswordPS.setString(1, newPassword);
		updatePasswordPS.setString(2, userID);
		
		int affectedRows = updatePasswordPS.executeUpdate();
				
		return affectedRows > 0 ? 200 : 500;
	}
	
	public int deleteAccount(Connection connection, String userID) throws SQLException {
		String sqlStr = " DELETE FROM users WHERE userID = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, userID);

			int affectedRows = ps.executeUpdate();
			
			return affectedRows > 0 ? 200 : 500;
		}
	}
}
