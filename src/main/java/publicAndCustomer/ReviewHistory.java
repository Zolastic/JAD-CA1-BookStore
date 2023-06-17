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
import dao.ReviewDAO;
import dao.VerifyUserDAO;
/**
 * Servlet implementation class ReviewHistory
 */

/**
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA1
 */

@WebServlet("/ReviewHistory")
public class ReviewHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	private ReviewDAO reviewDAO = new ReviewDAO();
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
			userID = verifyUserDAO.validateUserID(connection, userID);
			if (userID == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
				return;
			} else {
				reviewHistories = reviewDAO.getReviewHistories(connection, userID);
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
				int rowsAffectedDelete = reviewDAO.deleteReview(connection, review_id);
				if (rowsAffectedDelete != 1) {
					throw new Exception("Delete Error!");
				} else {
					int countUpdate = reviewDAO.updateReviewState(connection, transaction_history_itemID, 0);
					if (countUpdate == 1) {
						String referer = request.getHeader("Referer");
						response.sendRedirect(
								referer+"&scrollPosition=" + scrollPosition);
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
