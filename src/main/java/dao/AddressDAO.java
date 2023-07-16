package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Address;

public class AddressDAO {
	public ArrayList<Address> getAddressesByPostalCode(Connection connection) throws SQLException {
		String getAddressesSql = "SELECT * FROM address ORDER BY postal_code;";
		ArrayList<Address> addresses = new ArrayList<>();
		
		try(PreparedStatement ps = connection.prepareStatement(getAddressesSql)) {

			ResultSet resultSet = ps.executeQuery();
			
			while (resultSet.next()) {
				Address address = new Address(); 
				address.setAddressID(resultSet.getString("addr_id"));
				address.setUnitNumber(resultSet.getString("unit_number"));
				address.setBlockNumber(resultSet.getString("block_number"));
				address.setStreetAddress(resultSet.getString("street_address"));
				address.setPostalCode(resultSet.getString("postal_code"));
				address.setCountryID(resultSet.getString("countryId"));
				address.setUserID(resultSet.getString("userId"));
			}
			resultSet.close();
			return addresses;
		} 
	}
}
