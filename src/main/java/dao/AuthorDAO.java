package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Author;

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
}
