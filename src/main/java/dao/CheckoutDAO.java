package dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import model.Book;

public class CheckoutDAO {
	// Get all the checkout items details
	public List<Book> getCheckoutItems(Connection connection, String userID,
			List<Map<String, Object>> checkoutItemsList) {
		List<Book> checkoutItems = new ArrayList<>();

		for (Map<String, Object> itemMap : checkoutItemsList) {
			String bookID = (String) itemMap.get("bookID");
			int quantity = (int) itemMap.get("quantity");

			String simpleProc = "{call getBookDetails(?)}";
			try (CallableStatement cs = connection.prepareCall(simpleProc)) {
				cs.setString(1, bookID);
				cs.execute();
				try (ResultSet resultSetForBookDetails = cs.getResultSet()) {
					if (resultSetForBookDetails.next()) {
						Book book = new Book(resultSetForBookDetails.getString("book_id"),
								resultSetForBookDetails.getString("ISBN"), resultSetForBookDetails.getString("title"),
								resultSetForBookDetails.getString("authorName"),
								resultSetForBookDetails.getString("publisherName"),
								resultSetForBookDetails.getString("publication_date"),
								resultSetForBookDetails.getString("description"),
								resultSetForBookDetails.getString("genre_name"),
								resultSetForBookDetails.getString("img"), resultSetForBookDetails.getInt("sold"),
								resultSetForBookDetails.getInt("inventory"), resultSetForBookDetails.getDouble("price"),
								quantity, resultSetForBookDetails.getDouble("average_rating"));
						checkoutItems.add(book);
					}
				}
			} catch (SQLException e) {
				System.err.println("Error: " + e.getMessage());
			}
		}

		return checkoutItems;
	}

	// insert checkout items into DB transaction history after payment success
	public String insertTransactionHistory(Connection connection, double totalAmount, String custID, String addr_id, String paymentIntentId, double gstPercent, String fullAddr){
		String transactionHistoryUUID = uuidGenerator();

		String sql = "INSERT INTO transaction_history (transaction_historyID, transactionDate, totalAmount, custID, addr_id, paymentIntentId, gstPercent, fullAddr) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		String transactionDate = getCurrentDateTime();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, transactionHistoryUUID);
			statement.setString(2, transactionDate);
			statement.setDouble(3, totalAmount);
			statement.setString(4, custID);
			statement.setString(5, addr_id);
			statement.setString(6, paymentIntentId);
			statement.setDouble(7, gstPercent);
			statement.setString(8, fullAddr);

			int rowsAffected = statement.executeUpdate();

			statement.close();

			if (rowsAffected == 1) {
				return transactionHistoryUUID;
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	}

	// Function to generate an uuid
	private String uuidGenerator() {
		UUID uuid = UUID.randomUUID();
		return (uuid.toString());
	}

	// Function to get DATETIME
	private String getCurrentDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDate = new Date();
		return dateFormat.format(currentDate);
	}

	// insert checkout items into DB transaction history items after payment success
	public Boolean insertTransactionHistoryItems(Connection connection, List<Book> checkoutItems,
			String transactionHistoryUUID) {
		Boolean success = true;
		String sql = "INSERT INTO transaction_history_items (transaction_historyID, transaction_history_itemID, bookID, Qty) VALUES (?, ?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			for (Book book : checkoutItems) {
				String transactionHistoryItemUUID = uuidGenerator();
				statement.setString(1, transactionHistoryUUID);
				statement.setString(2, transactionHistoryItemUUID);
				statement.setString(3, book.getBookID());
				statement.setInt(4, book.getQuantity());
				int rowsAffected = statement.executeUpdate();
				if (rowsAffected != 1) {
					success = false;
					break;
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			success = false;
		}

		return success;
	}

	// Delete the cart items that is already purchased after success payment
	public int deleteFromCart(Connection connection, List<Book> checkoutItems, String custID) {
		int count = 0;
		String cartID = getCartID(connection, custID);
		if (cartID != null) {
			for (Book book : checkoutItems) {
				String deleteQuery = "DELETE FROM cart_items WHERE cartID=? AND BookID=?";
				try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
					deleteStatement.setString(1, cartID);
					deleteStatement.setString(2, book.getBookID());
					int rowsDeleted = deleteStatement.executeUpdate();
					count += rowsDeleted;
				} catch (SQLException e) {
					System.err.println("Error: " + e.getMessage());
				}
			}
		}
		return count;
	}

	// Update Book's inventory and sold
	public int updateBooks(Connection connection, List<Book> checkoutItems) {
		int count = 0;
		for (Book book : checkoutItems) {
			String updateQuery = "UPDATE book SET inventory = (inventory - ?), sold = (sold + ?) WHERE book_id=?";
			try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
				updateStatement.setInt(1, book.getQuantity());
				updateStatement.setInt(2, book.getQuantity());
				updateStatement.setString(3, book.getBookID());
				int rowsUpdated = updateStatement.executeUpdate();
				count += rowsUpdated;
			} catch (SQLException e) {
				System.err.println("Error: " + e.getMessage());
			}
		}
		return count;
	}

	// Get cart id with custID
	public String getCartID(Connection connection, String custID) {
		String cartID = null;
		String selectQuery = "SELECT cartID FROM cart WHERE custID=?";
		try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
			selectStatement.setString(1, custID);
			ResultSet resultSet = selectStatement.executeQuery();
			if (resultSet.next()) {
				cartID = resultSet.getString("cartID");
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return cartID;
	}
}
