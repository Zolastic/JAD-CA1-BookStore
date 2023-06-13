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
 * Author(s): Soh Jian Min (P2238856)
 * Description: JAD CA1
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

		List<ReviewHistoryClass> reviewHistory = new ArrayList<>();
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
			} else {
				reviewHistory = getReviewHistory(connection, userID);
			}

		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		request.setAttribute("reviewHistory", reviewHistory);
		request.setAttribute("validatedUserID", userID);
		String dispatcherURL = "publicAndCustomer/reviewHistory.jsp";
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

	private List<ReviewHistoryClass> getReviewHistory(Connection connection, String custID) throws SQLException {
		List<ReviewHistoryClass> reviewHistory = new ArrayList<>();

		String query = "SELECT review.*, book.*, genre.genre_name, author.authorName, publisher.publisherName, "
				+ "(SELECT CAST(AVG(IFNULL(rating, 0)) AS DECIMAL(2, 1)) FROM review WHERE bookID = book.book_id) AS average_rating " 
				+ "FROM review "
				+ "JOIN book ON review.bookID = book.book_id " + "JOIN genre ON genre.genre_id = book.genre_id "
				+ "JOIN author ON book.authorID = author.authorID "
				+ "JOIN publisher ON book.publisherID = publisher.publisherID " + "WHERE review.custID = ? "
				+ "ORDER BY review.ratingDate DESC";

		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, custID);

		ResultSet resultSet = statement.executeQuery();

		while (resultSet.next()) {
			String reviewID = resultSet.getString("review_id");
			String bookID = resultSet.getString("book_id");
			String reviewText = resultSet.getString("review_text");
			double rating = resultSet.getDouble("rating");
			String ratingDate = resultSet.getString("ratingDate");

			Book book = new Book(resultSet.getString("book_id"), resultSet.getString("ISBN"),
					resultSet.getString("title"), resultSet.getString("authorName"),
					resultSet.getString("publisherName"), resultSet.getString("publication_date"),
					resultSet.getString("description"), resultSet.getString("genre_name"), resultSet.getString("img"),
					resultSet.getInt("sold"), resultSet.getInt("inventory"), resultSet.getDouble("price"),
					resultSet.getDouble("average_rating"));

			ReviewHistoryClass review = new ReviewHistoryClass(book, reviewID, custID, bookID, reviewText, rating, ratingDate);
			reviewHistory.add(review);
		}

		return reviewHistory;
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
