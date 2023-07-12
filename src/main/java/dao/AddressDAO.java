package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Address;

public class AddressDAO {

	public List<Address> getAddressByUserId(Connection connection, String userId) throws SQLException {
		String sqlStr = "SELECT * FROM address WHERE userId=?";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, userId);

			ResultSet resultSet = ps.executeQuery();

			List<Address> addresses = new ArrayList<>();
			while (resultSet.next()) {
				String addrId = resultSet.getString("addrId");
				String unit_number = resultSet.getString("unit_number");
				String block_number = resultSet.getString("block_number");
				String street_address = resultSet.getString("street_address");
				String postal_code = resultSet.getString("postal_code");
				String country = resultSet.getString("country");
				addresses.add(new Address(addrId, unit_number, block_number, street_address, postal_code, country));
			}
			resultSet.close();
			return addresses;
		}catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	}
}
