package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Book;
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

	// Get book based on their genre
	public List<Book> getBooksByGenre(Connection connection, String genreID, int page) {
		List<Book> allGenreBook = new ArrayList<>();
		int pageSize = 10; // Number of books per page
		int offset = (page - 1) * pageSize;
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    WHERE book.genre_id = ?\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName LIMIT ?, ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, genreID);
			ps.setInt(2, offset);
			ps.setInt(3, pageSize);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Book genreBook = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
						rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
						rs.getString("description"), rs.getString("genre_name"), rs.getString("img"), rs.getInt("sold"),
						rs.getInt("inventory"), rs.getDouble("price"), 1, rs.getDouble("average_rating"));
				allGenreBook.add(genreBook);
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return allGenreBook;
	}

	// Get total pages of genre
	public int getTotalPagesByGenre(Connection connection, String genreID) {
		int pageSize = 10;
		String countSqlStr = "SELECT COUNT(*) FROM book WHERE book.genre_id = ?";
		try (PreparedStatement count = connection.prepareStatement(countSqlStr)) {
			count.setString(1, genreID);
			try (ResultSet countRs = count.executeQuery()) {
				if (countRs.next()) {
					int totalBooks = countRs.getInt(1);
					return (int) Math.ceil((double) totalBooks / pageSize);
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return 0;
		}
	}

	// Get total pages of genre search
	public int getTotalPagesByGenreSearch(Connection connection, String genreID, String searchInput) {
		int pageSize = 10;
		String countSqlStr = "SELECT COUNT(*) FROM book WHERE book.genre_id = ? AND book.title LIKE ?";
		try (PreparedStatement count = connection.prepareStatement(countSqlStr)) {
			count.setString(1, genreID);
			count.setString(2, searchInput);
			try (ResultSet countRs = count.executeQuery()) {
				if (countRs.next()) {
					int totalBooks = countRs.getInt(1);
					return (int) Math.ceil((double) totalBooks / pageSize);
				} else {
					return 0;
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return 0;
		}
	}

	// search book by their title
	public List<Book> searchBookByTitle(Connection connection, String genreID, String searchInput, int page) {
		List<Book> allGenreBook = new ArrayList<>();
		int pageSize = 10; // Number of books per page
		int offset = (page - 1) * pageSize;
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    WHERE book.genre_id = ?\r\n" + "	   AND book.title LIKE ?\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName LIMIT ?, ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, genreID);
			ps.setString(2, searchInput);
			ps.setInt(3, offset);
			ps.setInt(4, pageSize);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Book genreBook = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
						rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
						rs.getString("description"), rs.getString("genre_name"), rs.getString("img"), rs.getInt("sold"),
						rs.getInt("inventory"), rs.getDouble("price"), 1, rs.getDouble("average_rating"));
				allGenreBook.add(genreBook);
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return allGenreBook;
	}

}
