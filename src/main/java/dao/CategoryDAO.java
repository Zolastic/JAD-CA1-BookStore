package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Author;
import model.Book;
import model.Genre;
import utils.DBConnection;

public class CategoryDAO {
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
	public int getTotalPagesByGenreSearch(Connection connection, String genreID, String searchInput){
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
	public List<Book> searchBookByTitle(Connection connection, String genreID, String searchInput, int page){
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

	// Get all the genre
	public List<Genre> getAllGenres(Connection connection) {
		List<Genre> allGenre = new ArrayList<>();
		try (Statement stmt = connection.createStatement()) {
			String sqlStr = "SELECT * FROM genre;";
			ResultSet rs = stmt.executeQuery(sqlStr);

			while (rs.next()) {
				Genre genre = new Genre(rs.getString("genre_id"), rs.getString("genre_name"),
						rs.getString("genre_img"));
				allGenre.add(genre);
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}

		return allGenre;
	}


}
