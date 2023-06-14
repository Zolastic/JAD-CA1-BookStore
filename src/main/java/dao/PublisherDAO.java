package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Author;
import model.Publisher;

public class PublisherDAO {
	public List<Publisher> searchPublishers(Connection connection, String userInput) throws SQLException {

		userInput = userInput == null ? "" : userInput;

		String sqlStr = "SELECT * FROM publisher WHERE publisherName LIKE ?;";

		try (Statement statement = connection.createStatement();
				PreparedStatement ps = connection.prepareStatement(sqlStr);) {
			ps.setString(1, "%" + userInput + "%");

			ResultSet resultSet = ps.executeQuery();

			List<Publisher> publishers = new ArrayList<>();
			while (resultSet.next()) {
				String publisherID = resultSet.getString("publisherID");
				String publisherName = resultSet.getString("publisherName");
				publishers.add(new Publisher(publisherID, publisherName));
			}
			resultSet.close();
			return publishers;
		}
	}
	
	public Publisher getPublisher(Connection connection, String publisherID) throws SQLException {
		String sqlStr = "SELECT * FROM publisher WHERE publisherID = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, publisherID);

			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				String publisherName = resultSet.getString("publisherName");
				Publisher publisher = new Publisher(publisherID, publisherName);
				return publisher;
			}

			throw new RuntimeException("Book not found!!! publisherID: " + publisherID);
		}

	}
}
