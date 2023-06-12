package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Book;

public class BookDAO {
	
	public List<Book> searchBooks(Connection connection, String userInput) throws SQLException {
		
		userInput = userInput == null ? "" : userInput;
		
		String sqlStr = "SELECT book.book_id as bookId, book.img, book.title, book.price, book.description, \r\n"
				+ "book.publication_date as publicationDate, book.ISBN, book.inventory, genre.genre_name as genreName, book.sold, \r\n"
				+ "ROUND(AVG(IFNULL(rating, 0)), 1) as rating , author.authorName, publisher.publisherName \r\n"
				+ "FROM book \r\n" + "JOIN genre ON genre.genre_id = book.genre_id \r\n"
				+ "LEFT JOIN review ON review.bookID = book.book_id \r\n"
				+ "JOIN author ON book.authorID = author.authorID \r\n"
				+ "JOIN publisher ON book.publisherID = publisher.publisherID \r\n"
				+ "WHERE book.title LIKE ?\r\n"
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
}