package publicAndCustomer;

import java.io.IOException;
import java.sql.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import model.Book;
import model.TransactionHistory;
import model.TransactionHistoryItem;

import utils.DBConnection;

/**
 * Servlet implementation class transactionHistory
 */
@WebServlet("/transactionHistory")
public class transactionHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public transactionHistory() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userIDAvailable = request.getParameter("userIDAvailable");
		String scrollPosition = request.getParameter("scrollPosition");
		List<TransactionHistory> transactionHistories = new ArrayList<>();
		String userID = null;
		if (userIDAvailable != null && userIDAvailable.equals("true")) {
			userID = (String) request.getSession().getAttribute("userID");
		}
		try (Connection connection = DBConnection.getConnection()) {
			userID = validateUserID(connection, userID);
			if (userID == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
				return;
			}
			String query = "SELECT transaction_history.*, transaction_history_items.*, \r\n"
					+ "book.*, genre.genre_name, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName,\r\n"
					+ "publisher.publisherName\r\n" + "FROM transaction_history \r\n"
					+ "JOIN transaction_history_items ON transaction_history.transaction_historyID = transaction_history_items.transaction_historyID \r\n"
					+ "JOIN book ON transaction_history_items.bookID = book.book_id\r\n"
					+ "JOIN genre ON genre.genre_id = book.genre_id \r\n"
					+ "LEFT JOIN review ON review.bookID = book.book_id\r\n"
					+ "JOIN author ON book.authorID = author.authorID \r\n"
					+ "JOIN publisher ON book.publisherID = publisher.publisherID \r\n"
					+ "WHERE transaction_history.custID = ? GROUP BY \r\n"
					+ "transaction_history.transaction_historyID, \r\n"
					+ "transaction_history_items.transaction_history_itemID, \r\n" + "book.book_id";

			PreparedStatement statement = connection.prepareStatement(query);
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
							resultSet.getDouble("transaction_history.subtotal"),
							resultSet.getString("transaction_history.custID"),
							resultSet.getString("transaction_history.address"), new ArrayList<>());
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

			connection.close();
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}

		request.setAttribute("transactionHistories", transactionHistories);
		request.setAttribute("validatedUserID", userID);
		String dispatcherURL="publicAndCustomer/transactionHistory.jsp";
		if(scrollPosition!=null) {
			dispatcherURL+="?scrollPosition="+scrollPosition;
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(dispatcherURL);
		dispatcher.forward(request, response);
	}
	
	private String validateUserID(Connection connection, String userID) throws SQLException {
	    if (userID != null) {
	        String sqlStr = "SELECT COUNT(*) FROM users WHERE users.userID=?";
	        PreparedStatement ps = connection.prepareStatement(sqlStr);
	        ps.setString(1, userID);
	        ResultSet rs = ps.executeQuery();
	        rs.next();
	        int rowCount = rs.getInt(1);
	        if (rowCount < 1) {
	            userID = null;
	        }
	    }
	    return userID;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
