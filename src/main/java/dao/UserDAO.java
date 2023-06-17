package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

public class UserDAO {

	public static User getUserInfo(Connection connection, String userID) throws SQLException {
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
}
