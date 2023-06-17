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
import dao.VerifyUserDAO;
import utils.DBConnection;

/**
 * Servlet implementation class TransactionHistoryPage
 */

/**
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA1
 */

@WebServlet("/TransactionHistoryPage")
public class TransactionHistoryPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TransactionHistoryPage() {
		super();
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
			// validate user
			userID = verifyUserDAO.validateUserID(connection, userID);
			if (userID == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
				return;
			}
			transactionHistories = getTransactionHistories(connection, userID);
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		request.setAttribute("transactionHistories", transactionHistories);
		request.setAttribute("validatedUserID", userID);
		String dispatcherURL = "publicAndCustomer/transactionHistory.jsp";
		if (scrollPosition != null) {
			dispatcherURL += "?scrollPosition=" + scrollPosition;
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(dispatcherURL);
		dispatcher.forward(request, response);
	}

	// To get all transaction history of the user
	private List<TransactionHistory> getTransactionHistories(Connection connection, String userID) {
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
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return transactionHistories;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
