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
import utils.CloudinaryUtil;
import utils.DBConnection;

public class BookDAO {

	// Admin
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

	// Get all the books from db
	public List<Book> getAllBooksWOPage(Connection connection) {
		List<Book> allBooks = new ArrayList<>();
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Book book = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
							rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
							rs.getString("description"), rs.getString("genre_name"), rs.getString("img"),
							rs.getInt("sold"), rs.getInt("inventory"), rs.getDouble("price"), 1,
							rs.getDouble("average_rating"));
					allBooks.add(book);
				}
			}
		} catch (SQLException e) {
			allBooks = null;
			System.err.println("Error: " + e.getMessage());
		}
		return allBooks;
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

	public int addBook(String title, Double price, String author, String publisher, int quantity, String pubDate,
			String isbn, String description, String genreId, String image, String imagePublicID) throws SQLException {
		String sqlStr = "INSERT INTO BOOK (title, price, authorID, publisherID, inventory, publication_date, ISBN, description, genre_id, img, img_public_id, book_id)\r\n"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sqlStr)) {

			ps.setString(1, title);
			ps.setDouble(2, price);
			ps.setString(3, author);
			ps.setString(4, publisher);
			ps.setInt(5, quantity);
			ps.setString(6, pubDate);
			ps.setString(7, isbn);
			ps.setString(8, description);
			ps.setString(9, genreId);
			ps.setString(10, image);
			ps.setString(11, imagePublicID);
			ps.setString(12, (UUID.randomUUID()).toString());

			int affectedRows = ps.executeUpdate();

			return affectedRows > 0 ? 200 : 500;
		}
	}

	public List<Book> getBooksByAuthorID(Connection connection, String authorID) throws SQLException {
		String sqlStr = "SELECT book_id as bookID, title FROM book\r\n"
				+ "JOIN author ON book.authorID = author.authorID\r\n" + "WHERE author.authorID = ?;";
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
				+ "JOIN genre ON book.genre_id = genre.genre_id\r\n" + "WHERE genre.genre_id = ?;";
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
				+ "JOIN publisher ON book.publisherID = publisher.publisherID\r\n" + "WHERE publisher.publisherID = ?;";
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
	
	public String getBookImagePublicID(Connection connection, String bookID) throws SQLException {
		String sqlStr = "Select img_public_id FROM book WHERE book_id = ?";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, bookID);

			ResultSet resultSet = ps.executeQuery();
			
			if (resultSet.next()) {
				return resultSet.getString("img_public_id");
			}

			return null;
		}
	}

	public int updateBook(Connection connection, String bookID, String title, Double price, String author,
			String publisher, int quantity, String pubDate, String isbn, String description, String genreId, int sold,
			String image, String imagePublicID) throws SQLException {
		String sqlStrWithImage = " UPDATE book SET title = ?, price = ?, authorID = ?, publisherID = ?, inventory = ?, \r\n"
				+ " publication_date = ?, ISBN = ?, description = ?,\r\n" + " genre_id = ?, sold = ?, img = ?,\r\n" + "img_public_id = ?"
				+ " WHERE book_id = ?;";

		String sqlStrWithoutImage = " UPDATE book SET title = ?, price = ?, authorID = ?, publisherID = ?, inventory = ?, \r\n"
				+ " publication_date = ?, ISBN = ?, description = ?,\r\n" + " genre_id = ?, sold = ?\r\n"
				+ " WHERE book_id = ?;";

		boolean noImage = image == null;

		String sqlUpdate = noImage ? sqlStrWithoutImage : sqlStrWithImage;

		PreparedStatement ps = connection.prepareStatement(sqlUpdate);

		ps.setString(1, title);
		ps.setDouble(2, price);
		ps.setString(3, author);
		ps.setString(4, publisher);
		ps.setInt(5, quantity);
		ps.setString(6, pubDate);
		ps.setString(7, isbn);
		ps.setString(8, description);
		ps.setString(9, genreId);
		ps.setInt(10, sold);

		if (noImage) {
			ps.setString(11, bookID);
		} else {
			ps.setString(11, image);
			ps.setString(12, imagePublicID);
			ps.setString(13, bookID);
		}

		int affectedRows = ps.executeUpdate();
		System.out.printf("affectedRows: " + affectedRows);

		return affectedRows > 0 ? 200 : 500;
	}

	public List<Book> getBestSellingBooks(Connection connection) throws SQLException {
		String sqlStr = "SELECT book.book_id as bookId, book.img, book.title, book.price, book.description, \r\n"
				+ "book.publication_date as publicationDate, book.ISBN, book.inventory, genre.genre_name as genreName, book.sold, \r\n"
				+ "ROUND(AVG(IFNULL(rating, 0)), 1) as rating , author.authorName, publisher.publisherName \r\n"
				+ "FROM book \r\n" + "JOIN genre ON genre.genre_id = book.genre_id \r\n"
				+ "LEFT JOIN review ON review.bookID = book.book_id \r\n"
				+ "JOIN author ON book.authorID = author.authorID \r\n"
				+ "JOIN publisher ON book.publisherID = publisher.publisherID \r\n"
				+ "GROUP BY book.book_id, book.img, book.title, book.price, \r\n"
				+ "genre.genre_name, book.sold, book.inventory, author.authorName, \r\n"
				+ "publisher.publisherName ORDER BY sold DESC LIMIT 10;\r\n" + "";

		try (Statement statement = connection.createStatement();
				PreparedStatement ps = connection.prepareStatement(sqlStr);) {

			ResultSet resultSet = ps.executeQuery();

			List<Book> books = new ArrayList<>();
			while (resultSet.next()) {
				Book book = new Book();
				book.setBookID(resultSet.getString("bookId"));
				book.setImg(resultSet.getString("img"));
				book.setTitle(resultSet.getString("title"));
				book.setDescription(resultSet.getString("description"));
				book.setAuthor(resultSet.getString("authorName"));
				book.setRating(resultSet.getDouble("rating"));
				book.setSold(resultSet.getInt("sold"));
				book.setInventory(resultSet.getInt("inventory"));
				book.setISBN(resultSet.getString("ISBN"));
				book.setPrice(resultSet.getDouble("price"));
				books.add(book);
			}
			resultSet.close();
			return books;
		}
	}

	public List<Book> getLeastSellingBooks(Connection connection) throws SQLException {
		String sqlStr = "SELECT book.book_id as bookId, book.img, book.title, book.price, book.description, \r\n"
				+ "book.publication_date as publicationDate, book.ISBN, book.inventory, genre.genre_name as genreName, book.sold, \r\n"
				+ "ROUND(AVG(IFNULL(rating, 0)), 1) as rating , author.authorName, publisher.publisherName \r\n"
				+ "FROM book \r\n" + "JOIN genre ON genre.genre_id = book.genre_id \r\n"
				+ "LEFT JOIN review ON review.bookID = book.book_id \r\n"
				+ "JOIN author ON book.authorID = author.authorID \r\n"
				+ "JOIN publisher ON book.publisherID = publisher.publisherID \r\n"
				+ "GROUP BY book.book_id, book.img, book.title, book.price, \r\n"
				+ "genre.genre_name, book.sold, book.inventory, author.authorName, \r\n"
				+ "publisher.publisherName ORDER BY sold LIMIT 10;\r\n" + "";

		try (Statement statement = connection.createStatement();
				PreparedStatement ps = connection.prepareStatement(sqlStr);) {

			ResultSet resultSet = ps.executeQuery();

			List<Book> books = new ArrayList<>();
			while (resultSet.next()) {
				Book book = new Book();
				book.setBookID(resultSet.getString("bookId"));
				book.setImg(resultSet.getString("img"));
				book.setTitle(resultSet.getString("title"));
				book.setDescription(resultSet.getString("description"));
				book.setAuthor(resultSet.getString("authorName"));
				book.setRating(resultSet.getDouble("rating"));
				book.setSold(resultSet.getInt("sold"));
				book.setInventory(resultSet.getInt("inventory"));
				book.setISBN(resultSet.getString("ISBN"));
				book.setPrice(resultSet.getDouble("price"));
				books.add(book);
			}
			resultSet.close();
			return books;
		}
	}

	public List<Book> getBooksWithLowStockLevel(Connection connection) throws SQLException {
		String sqlStr = "SELECT\r\n" + "    book.book_id AS bookId,\r\n" + "    book.img,\r\n" + "    book.title,\r\n"
				+ "    book.price,\r\n" + "    book.description,\r\n"
				+ "    book.publication_date AS publicationDate,\r\n" + "    book.ISBN,\r\n" + "    book.inventory,\r\n"
				+ "    genre.genre_name AS genreName,\r\n" + "    book.sold,\r\n"
				+ "    ROUND(AVG(IFNULL(rating, 0)), 1) AS rating,\r\n" + "    author.authorName,\r\n"
				+ "    publisher.publisherName\r\n" + "FROM\r\n" + "    book\r\n" + "JOIN\r\n"
				+ "    genre ON genre.genre_id = book.genre_id\r\n" + "LEFT JOIN\r\n"
				+ "    review ON review.bookID = book.book_id\r\n" + "JOIN\r\n"
				+ "    author ON book.authorID = author.authorID\r\n" + "JOIN\r\n"
				+ "    publisher ON book.publisherID = publisher.publisherID\r\n" + "WHERE\r\n"
				+ "	book.inventory < 10\r\n" + "GROUP BY\r\n" + "    book.book_id,\r\n" + "    book.img,\r\n"
				+ "    book.title,\r\n" + "    book.price,\r\n" + "    genre.genre_name,\r\n" + "    book.sold,\r\n"
				+ "    book.inventory,\r\n" + "    author.authorName,\r\n" + "    publisher.publisherName\r\n"
				+ "ORDER BY book.inventory;";

		try (Statement statement = connection.createStatement();
				PreparedStatement ps = connection.prepareStatement(sqlStr);) {

			ResultSet resultSet = ps.executeQuery();

			List<Book> books = new ArrayList<>();
			while (resultSet.next()) {
				Book book = new Book();
				book.setBookID(resultSet.getString("bookId"));
				book.setImg(resultSet.getString("img"));
				book.setTitle(resultSet.getString("title"));
				book.setDescription(resultSet.getString("description"));
				book.setAuthor(resultSet.getString("authorName"));
				book.setRating(resultSet.getDouble("rating"));
				book.setSold(resultSet.getInt("sold"));
				book.setInventory(resultSet.getInt("inventory"));
				book.setISBN(resultSet.getString("ISBN"));
				book.setPrice(resultSet.getDouble("price"));
				books.add(book);
			}
			resultSet.close();
			return books;
		}
	}

	// Users & Public
	// Function to get specific book details
	public Book getBookDetailsForBybookID(Connection connection, String bookID) {
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
			bookDetails = null;
			System.err.println("Error: " + e.getMessage());
		}
		return bookDetails;
	}

	// Get all the books from db
	public List<Book> getAllBooks(Connection connection, int page) {
		List<Book> allBooks = new ArrayList<>();
		int pageSize = 10; // Number of books per page
		int offset = (page - 1) * pageSize;
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName LIMIT ?, ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setInt(1, offset);
			ps.setInt(2, pageSize);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Book book = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
							rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
							rs.getString("description"), rs.getString("genre_name"), rs.getString("img"),
							rs.getInt("sold"), rs.getInt("inventory"), rs.getDouble("price"), 1,
							rs.getDouble("average_rating"));
					allBooks.add(book);
				}
			}
		} catch (SQLException e) {
			allBooks = null;
			System.err.println("Error: " + e.getMessage());
		}
		return allBooks;
	}

	// Get total page for all books
	public int getTotalPagesForAllBooks(Connection connection) {
		int pageSize = 10;
		String countSqlStr = "SELECT COUNT(*) FROM book";
		try (PreparedStatement countPs = connection.prepareStatement(countSqlStr)) {
			try (ResultSet countRs = countPs.executeQuery()) {
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

	// Get the search results
	public List<Book> searchBookByTitle(Connection connection, String searchInput, int page) {
		List<Book> searchResults = new ArrayList<>();
		int pageSize = 10; // Number of books per page
		int offset = (page - 1) * pageSize;
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    WHERE book.title LIKE ?\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName LIMIT ?, ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, searchInput);
			ps.setInt(2, offset);
			ps.setInt(3, pageSize);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Book searchResult = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
							rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
							rs.getString("description"), rs.getString("genre_name"), rs.getString("img"),
							rs.getInt("sold"), rs.getInt("inventory"), rs.getDouble("price"), 1,
							rs.getDouble("average_rating"));
					searchResults.add(searchResult);
				}
			}
		} catch (SQLException e) {
			searchResults = null;
			System.err.println("Error: " + e.getMessage());
		}
		return searchResults;
	}

	// Total Pages for search book by title
	public int getTotalPagesForSearch(Connection connection, String searchInput) {
		int pageSize = 10;
		String countSqlStr = "SELECT COUNT(*) FROM book WHERE book.title LIKE ?";
		try (PreparedStatement count = connection.prepareStatement(countSqlStr)) {
			count.setString(1, searchInput);
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

	// Getting popular books
	public List<Book> popularBooks(Connection connection) {
		List<Book> popularBooks = new ArrayList<>();

		String query = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(review.rating) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName FROM book JOIN genre ON genre.genre_id = book.genre_id LEFT JOIN review ON review.bookID = book.book_id JOIN author ON book.authorID = author.authorID JOIN publisher ON book.publisherID = publisher.publisherID GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName ORDER BY book.sold DESC LIMIT 6;";

		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				String bookID = resultSet.getString("book_id");
				String iSBN = resultSet.getString("ISBN");
				String title = resultSet.getString("title");
				String author = resultSet.getString("authorName");
				String publisher = resultSet.getString("publisherName");
				String publicationDate = resultSet.getString("publication_date");
				String description = resultSet.getString("description");
				String genreName = resultSet.getString("genre_name");
				String img = resultSet.getString("img");
				int sold = resultSet.getInt("sold");
				int inventory = resultSet.getInt("inventory");
				double price = resultSet.getDouble("price");
				double rating = resultSet.getDouble("average_rating");

				Book popularBook = new Book(bookID, iSBN, title, author, publisher, publicationDate, description,
						genreName, img, sold, inventory, price, rating);
				popularBooks.add(popularBook);
			}
		} catch (SQLException e) {
			popularBooks = null;
			System.err.println("Error: " + e.getMessage());
		}

		return popularBooks;
	}

}
