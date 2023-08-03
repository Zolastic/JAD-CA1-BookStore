package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CartDAO;
import utils.DBConnection;

/**
 * Servlet implementation class SelectCartItems
 */
@WebServlet("/SelectCartItem")
public class SelectCartItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CartDAO cartDAO = new CartDAO();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String bookID = request.getParameter("bookID");
		String cartID = request.getParameter("cartID");
		String scrollPosition = request.getParameter("scrollPositionForSelect");
		String newSelection = request.getParameter("newSelection");
		if (bookID == null || cartID == null || newSelection == null || scrollPosition == null) {
			String referer = request.getHeader("Referer");
			referer = removeParameterFromUrl(referer);
			response.sendRedirect(referer + "?scrollPosition=" + scrollPosition + "&error=true");
		} else {
			try (Connection connection = DBConnection.getConnection()) {
				int newSelectionInt = Integer.parseInt(newSelection);
				int rowsAffected = cartDAO.updateCartItemSelection(connection, cartID, bookID, newSelectionInt);
				if (rowsAffected == 1) {
					String referer = request.getHeader("Referer");
					referer = removeParameterFromUrl(referer);
					response.sendRedirect(referer + "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition);
				} else {
					String referer = request.getHeader("Referer");
					referer = removeParameterFromUrl(referer);
					response.sendRedirect(
							referer + "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition + "&error=true");
				}
			} catch (SQLException e) {
				System.err.println("Error: " + e);
				String referer = request.getHeader("Referer");
				referer = removeParameterFromUrl(referer);
				response.sendRedirect(
						referer + "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition + "&error=true");
			}
		}
	}
	
	// To remove parameters from url
	private String removeParameterFromUrl(String url) {
		int questionMarkIndex = url.indexOf("?");
		if (questionMarkIndex >= 0) {
			String baseUrl = url.substring(0, questionMarkIndex);
			return baseUrl;
		}

		return url;
	}


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
