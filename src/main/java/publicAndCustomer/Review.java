package publicAndCustomer;

import java.io.IOException;
import dao.BookDAO;
import dao.ReviewDAO;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Book;
import utils.DBConnection;
import dao.VerifyUserDAO;

/**
 * Servlet implementation class Review
 */

/**
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA1
 */

@WebServlet("/Review")
public class Review extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	private BookDAO bookDAO = new BookDAO();
	private ReviewDAO reviewDAO = new ReviewDAO();

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
				// Validate userID
				userID = verifyUserDAO.validateUserID(connection, userID);
				if (userID == null) {
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
					dispatcher.forward(request, response);
					return;
				}
				bookDetails = bookDAO.getBookDetailsForBybookID(connection, bookID);
				connection.close();
			} catch (SQLException e) {
				System.err.println("Error: " + e);
			}
		} else {
			if (userID != null) {
				response.sendRedirect("CA1-assignment/TransactionHistoryPage?userIDAvailable=true");
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

	// Function to do all logic to submit review
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
			RequestDispatcher dispatcher = request.getRequestDispatcher("TransactionHistoryPage"
					+ "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition + "&success=false");
			dispatcher.forward(request, response);
		} else {
			try (Connection connection = DBConnection.getConnection()) {
				String review_id = reviewDAO.insertReview(connection, custID, bookID, review_text, rating, transactionHistoryItemID);
				if (review_id != null) {
					int countUpdate = reviewDAO.updateReviewState(connection, transactionHistoryItemID,1);
					if (countUpdate == 1) {
						RequestDispatcher dispatcher = request.getRequestDispatcher("TransactionHistoryPage"
								+ "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition + "&success=true");
						dispatcher.forward(request, response);
					} else {
						RequestDispatcher dispatcher = request.getRequestDispatcher("TransactionHistoryPage"
								+ "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition + "&success=false");
						dispatcher.forward(request, response);
					}
				} else {
					RequestDispatcher dispatcher = request.getRequestDispatcher("TransactionHistoryPage"
							+ "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition + "&success=false");
					dispatcher.forward(request, response);
				}
			} catch (SQLException e) {
				System.err.println("Error: " + e);
				RequestDispatcher dispatcher = request.getRequestDispatcher("TransactionHistoryPage"
						+ "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition + "&success=false");
				dispatcher.forward(request, response);
			}
		}
	}

	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Check for action
		String action = request.getParameter("action");
		if (action != null && action.equals("submitReview")) {
			submitReview(request, response);
		} else {
			doGet(request, response);
		}
	}

}
