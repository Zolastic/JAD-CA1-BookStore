package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Book;
import utils.DBConnection;

public class BookDAO {

	public List<Book> searchBooks(Connection connection, String userInput) throws SQLException {

		userInput = userInput == null ? "" : userInput;

		String sqlStr = "SELECT book.book_id as bookId, book.img, book.title, book.price, book.description, \r\n"
				+ "book.publication_date as publicationDate, book.ISBN, book.inventory, genre.genre_name as genreName, book.sold, \r\n"
				+ "ROUND(AVG(IFNULL(rating, 0)), 1) as rating , author.authorName, publisher.publisherName \r\n"
				+ "FROM book \r\n" + "JOIN genre ON genre.genre_id = book.genre_id \r\n"
				+ "LEFT JOIN review ON review.bookID = book.book_id \r\n"
				+ "JOIN author ON book.authorID = author.authorID \r\n"
				+ "JOIN publisher ON book.publisherID = publisher.publisherID \r\n" + "WHERE book.title LIKE ?\r\n"
				+ "GROUP BY book.book_id, book.img, book.title, book.price, \r\n"
				+ "genre.genre_name, book.sold, book.inventory, author.authorName, \r\n"
				+ "publisher.publisherName;\r\n" + "";

		try (Statement statement = connection.createStatement();
				PreparedStatement ps = connection.prepareStatement(sqlStr);) {
			ps.setString(1, "%" + userInput + "%");

			ResultSet resultSet = ps.executeQuery();

			List<Book> books = new ArrayList<>();
			while (resultSet.next()) {
				String bookID = resultSet.getString("bookID");
				String isbn = resultSet.getString("isbn");
				String title = resultSet.getString("title");
				String author = resultSet.getString("authorName");
				String publisher = resultSet.getString("publisherName");
				String publication_date = resultSet.getString("publicationDate");
				String description = resultSet.getString("description");
				String img = resultSet.getString("img");
				String genreName = resultSet.getString("genreName");
				int sold = resultSet.getInt("sold");
				int inventory = resultSet.getInt("inventory");
				double price = resultSet.getDouble("price");
				double rating = resultSet.getDouble("rating");
				books.add(new Book(bookID, isbn, title, author, publisher, publication_date, description, genreName,
						img, sold, inventory, price, rating));
			}
			resultSet.close();
			return books;
		}
	}

	// Function to get specific book details
	public Book getBookDetailsForCustomer(Connection connection, String bookID) {
		Book bookDetails = null;
		String simpleProc = "{call getBookDetails(?)}";
		try (CallableStatement cs = connection.prepareCall(simpleProc)) {
			cs.setString(1, bookID);
			cs.execute();
			try (ResultSet resultSetForBookDetails = cs.getResultSet()) {
				if (resultSetForBookDetails.next()) {
					bookDetails = new Book(resultSetForBookDetails.getString("book_id"),
							resultSetForBookDetails.getString("ISBN"), resultSetForBookDetails.getString("title"),
							resultSetForBookDetails.getString("authorName"),
							resultSetForBookDetails.getString("publisherName"),
							resultSetForBookDetails.getString("publication_date"),
							resultSetForBookDetails.getString("description"),
							resultSetForBookDetails.getString("genre_name"), resultSetForBookDetails.getString("img"),
							resultSetForBookDetails.getInt("sold"), resultSetForBookDetails.getInt("inventory"),
							resultSetForBookDetails.getDouble("price"), 1,
							resultSetForBookDetails.getDouble("average_rating"));
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return bookDetails;
	}

	public Book getBook(Connection connection, String bookID) throws SQLException {
		String sqlStr = "SELECT book.book_id as bookId, book.img, book.title, book.price, book.description, \r\n"
				+ "book.publication_date as publicationDate, book.ISBN, book.inventory, genre.genre_name as genreName, book.sold, \r\n"
				+ "ROUND(AVG(IFNULL(rating, 0)), 1) as rating , author.authorName, publisher.publisherName \r\n"
				+ "FROM book \r\n" + "JOIN genre ON genre.genre_id = book.genre_id \r\n"
				+ "LEFT JOIN review ON review.bookID = book.book_id \r\n"
				+ "JOIN author ON book.authorID = author.authorID \r\n"
				+ "JOIN publisher ON book.publisherID = publisher.publisherID \r\n" + "WHERE book.book_id = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, bookID);

			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				String isbn = resultSet.getString("isbn");
				String title = resultSet.getString("title");
				String author = resultSet.getString("authorName");
				String publisher = resultSet.getString("publisherName");
				String publication_date = resultSet.getString("publicationDate");
				String description = resultSet.getString("description");
				String img = resultSet.getString("img");
				String genreName = resultSet.getString("genreName");
				int sold = resultSet.getInt("sold");
				int inventory = resultSet.getInt("inventory");
				double price = resultSet.getDouble("price");
				double rating = resultSet.getDouble("rating");
				resultSet.close();
				Book book = new Book(bookID, isbn, title, author, publisher, publication_date, description, genreName,
						img, sold, inventory, price, rating);
				return book;
			}

			throw new RuntimeException("Book not found!!! bookID: " + bookID);
		}
	}
	
	public int addBook(String title, Double price, int author, int publisher, int quantity, String pubDate, String isbn, String description,
			int genreId, String image) throws SQLException {
		String sqlStr = "INSERT INTO BOOK (title, price, authorID, publisherID, inventory, publication_date, ISBN, description, genre_id, img, book_id)\r\n"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sqlStr)) {
	        
			ps.setString(1, title);
			ps.setDouble(2, price);
			ps.setInt(3, author);
			ps.setInt(4, publisher);
			ps.setInt(5, quantity);
			ps.setString(6, pubDate);
			ps.setString(7, isbn);
			ps.setString(8, description);
			ps.setInt(9, genreId);
			ps.setString(10, image);
			ps.setString(11, (UUID.randomUUID()).toString());
			
			int affectedRows = ps.executeUpdate();
			
			return affectedRows > 0 ? 200 : 500;
		}
	}
	
	public List<Book> getBooksByAuthorID(Connection connection, String authorID) throws SQLException {
		String sqlStr = "SELECT book_id as bookID, title FROM book\r\n"
				+ "JOIN author ON book.authorID = author.authorID\r\n"
				+ "WHERE author.authorID = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, authorID);

			ResultSet resultSet = ps.executeQuery();

			List<Book> books = new ArrayList<>();
			while (resultSet.next()) {
				String bookID = resultSet.getString("bookID");
				String title = resultSet.getString("title");
				books.add(new Book(bookID, title));
			}
			resultSet.close();
			return books;
		}

	}
	
	public List<Book> getBooksByGenreId(Connection connection, String genreID) throws SQLException {
		String sqlStr = "SELECT book_id as bookID, title FROM book\r\n"
				+ "JOIN genre ON book.genre_id = genre.genre_id\r\n"
				+ "WHERE genre.genre_id = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, genreID);

			ResultSet resultSet = ps.executeQuery();

			List<Book> books = new ArrayList<>();
			while (resultSet.next()) {
				String bookID = resultSet.getString("bookID");
				String title = resultSet.getString("title");
				books.add(new Book(bookID, title));
			}
			resultSet.close();
			return books;
		}

	}
	
	public List<Book> getBooksByPublisherID(Connection connection, String publisherID) throws SQLException {
		String sqlStr = "SELECT book_id as bookID, title FROM book\r\n"
				+ "JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "WHERE publisher.publisherID = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, publisherID);

			ResultSet resultSet = ps.executeQuery();

			List<Book> books = new ArrayList<>();
			while (resultSet.next()) {
				String bookID = resultSet.getString("bookID");
				String title = resultSet.getString("title");
				books.add(new Book(bookID, title));
			}
			resultSet.close();
			return books;
		}

	}
	
	public int deleteBook(Connection connection, String bookID) throws SQLException {
		String sqlStr = " DELETE FROM book WHERE book_id = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, bookID);

			int affectedRows = ps.executeUpdate();
			
			return affectedRows > 0 ? 200 : 500;
		}
	}
	
	public int updateBook(Connection connection, String bookID, String title, Double price, int author, int publisher, int quantity, String pubDate, String isbn, String description,
			int genreId, int sold, String image) throws SQLException {
		String sqlStrWithImage = " UPDATE book SET title = ?, price = ?, authorID = ?, publisherID = ?, inventory = ?, \r\n"
				+ " publication_date = ?, ISBN = ?, description = ?,\r\n" + " genre_id = ?, sold = ?, img = ?\r\n"
				+ " WHERE book_id = ?;";

		String sqlStrWithoutImage = " UPDATE book SET title = ?, price = ?, authorID = ?, publisherID = ?, inventory = ?, \r\n"
				+ " publication_date = ?, ISBN = ?, description = ?,\r\n" + " genre_id = ?, sold = ?\r\n"
				+ " WHERE book_id = ?;";

		boolean noImage = image == null;

		String sqlUpdate = noImage ? sqlStrWithoutImage : sqlStrWithImage;

		PreparedStatement ps = connection.prepareStatement(sqlUpdate);

		ps.setString(1, title);
		ps.setDouble(2, price);
		ps.setInt(3, author);
		ps.setInt(4, publisher);
		ps.setInt(5, quantity);
		ps.setString(6, pubDate);
		ps.setString(7, isbn);
		ps.setString(8, description);
		ps.setInt(9, genreId);
		ps.setInt(10, sold);

		if (noImage) {
			ps.setString(11, bookID);
		} else {
			ps.setString(11, image);
			ps.setString(12, bookID);
		}

		int affectedRows = ps.executeUpdate();
		System.out.printf("affectedRows: " + affectedRows);
		
		return affectedRows > 0 ? 200 : 500;
	}
}
