package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Address;
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

	public User validateUserCredentials(Connection connection, String email, String password) throws SQLException {
		String validateCredentialsSqlStr = "SELECT * FROM users WHERE email = ? and password = ?;";
		try (PreparedStatement validateCredentialsPS = connection.prepareStatement(validateCredentialsSqlStr)) {
			validateCredentialsPS.setString(1, email);
			validateCredentialsPS.setString(2, password);
			ResultSet resultSet = validateCredentialsPS.executeQuery();

			if (resultSet.next()) {
				String userID = resultSet.getString("userID");
				String name = resultSet.getString("name");
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

	public boolean checkForExistingUserEmail(Connection connection, String email) throws SQLException {
		String duplicateCheckSqlStr = "SELECT * FROM users WHERE email = ?;";
		try (PreparedStatement duplicateCheckPS = connection.prepareStatement(duplicateCheckSqlStr)) {
			duplicateCheckPS.setString(1, email);
			ResultSet resultSet = duplicateCheckPS.executeQuery();

			if (resultSet.next()) {
				return true;
			}
			return false;
		}
	}

	public int addUser(Connection connection, String name, String email, String password, String customerID,
			String secret) throws SQLException {
		String insertUserSqlStr = "INSERT INTO users (userID, name, email, password, role, secret) VALUES (?, ?, ?, ?, \"customer\", ?);";
		try (PreparedStatement insertUserPS = connection.prepareStatement(insertUserSqlStr)) {
			insertUserPS.setString(1, customerID);
			insertUserPS.setString(2, name);
			insertUserPS.setString(3, email);
			insertUserPS.setString(4, password);
			insertUserPS.setString(5, secret);
			int affectedUserRows = insertUserPS.executeUpdate();

			return affectedUserRows;
		}
	}

	public int updateUser(Connection connection, String name, String email, String image, String userID)
			throws SQLException {
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

	public int updateUserPassword(Connection connection, String userID, String currentPassword, String newPassword,
			String confirmNewPassword) throws SQLException {
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

	public int updateUserPassword(Connection connection, String userID, String newPassword, String confirmNewPassword)
			throws SQLException {
		String updatePasswordSqlStr = "UPDATE users SET password = ? WHERE userID = ?;";

		PreparedStatement updatePasswordPS = connection.prepareStatement(updatePasswordSqlStr);

		if (!newPassword.equals(confirmNewPassword)) {
			return 400;
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

	public ArrayList<User> getUsers(Connection connection, String userInput) throws SQLException {
		userInput = userInput == null ? "" : userInput;
		String getUsersSql = "SELECT * FROM users WHERE name LIKE ? OR email LIKE ?;";
		ArrayList<User> users = new ArrayList<>();

		try (PreparedStatement ps = connection.prepareStatement(getUsersSql)) {
			ps.setString(1, "%" + userInput + "%");
			ps.setString(2, "%" + userInput + "%");
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				String userID = resultSet.getString("userID");
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String password = resultSet.getString("password");
				String role = resultSet.getString("role");
				String img = resultSet.getString("img");
				String secret = resultSet.getString("secret");
				User user = new User(userID, name, email, password, role, img, secret);
				users.add(user);
			}
			resultSet.close();
			return users;
		}
	}

	public int verifyUserIsAdmin(Connection connection, String userID) throws SQLException {
		String sqlStr = "SELECT role FROM users WHERE userID = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, userID);

			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				String role = resultSet.getString("role");
				return role.equals("admin") ? 200 : 401;
			}

			return 500;
		}
	}
}
