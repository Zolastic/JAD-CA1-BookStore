package publicAndCustomer;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Book;
import model.ReviewHistoryClass;
import utils.DBConnection;

/**
 * Servlet implementation class ReviewHistory
 */

/**
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA1
 */

@WebServlet("/ReviewHistory")
public class ReviewHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ReviewHistory() {
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
		List<ReviewHistoryClass> reviewHistories = new ArrayList<>();
		String userID = null;
		if (userIDAvailable != null && userIDAvailable.equals("true")) {
			userID = (String) request.getSession().getAttribute("userID");
		}
		try (Connection connection = DBConnection.getConnection()) {
			// To validate the user
			userID = validateUserID(connection, userID);
			if (userID == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
				return;
			} else {
				reviewHistories = getReviewHistories(connection, userID);
			}

		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		request.setAttribute("scrollPosition", scrollPosition);
		request.setAttribute("reviewHistories", reviewHistories);
		request.setAttribute("validatedUserID", userID);
		String dispatcherURL = "publicAndCustomer/reviewHistory.jsp";
		RequestDispatcher dispatcher = request.getRequestDispatcher(dispatcherURL);
		dispatcher.forward(request, response);
	}

	// Function to validate user id
	private String validateUserID(Connection connection, String userID) {
		if (userID != null) {
			String sqlStr = "SELECT COUNT(*) FROM users WHERE users.userID=?";
			try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
				ps.setString(1, userID);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						int rowCount = rs.getInt(1);
						if (rowCount < 1) {
							userID = null;
						}
					}
				}
			} catch (SQLException e) {
				userID = null;
				System.err.println("Error: " + e.getMessage());
			}
		}
		return userID;
	}

	// Get all review history of the user
	private List<ReviewHistoryClass> getReviewHistories(Connection connection, String custID) {
		List<ReviewHistoryClass> reviewHistories = new ArrayList<>();
		String query = "SELECT review.*, book.*, genre.genre_name, author.authorName, publisher.publisherName, "
				+ "(SELECT CAST(AVG(IFNULL(rating, 0)) AS DECIMAL(2, 1)) FROM review WHERE bookID = book.book_id) AS average_rating "
				+ "FROM review " + "JOIN book ON review.bookID = book.book_id "
				+ "JOIN genre ON genre.genre_id = book.genre_id " + "JOIN author ON book.authorID = author.authorID "
				+ "JOIN publisher ON book.publisherID = publisher.publisherID " + "WHERE review.custID = ? "
				+ "ORDER BY review.ratingDate DESC";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, custID);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				String reviewID = resultSet.getString("review_id");
				String bookID = resultSet.getString("book_id");
				String reviewText = resultSet.getString("review_text");
				double rating = resultSet.getDouble("rating");
				String ratingDate = resultSet.getString("ratingDate");
				String transaction_history_itemID = resultSet.getString("transaction_history_itemID");
				Book book = new Book(resultSet.getString("book_id"), resultSet.getString("ISBN"),
						resultSet.getString("title"), resultSet.getString("authorName"),
						resultSet.getString("publisherName"), resultSet.getString("publication_date"),
						resultSet.getString("description"), resultSet.getString("genre_name"),
						resultSet.getString("img"), resultSet.getInt("sold"), resultSet.getInt("inventory"),
						resultSet.getDouble("price"), resultSet.getDouble("average_rating"));

				ReviewHistoryClass review = new ReviewHistoryClass(book, reviewID, custID, bookID, reviewText, rating,
						ratingDate, transaction_history_itemID);
				reviewHistories.add(review);
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return reviewHistories;
	}

	protected void deleteReviewAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String review_id = request.getParameter("review_id");
		String scrollPosition = request.getParameter("scrollPositionForDelete");
		String transaction_history_itemID = request.getParameter("transaction_history_itemID");
		if (review_id == null || scrollPosition == null || transaction_history_itemID == null) {
			String referer = request.getHeader("Referer");
			response.sendRedirect(
					 referer+"&scrollPosition=" + scrollPosition + "&delete=false");
		} else {
			try (Connection connection = DBConnection.getConnection()) {
				int rowsAffectedDelete = deleteReview(connection, review_id);
				if (rowsAffectedDelete != 1) {
					throw new Exception("Delete Error!");
				} else {
					int countUpdate = updateReviewState(connection, transaction_history_itemID);
					if (countUpdate == 1) {
						String referer = request.getHeader("Referer");
						response.sendRedirect(
								referer+"&scrollPosition=" + scrollPosition + "&delete=true");
					} else {
						throw new Exception("Update Error!");
					}
				}
			} catch (Exception e) {
				System.err.println("Error: " + e);
				String referer = request.getHeader("Referer");
				response.sendRedirect(
						referer+"&scrollPosition=" + scrollPosition + "&delete=false");
			}
		}
	}

	// Delete the inserted review
	private int deleteReview(Connection connection, String review_id) {
		String sql = "DELETE FROM review WHERE review_id=?;";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, review_id);
			int rowsAffected = statement.executeUpdate();
			statement.close();
			return rowsAffected;
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return 0;
		}
	}

	// Function to update user's review state for the transaction history item
	private int updateReviewState(Connection connection, String transactionHistoryItemID) {
		String sql = "UPDATE transaction_history_items SET reviewed=0 WHERE transaction_history_itemID=?";
		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			statement.setString(1, transactionHistoryItemID);
			int rowsAffected = statement.executeUpdate();
			return rowsAffected;
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return 0;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action != null && action.equals("deleteReview")) {
			deleteReviewAction(request, response);
		} else {
			doGet(request, response);
		}
	}

}
