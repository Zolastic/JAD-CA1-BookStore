package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Address;
import utils.DBConnection;


public class AddressDAO {

	public List<Address> getAddressByUserId(Connection connection, String userId) throws SQLException {
		String sqlStr = "SELECT * FROM address JOIN country ON address.countryId=country.countryId WHERE userId=?";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, userId);

			ResultSet resultSet = ps.executeQuery();

			List<Address> addresses = new ArrayList<>();
			while (resultSet.next()) {
				String addr_id = resultSet.getString("address.addr_id");
				String unit_number = resultSet.getString("address.unit_number");
				String block_number = resultSet.getString("address.block_number");
				String street_address = resultSet.getString("address.street_address");
				String postal_code = resultSet.getString("address.postal_code");
				String countryId = resultSet.getString("country.countryId");
				String countryName = resultSet.getString("country.countryName");
				addresses.add(new Address(addr_id, unit_number, block_number, street_address, postal_code, countryId,
						countryName));
			}
			resultSet.close();
			return addresses;
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	}

	public Address getAddressByAddrId(Connection connection, String addr_id) throws SQLException {
		String sqlStr = "SELECT * FROM address JOIN country ON address.countryId=country.countryId WHERE addr_id=?";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, addr_id);
			ResultSet resultSet = ps.executeQuery();
			Address address = null;
			System.out.println("hello");
			while (resultSet.next()) {
				String unit_number = resultSet.getString("address.unit_number");
				String block_number = resultSet.getString("address.block_number");
				String street_address = resultSet.getString("address.street_address");
				String postal_code = resultSet.getString("address.postal_code");
				String countryId = resultSet.getString("country.countryId");
				String countryName = resultSet.getString("country.countryName");

				// Create an Address object based on the retrieved data
				address = new Address(addr_id, unit_number, block_number, street_address, postal_code, countryId,
						countryName);

				// Print the address object
				System.out.println(address);
			}

			System.out.print(address);
			return address;
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	}

	public int editAddress(Connection connection, Address addr) throws SQLException {
		int rowsAffected = 0;
		try {
			String addr_id = addr.getAddr_id();
			String unit_number = addr.getUnit_number();
			String block_number = addr.getBlock_number();
			String street_address = addr.getStreet_address();
			String postal_code = addr.getPostal_code();
			String countryId = addr.getCountryId();
			String countryName = addr.getCountryName();
			String updateQuery = "UPDATE address SET unit_number = ?,block_number=?,street_address=?,postal_code=?, countryId=? WHERE addr_id = ?";
			PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
			updateStatement.setString(1, unit_number);
			updateStatement.setString(2, block_number);
			updateStatement.setString(3, street_address);
			updateStatement.setString(4, postal_code);
			updateStatement.setString(5, countryId);
			updateStatement.setString(6, addr_id);
			rowsAffected = updateStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			rowsAffected = 0;
		}
		return rowsAffected;
	}

	public int insertNewAddress(Connection connection, Address addr, String userId) {
		int rowsAffected = 0;
		try {
			String addr_id = addr.getAddr_id();
			String unit_number = addr.getUnit_number();
			String block_number = addr.getBlock_number();
			String street_address = addr.getStreet_address();
			String postal_code = addr.getPostal_code();
			String countryId = addr.getCountryId();
			String countryName = addr.getCountryName();
			String insertQuery = "INSERT INTO address (addr_id,unit_number, block_number, street_address, postal_code, countryId, userId) VALUES (? ,?, ?, ?, ?, ?, ?)";
			PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
			insertStatement.setString(1, addr_id);
			insertStatement.setString(2, unit_number);
			insertStatement.setString(3, block_number);
			insertStatement.setString(4, street_address);
			insertStatement.setString(5, postal_code);
			insertStatement.setString(6, countryId);
			insertStatement.setString(7, userId);
			rowsAffected = insertStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			rowsAffected = 0;
		}
		return rowsAffected;
	}

	public boolean deleteAddr(String addr_id) {
		try (Connection connection = DBConnection.getConnection()) {
			String deleteQuery = "DELETE FROM address WHERE addr_id=?;";
			PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
			deleteStatement.setString(1, addr_id);
			int rowsAffected = deleteStatement.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println("Error: " + e);
			return false;
		}
	}

}