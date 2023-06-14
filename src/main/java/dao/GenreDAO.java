package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Genre;

public class GenreDAO {
	
	public List<Genre> searchGenres(Connection connection, String userInput) throws SQLException {

		userInput = userInput == null ? "" : userInput;

		String sqlStr = "SELECT genre_id as genreID, genre_Name as genreName, genre_img as image \r\n"
				+ "FROM genre\r\n" + "WHERE genre_name LIKE ?;";

		try (Statement statement = connection.createStatement();
				PreparedStatement ps = connection.prepareStatement(sqlStr);) {
			ps.setString(1, "%" + userInput + "%");

			ResultSet resultSet = ps.executeQuery();

			List<Genre> genres = new ArrayList<>();
			while (resultSet.next()) {
				String genreID = resultSet.getString("genreID");
				String genreName = resultSet.getString("genreName");
				String image = resultSet.getString("image");

				genres.add(new Genre(genreID, genreName, image));
			}
			resultSet.close();
			return genres;
		}
	}
	
	public Genre getGenre(Connection connection, String genreID) throws SQLException {
		String sqlStr = "SELECT * FROM genre WHERE genre_id = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, genreID);

			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				String genreName = resultSet.getString("genre_name");
				String genre_img = resultSet.getString("genre_img");
				Genre genre = new Genre(genreID, genreName, genre_img);
				return genre;
			}

			throw new RuntimeException("Genre not found!!! genreID: " + genreID);
		}

	}
}
