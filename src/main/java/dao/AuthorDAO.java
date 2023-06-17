package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;

import model.Author;
import utils.DBConnection;

public class AuthorDAO {
	
	public List<Author> searchAuthors(Connection connection, String userInput) throws SQLException {

		userInput = userInput == null ? "" : userInput;

		String sqlStr = "SELECT * FROM author WHERE authorName LIKE ?;";

		try (Statement statement = connection.createStatement();
				PreparedStatement ps = connection.prepareStatement(sqlStr);) {
			ps.setString(1, "%" + userInput + "%");

			ResultSet resultSet = ps.executeQuery();

			List<Author> authors = new ArrayList<>();
			while (resultSet.next()) {
				String authorID = resultSet.getString("authorID");
				String authorName = resultSet.getString("authorName");
				authors.add(new Author(authorID, authorName));
			}
			resultSet.close();
			return authors;
		}
	}
	
	public Author getAuthorById(Connection connection, String authorID) throws SQLException {
		String sqlStr = "SELECT * FROM author WHERE authorID = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, authorID);

			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				String authorName = resultSet.getString("authorName");
				Author author = new Author(authorID, authorName);
				return author;
			}

			// TODO - Revise later
			throw new RuntimeException("Author not found!!! authorID: " + authorID);
		}

	}
	
	public Author getAuthorByName(Connection connection, String authorName) throws SQLException {
		String sqlStr = "SELECT * FROM author WHERE authorName = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, authorName);

			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				String authorID = resultSet.getString("authorID");
				Author author = new Author(authorID, authorName);
				return author;
			}

			return null;
		}

	}
	
	public List<Author> getAuthors(Connection connection) throws SQLException {
		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement
							.executeQuery("SELECT * FROM author;");) {
				
				List<Author> authors = new ArrayList<>();
				while (resultSet.next()) {
					String authorId = resultSet.getString("authorID");
					String authorName = resultSet.getString("authorName");
					authors.add(new Author(authorId, authorName));
				}
				
				return authors;
			} 
	}
	
	public int addAuthor(Connection connection, String authorName) throws SQLException {
		Author author = getAuthorByName(connection, authorName);
		if (author != null) {
			return 409;
		}
		
		String addAuthorsqlStr = "INSERT INTO author (authorID, authorName) VALUES (?, ?);";
		try (PreparedStatement addAuthorPS = connection.prepareStatement(addAuthorsqlStr)) {
			
			addAuthorPS.setString(1, (UUID.randomUUID()).toString());
			addAuthorPS.setString(2, authorName);

			int affectedRows = addAuthorPS.executeUpdate();

			return affectedRows > 0 ? 200 : 500;
		}
	}
	
	public int deleteAuthor(Connection connection, String authorID) throws SQLException {
		String sqlStr = " DELETE FROM author WHERE authorID = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, authorID);

			int affectedRows = ps.executeUpdate();
			
			return affectedRows > 0 ? 200 : 500;
		}
	}
	
	public int updateAuthor(Connection connection, String authorID, String authorName) throws SQLException {

		String sqlStr = "UPDATE author SET authorName = ? WHERE authorID = ?";

		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, authorName);
			ps.setString(2, authorID);

			int affectedRows = ps.executeUpdate();

			return affectedRows > 0 ? 200 : 500;
		}
	}
}
