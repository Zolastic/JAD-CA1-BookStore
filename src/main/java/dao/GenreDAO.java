package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

	public Genre getGenreById(Connection connection, String genreID) throws SQLException {
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

	public Genre getGenreByName(Connection connection, String genreName) throws SQLException {
		String sqlStr = "SELECT * FROM genre WHERE genre_name = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, genreName);

			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				String genreID = resultSet.getString("genre_id");
				String genre_img = resultSet.getString("genre_img");
				Genre genre = new Genre(genreID, genreName, genre_img);
				return genre;
			}
			return null;
		}

	}

	public List<Genre> getGenres(Connection connection) throws SQLException {
		try (Statement genreStatement = connection.createStatement();
				ResultSet genreResultSet = genreStatement.executeQuery(
						"SELECT genre_id as genreId, genre_name as genreName, genre_img as image FROM genre;");) {

			List<Genre> genres = new ArrayList<>();
			while (genreResultSet.next()) {
				String genreId = genreResultSet.getString("genreId");
				String genreName = genreResultSet.getString("genreName");
				String genreImage = genreResultSet.getString("image");
				genres.add(new Genre(genreId, genreName, genreImage));
			}

			return genres;
		}
	}

	public int addGenre(Connection connection, String genreName, String image) throws SQLException {
		Genre genre = getGenreByName(connection, genreName);
		if (genre != null) {
			return 409;
		}
		String addGenreSqlStr = "INSERT INTO genre (genre_id, genre_name, genre_img) VALUES (?, ?, ?);";
		try (PreparedStatement addGenrePS = connection.prepareStatement(addGenreSqlStr);) {

			addGenrePS.setString(1, UUID.randomUUID().toString());
			addGenrePS.setString(2, genreName);
			addGenrePS.setString(3, image);

			int affectedRows = addGenrePS.executeUpdate();

			return affectedRows > 0 ? 200 : 500;
		}
	}
	
	public int deleteGenre(Connection connection, String genreID) throws SQLException {
		String sqlStr = " DELETE FROM genre WHERE genre_id = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, genreID);

			int affectedRows = ps.executeUpdate();
			
			return affectedRows > 0 ? 200 : 500;
		}
	}
	
	public int updateGenre(Connection connection, String genreID, String genreName, String image) throws SQLException {
		String sqlStrWithImage = "UPDATE genre SET genre_name = ?, genre_img = ? WHERE genre_id = ?;";
		String sqlStrWithoutImage = "UPDATE genre SET genre_name = ? WHERE genre_id = ?;";

		boolean noImage = image == null;
		String sqlUpdate = noImage ? sqlStrWithoutImage : sqlStrWithImage;
		
		PreparedStatement ps = connection.prepareStatement(sqlUpdate);

		ps.setString(1, genreName);
		
		if (noImage) {
			ps.setString(2, genreID);
		} else {
			ps.setString(2, image);
			ps.setString(3, genreID);
		}


		int affectedRows = ps.executeUpdate();
		System.out.printf("affectedRows: " + affectedRows);

		return affectedRows > 0 ? 200 : 500;
	}
}
