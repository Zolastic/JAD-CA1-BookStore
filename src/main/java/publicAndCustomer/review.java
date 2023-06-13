package publicAndCustomer;

import java.io.IOException;
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

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Book;
import utils.DBConnection;

/**
 * Servlet implementation class review
 */
@WebServlet("/review")
public class review extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public review() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bookID = request.getParameter("bookID");
		String scrollPosition = request.getParameter("scrollPosition");
		String userID = request.getParameter("custID");
		String transactionHistoryItemID = request.getParameter("transactionHistoryItemID");

		Book bookDetails = null;

		if (bookID != null && userID != null && scrollPosition != null || transactionHistoryItemID != null) {
			try (Connection connection = DBConnection.getConnection()) {
				userID = validateUserID(connection, userID);
				if (userID == null) {
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
					dispatcher.forward(request, response);
					return;
				}
				bookDetails = getBookDetails(connection, bookID);

				connection.close();
			} catch (SQLException e) {
				System.err.println("Error: " + e);
			}
		} else {
			if (userID != null) {
				response.sendRedirect("CA1-assignment/transactionHistory?userIDAvailable=true");
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
			}

		}
		request.setAttribute("scrollPosition", scrollPosition);
		request.setAttribute("bookDetails", bookDetails);
		request.setAttribute("validatedUserID", userID);
		request.setAttribute("transactionHistoryItemID", transactionHistoryItemID);
		RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/review.jsp");
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

	private Book getBookDetails(Connection connection, String bookID) throws SQLException {
		Book bookDetails = null;
		String simpleProc = "{call getBookDetails(?)}";
		CallableStatement cs = connection.prepareCall(simpleProc);
		cs.setString(1, bookID);
		cs.execute();
		ResultSet resultSetForBookDetails = cs.getResultSet();

		if (resultSetForBookDetails.next()) {
			bookDetails = new Book(resultSetForBookDetails.getString("book_id"),
					resultSetForBookDetails.getString("ISBN"), resultSetForBookDetails.getString("title"),
					resultSetForBookDetails.getString("authorName"), resultSetForBookDetails.getString("publisherName"),
					resultSetForBookDetails.getString("publication_date"),
					resultSetForBookDetails.getString("description"), resultSetForBookDetails.getString("genre_name"),
					resultSetForBookDetails.getString("img"), resultSetForBookDetails.getInt("sold"),
					resultSetForBookDetails.getInt("inventory"), resultSetForBookDetails.getDouble("price"), 1,
					resultSetForBookDetails.getDouble("average_rating"));
		}

		return bookDetails;
	}

	protected void submitReview(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String scrollPosition = request.getParameter("scrollPosition");
		String bookID = request.getParameter("bookID");
		String custID = request.getParameter("custID");
		String review_text = request.getParameter("review_text");
		String ratingString = request.getParameter("rating");
		double rating = Double.parseDouble(ratingString);

		String transactionHistoryItemID = request.getParameter("transactionHistoryItemID");
		if (custID == null || bookID == null || scrollPosition == null || transactionHistoryItemID == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("transactionHistory"
					+ "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition + "&success=false");
			dispatcher.forward(request, response);
		} else {
			try (Connection connection = DBConnection.getConnection()) {
				String review_id = insertReview(connection, custID, bookID, review_text, rating);

				if (review_id != null) {
					int countUpdate = updateReviewState(connection, transactionHistoryItemID);
					if (countUpdate == 1) {
						RequestDispatcher dispatcher = request.getRequestDispatcher("transactionHistory"
								+ "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition + "&success=true");
						dispatcher.forward(request, response);
					} else {
						RequestDispatcher dispatcher = request.getRequestDispatcher("transactionHistory"
								+ "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition + "&success=false");
						dispatcher.forward(request, response);
					}

				} else {
					RequestDispatcher dispatcher = request.getRequestDispatcher("transactionHistory"
							+ "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition + "&success=false");
					dispatcher.forward(request, response);
				}
			} catch (SQLException e) {
				System.err.println("Error: " + e);
				RequestDispatcher dispatcher = request.getRequestDispatcher("transactionHistory"
						+ "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition + "&success=false");
				dispatcher.forward(request, response);
			}

		}
	}

	private String insertReview(Connection connection, String custID, String bookID, String review_text, double rating)
			throws SQLException {

		String review_id = uuidGenerator();

		String sql = "INSERT INTO review (review_id, custID, bookID, review_text, rating, ratingDate) VALUES (?, ?, ?, ?, ?, ?)";
		String ratingDate = getCurrentDate();
		PreparedStatement statement = connection.prepareStatement(sql);
		
		statement.setString(1, review_id);
		statement.setString(2, custID);
		statement.setString(3, bookID);
		statement.setString(4, review_text);
		statement.setDouble(5, rating);
		statement.setString(6, ratingDate);

		int rowsAffected = statement.executeUpdate();

		statement.close();

		if (rowsAffected == 1) {
			return review_id;
		} else {
			return null;
		}
	}

	private int updateReviewState(Connection connection, String transactionHistoryItemID) throws SQLException {


		String sql = "UPDATE transaction_history_items SET reviewed=1 WHERE transaction_history_itemID=?";
		PreparedStatement statement = connection.prepareStatement(sql);

		statement.setString(1, transactionHistoryItemID);

		int rowsAffected = statement.executeUpdate();

		statement.close();
		return rowsAffected;
	}

	private String getCurrentDate() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date currentDate = new Date();
		return dateFormat.format(currentDate);
	}

	private String uuidGenerator() {
		UUID uuid = UUID.randomUUID();
		return (uuid.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action != null && action.equals("submitReview")) {
			submitReview(request, response);
		} else {
			doGet(request, response);
		}
	}

}
