package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Country;

public class CountryDAO {
	public List<Country> getAllCountry(Connection connection) throws SQLException {
		String sqlStr = "SELECT * FROM country";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ResultSet resultSet = ps.executeQuery();
			List<Country> countries = new ArrayList<>();
			while (resultSet.next()) {
				String countryId = resultSet.getString("countryId");
				String countryName = resultSet.getString("countryName");
				countries.add(new Country(countryId, countryName));
			}
			resultSet.close();
			return countries;
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	}

}
