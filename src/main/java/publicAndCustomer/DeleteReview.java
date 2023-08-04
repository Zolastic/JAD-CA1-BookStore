package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.ReviewDAO;
import dao.VerifyUserDAO;
import utils.DBConnection;

/**
 * Servlet implementation class DeleteReview
 */
@WebServlet("/DeleteReview")
public class DeleteReview extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ReviewDAO reviewDAO = new ReviewDAO();
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String review_id = request.getParameter("review_id");
		String userID = (String) request.getSession().getAttribute("userID");
		String scrollPosition = request.getParameter("scrollPositionForDelete");
		String transaction_history_itemID = request.getParameter("transaction_history_itemID");
		if (review_id == null || scrollPosition == null || transaction_history_itemID == null) {
			String referer = request.getHeader("Referer");
			response.sendRedirect(referer + "&scrollPosition=" + scrollPosition + "&delete=false");
		} else {
			try (Connection connection = DBConnection.getConnection()) {
				userID = verifyUserDAO.validateUserID(connection, userID);
				if (userID == null) {
					RequestDispatcher dispatcher = request.getRequestDispatcher("/publicAndCustomer/registrationPage.jsp");
					dispatcher.forward(request, response);
					return;
				}
				int rowsAffectedDelete = reviewDAO.deleteReview(connection, review_id);
				if (rowsAffectedDelete != 1) {
					throw new Exception("Delete Error!");
				} else {
					int countUpdate = reviewDAO.updateReviewState(connection, transaction_history_itemID, 0);
					if (countUpdate == 1) {
						String referer = request.getHeader("Referer");
						response.sendRedirect(referer + "&scrollPosition=" + scrollPosition);
					} else {
						throw new Exception("Update Error!");
					}
				}
			} catch (Exception e) {
				System.err.println("Error: " + e);
				String referer = request.getHeader("Referer");
				response.sendRedirect(referer + "&scrollPosition=" + scrollPosition + "&delete=false");
			}
		}
	}

}
