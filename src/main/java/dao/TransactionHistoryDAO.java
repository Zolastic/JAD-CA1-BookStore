package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import model.TransactionHistory;
import model.TransactionHistoryItem;

public class TransactionHistoryDAO {
	// To get all transaction history of the user
	public List<TransactionHistory> getTransactionHistories(Connection connection, String userID) {
		List<TransactionHistory> transactionHistories = new ArrayList<>();
		String query = "SELECT transaction_history.*, transaction_history_items.*,book.*, genre.genre_name,CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating,author.authorName, publisher.publisherName\r\n"
				+ "FROM transaction_history\r\n"
				+ "JOIN transaction_history_items ON transaction_history.transaction_historyID = transaction_history_items.transaction_historyID\r\n"
				+ "JOIN book ON transaction_history_items.bookID = book.book_id\r\n"
				+ "JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "JOIN author ON book.authorID = author.authorID\r\n"
				+ "JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "WHERE transaction_history.custID = ?\r\n"
				+ "GROUP BY transaction_history.transaction_historyID, transaction_history_items.transaction_history_itemID, book.book_id\r\n"
				+ "ORDER BY transaction_history.transactionDate DESC\r\n";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, userID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String transactionHistoryID = resultSet.getString("transaction_history.transaction_historyID");
				TransactionHistory transactionHistory = null;
				for (TransactionHistory history : transactionHistories) {
					if (history.getTransactionHistoryID().equals(transactionHistoryID)) {
						transactionHistory = history;
						break;
					}
				}
				if (transactionHistory == null) {
					transactionHistory = new TransactionHistory(transactionHistoryID,
							resultSet.getString("transaction_history.transactionDate"),
							resultSet.getDouble("transaction_history.totalAmount"),
							resultSet.getString("transaction_history.custID"),
							resultSet.getString("transaction_history.addrId"), new ArrayList<>());
					transactionHistories.add(transactionHistory);
				}
				TransactionHistoryItem transactionHistoryItem = new TransactionHistoryItem(
						resultSet.getString("transaction_history_items.transaction_history_itemID"),
						resultSet.getString("transaction_history_items.bookID"),
						resultSet.getInt("transaction_history_items.qty"),
						resultSet.getInt("transaction_history_items.reviewed"));
				Book book = new Book(resultSet.getString("book.book_id"), resultSet.getString("book.ISBN"),
						resultSet.getString("book.title"), resultSet.getString("author.authorName"),
						resultSet.getString("publisher.publisherName"), resultSet.getString("book.publication_date"),
						resultSet.getString("book.description"), resultSet.getString("genre.genre_name"),
						resultSet.getString("book.img"), resultSet.getInt("book.sold"),
						resultSet.getInt("book.inventory"), resultSet.getDouble("book.price"),
						resultSet.getDouble("average_rating"));
				transactionHistoryItem.setBook(book);
				List<TransactionHistoryItem> transactionHistoryItems = transactionHistory.getTransactionHistoryItems();
				transactionHistoryItems.add(transactionHistoryItem);
				transactionHistory.setTransactionHistoryItems(transactionHistoryItems);
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return transactionHistories;
	}
}
