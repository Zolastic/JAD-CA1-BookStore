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
import model.TransactionHistoryItems;

public class TransactionHistoryItemsDAO {
	private TransactionHistoryDAO transactionHistoryDAO = new TransactionHistoryDAO();
	
	public List<TransactionHistoryItems> getUserTransactionHistoryItem(Connection connection, String userID) throws SQLException {
		String sqlStr = "SELECT * FROM transaction_history_items WHERE transaction_historyID = ?;";
		ArrayList<TransactionHistoryItems> transactionHistoriesItems = new ArrayList<>();
		
		List<TransactionHistory> transactionHistories = transactionHistoryDAO.getUserTransactionHistories(connection, userID);
		
		if (transactionHistories == null) {
			return null;
		}
		
		for (TransactionHistory transactionHistory : transactionHistories) {
			try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
				ps.setString(1, transactionHistory.getTransactionHistoryID());
				ResultSet resultSet = ps.executeQuery();

				while (resultSet.next()) {
					TransactionHistoryItems transactionHistoryItems = new TransactionHistoryItems();
					transactionHistoryItems.setTransactionHistoryItemID(resultSet.getString("transaction_history_itemID"));
					transactionHistoryItems.setTransactionHistoryID(resultSet.getString("transaction_historyID"));
					transactionHistoryItems.setBookID(resultSet.getString("bookID"));
					transactionHistoryItems.setQuantity(resultSet.getInt("Qty"));
					transactionHistoryItems.setReviewed(resultSet.getInt("reviewed"));
					transactionHistoriesItems.add(transactionHistoryItems);
				}
			}
		}
		
		return transactionHistoriesItems;
	}
	
	public List<TransactionHistoryItemBook> getTransactionHistoryItemsByTransactionHistoryID(Connection connection, String transactionHistoryID) throws SQLException {
		String sqlStr = "SELECT * FROM transaction_history_items, book \r\n"
				+ "WHERE transaction_historyID = ? AND transaction_history_items.bookID = book.book_id;";
		ArrayList<TransactionHistoryItemBook> transactionHistoriesItems = new ArrayList<>();
		
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, transactionHistoryID);
			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				Book book = new Book();
				book.setBookID(resultSet.getString("bookID"));
				book.setTitle(resultSet.getString("title"));
				
				TransactionHistoryItemBook transactionHistoryItem = new TransactionHistoryItemBook();
				transactionHistoryItem.setTransactionHistoryItemID(resultSet.getString("transaction_history_itemID"));
				transactionHistoryItem.setQuantity(resultSet.getInt("Qty"));
				transactionHistoryItem.setReviewed(resultSet.getInt("reviewed"));
				transactionHistoryItem.setBook(book);
				
				transactionHistoriesItems.add(transactionHistoryItem);
			}
		}
		
		return transactionHistoriesItems;
	}
}
