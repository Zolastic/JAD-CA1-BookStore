package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.Book;
import model.TransactionHistory;
import model.TransactionHistoryItemBook;
import model.TransactionHistoryWithItems;

public class TransactionHistoryDAO {

	public List<TransactionHistory> getUserTransactionHistories(Connection connection, String userID) throws SQLException {
		String sqlStr = "SELECT * FROM transaction_history WHERE custID = ?;";
		ArrayList<TransactionHistory> TransactionHistories = new ArrayList<>();

		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, userID);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				TransactionHistory transactionHistory = new TransactionHistory();
				transactionHistory.setTransactionHistoryID(resultSet.getString("transaction_historyID"));
				transactionHistory.setTransactionDate(resultSet.getString("transactionDate"));
				transactionHistory.setTotalAmount(resultSet.getDouble("totalAmount"));
				transactionHistory.setAddressID(resultSet.getString("addr_id"));
				transactionHistory.setPaymentInpaymentIntentID(resultSet.getString("paymentIntentId"));
				TransactionHistories.add(transactionHistory);
			}
			resultSet.close();
			return TransactionHistories;
		}
	}
	
	public TransactionHistory getTransactionHistoryByID(Connection connection, String transactionHistoryID) throws SQLException {
		String sqlStr = "SELECT * FROM transaction_history WHERE transaction_historyID = ?;\r\n";
		
		TransactionHistory transactionHistory = new TransactionHistory();
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, transactionHistoryID);
			
			ResultSet resultSet = ps.executeQuery();
			
			if (resultSet.next()) {
				transactionHistory.setTransactionHistoryID(transactionHistoryID);
				transactionHistory.setTransactionDate(resultSet.getString("transactionDate"));
				transactionHistory.setTotalAmount(resultSet.getDouble("totalAmount"));
				transactionHistory.setAddressID(resultSet.getString("addr_id"));
				transactionHistory.setPaymentInpaymentIntentID(resultSet.getString("paymentIntentId"));
				transactionHistory.setGstPercentage(resultSet.getDouble("gstPercent"));
				transactionHistory.setFullAddress(resultSet.getString("fullAddr"));
			}
		}
		
		return transactionHistory;
	}
	
	public int updateTransactionHistoryAddress(Connection connection, String fullAddress, String transactionHistoryID) throws SQLException {
		
		String sqlStr = "UPDATE transaction_history SET fullAddr = ? WHERE transaction_historyID = ?;";
		
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, fullAddress);
			ps.setString(2, transactionHistoryID);
			
			int affectedRows = ps.executeUpdate();
			
			return affectedRows > 0 ? 200 : 500;
		} 
	}

	// To get all transaction history of the user
	public List<TransactionHistoryWithItems> getTransactionHistoriesOfUser(Connection connection, String userID) {
		List<TransactionHistoryWithItems> transactionHistories = new ArrayList<>();
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
				TransactionHistoryWithItems transactionHistory = null;
				for (TransactionHistoryWithItems history : transactionHistories) {
					if (history.getTransactionHistoryID().equals(transactionHistoryID)) {
						transactionHistory = history;
						break;
					}
				}
				if (transactionHistory == null) {
					transactionHistory = new TransactionHistoryWithItems(transactionHistoryID,
							resultSet.getString("transaction_history.transactionDate"),
							resultSet.getDouble("transaction_history.totalAmount"),
							resultSet.getString("transaction_history.custID"), new ArrayList<>(),
							resultSet.getString("transaction_history.addr_id"),
							resultSet.getString("transaction_history.paymentIntentId"),
							resultSet.getDouble("transaction_history.gstPercent"),
							resultSet.getString("transaction_history.fullAddr"));
					transactionHistories.add(transactionHistory);
				}
				TransactionHistoryItemBook transactionHistoryItem = new TransactionHistoryItemBook(
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
				List<TransactionHistoryItemBook> transactionHistoryItems = transactionHistory
						.getTransactionHistoryItems();
				transactionHistoryItems.add(transactionHistoryItem);
				transactionHistory.setTransactionHistoryItems(transactionHistoryItems);
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return transactionHistories;
	}
}
