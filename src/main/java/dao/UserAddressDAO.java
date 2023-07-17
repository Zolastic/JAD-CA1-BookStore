package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.UserAddress;

public class UserAddressDAO {
	public ArrayList<UserAddress> getUserOrderByPostalCode(Connection connection) throws SQLException {
		String getAddressesSql = "SELECT u.userID, u.name, u.email, u.img, \r\n"
				+ "a.addr_id, a.unit_number, a.block_number, a.street_address, a.postal_code, a.countryId\r\n"
				+ "FROM users u, address a \r\n"
				+ "WHERE a.userId = u.userID ORDER BY a.postal_code;";
		ArrayList<UserAddress> users = new ArrayList<>();

		try (PreparedStatement ps = connection.prepareStatement(getAddressesSql)) {

			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				UserAddress user = new UserAddress();
				user.setUserID(resultSet.getString("userID"));
				user.setName(resultSet.getString("name"));
				user.setEmail(resultSet.getString("email"));
				user.setImage(resultSet.getString("img"));
				user.setAddressID(resultSet.getString("addr_id"));
				user.setUnitNumber(resultSet.getString("unit_number"));
				user.setBlockNumber(resultSet.getString("block_number"));
				user.setStreetAddress(resultSet.getString("street_address"));
				user.setPostalCode(resultSet.getString("postal_code"));
				user.setCountryID(resultSet.getString("countryId"));
				user.setUserID(resultSet.getString("userId"));
				users.add(user);
			}
			resultSet.close();
			return users;
		}
	}
}
