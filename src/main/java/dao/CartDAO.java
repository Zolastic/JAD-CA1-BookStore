package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Book;
import utils.DBConnection;

public class CartDAO {
	// Function to validate user id
	public String getCartID(Connection connection, String custID) {
		String cartID = null;
		try {
			String query = "SELECT cartID FROM cart WHERE custID = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setString(1, custID);

			ResultSet resultSet = statement.executeQuery();

			if (resultSet.next()) {
				cartID = resultSet.getString("cartID");
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			cartID = null;
		}

		return cartID;
	}

	// Function to add to cart
	public int addToCart(Connection connection, String cartID, String bookID, int quantity) {
		int rowsAffected = 0;
		try {
			String checkQuery = "SELECT Qty FROM cart_items WHERE cartID = ? AND BookID = ?";
			PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
			checkStatement.setString(1, cartID);
			checkStatement.setString(2, bookID);
			ResultSet checkResultSet = checkStatement.executeQuery();

			if (checkResultSet.next()) {
				int currentQuantity = checkResultSet.getInt("Qty");
				quantity += currentQuantity;

				String updateQuery = "UPDATE cart_items SET Qty = ? WHERE cartID = ? AND BookID = ?";
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
				updateStatement.setInt(1, quantity);
				updateStatement.setString(2, cartID);
				updateStatement.setString(3, bookID);
				rowsAffected = updateStatement.executeUpdate();
			} else {
				String insertQuery = "INSERT INTO cart_items (cartID, Qty, BookID) VALUES (?, ?, ?)";
				PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
				insertStatement.setString(1, cartID);
				insertStatement.setInt(2, quantity);
				insertStatement.setString(3, bookID);

				rowsAffected = insertStatement.executeUpdate();
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			rowsAffected = 0;
		}

		return rowsAffected;
	}

	// Function to get cart items
	public List<Book> getCartItems(Connection connection, String userID) {
		List<Book> cartItems = new ArrayList<>();
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName, cart_items.Qty, cart_items.selected\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    JOIN cart ON cart.custID=?\r\n" + "    JOIN cart_items ON cart.cartID=cart_items.cartID\r\n"
				+ "    WHERE cart_items.BookID=book.book_id\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, userID);
			ResultSet resultSet = ps.executeQuery();
			while (resultSet.next()) {
				String bookID = resultSet.getString("book_id");
				String isbn = resultSet.getString("ISBN");
				String title = resultSet.getString("title");
				String author = resultSet.getString("authorName");
				String publisher = resultSet.getString("publisherName");
				String publication_date = resultSet.getString("publication_date");
				String description = resultSet.getString("description");
				String genre_name = resultSet.getString("genre_name");
				String img = resultSet.getString("img");
				int sold = resultSet.getInt("sold");
				int inventory = resultSet.getInt("inventory");
				double price = resultSet.getDouble("price");
				double rating = resultSet.getDouble("average_rating");
				int quantity = resultSet.getInt("Qty");
				int selected = resultSet.getInt("selected");
				cartItems.add(new Book(bookID, isbn, title, author, publisher, publication_date, description,
						genre_name, img, sold, inventory, price, rating, quantity, selected));

			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return cartItems;
	}

	public boolean deleteCartItem(String cartID, String bookID) {
		try (Connection connection = DBConnection.getConnection()) {
			String deleteQuery = "DELETE FROM cart_items WHERE cartID=? AND BookID=?;";
			PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
			deleteStatement.setString(1, cartID);
			deleteStatement.setString(2, bookID);
			int rowsAffected = deleteStatement.executeUpdate();
			return rowsAffected > 0;
		} catch (SQLException e) {
			System.err.println("Error: " + e);
			return false;
		}
	}

	public int updateCartItemSelection(Connection connection, String cartID, String bookID, int newSelection) {
		int rowsAffected = 0;
		try {
			String updateQuery = "UPDATE cart_items SET selected=? WHERE cartID=? AND BookID=?;";
			PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
			updateStatement.setInt(1, newSelection);
			updateStatement.setString(2, cartID);
			updateStatement.setString(3, bookID);
			rowsAffected = updateStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			rowsAffected = 0;
		}
		return rowsAffected;
	}
	
	public int updateAllCartItemSelection(Connection connection, String cartID, int newSelection) {
		int rowsAffected = 0;
		try {
			String updateQuery = "UPDATE cart_items SET selected=? WHERE cartID=?;";
			PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
			updateStatement.setInt(1, newSelection);
			updateStatement.setString(2, cartID);
			rowsAffected = updateStatement.executeUpdate();
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			rowsAffected = 0;
		}
		return rowsAffected;
	}
	
	public int updateCartItemQuantity(Connection connection, String cartID, String bookID, int updatedQuantity) {
	    int rowsAffected = 0;
	    try {
	        String updateQuery = "UPDATE cart_items SET Qty=? WHERE cartID=? AND BookID=?;";
	        PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
	        updateStatement.setInt(1, updatedQuantity);
	        updateStatement.setString(2, cartID);
	        updateStatement.setString(3, bookID);
	        rowsAffected = updateStatement.executeUpdate();
	    } catch (SQLException e) {
	        System.err.println("Error: " + e.getMessage());
	        rowsAffected = 0;
	    }
	    return rowsAffected;
	}

	public int createCartForUser(Connection connection, String customerID) throws SQLException {
		String insertCartSqlStr = "INSERT INTO cart (cartID, custID) VALUES (?, ?);";
		try (PreparedStatement insertCartPS = connection.prepareStatement(insertCartSqlStr)) {
			insertCartPS.setString(1, UUID.randomUUID().toString());
			insertCartPS.setString(2, customerID);
			int affectedCartRows = insertCartPS.executeUpdate();
			return affectedCartRows;
		}
	}
}
