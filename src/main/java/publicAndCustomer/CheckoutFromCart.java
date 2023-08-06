package publicAndCustomer;

import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.VerifyUserDAO;
import utils.DBConnection;

/**
 * Servlet implementation class CheckoutFromCart
 */
@WebServlet("/CheckoutFromCart")
public class CheckoutFromCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
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

	private String removeParameterFromUrl(String url) {
		int questionMarkIndex = url.indexOf("?");
		if (questionMarkIndex >= 0) {
			String baseUrl = url.substring(0, questionMarkIndex);
			return baseUrl;
		}

		return url;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userID = (String) request.getSession().getAttribute("userID");
		try (Connection connection = DBConnection.getConnection()) {
			userID = verifyUserDAO.validateUserID(connection, userID);
			connection.close();
		} catch (SQLException e) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/publicAndCustomer/registrationPage.jsp");
			dispatcher.forward(request, response);
			return;
		}
		if (userID == null) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("/publicAndCustomer/registrationPage.jsp");
			dispatcher.forward(request, response);
			return;
		}
		String checkoutItems = request.getParameter("selectedCartItems");
		if (checkoutItems == null) {
			String referer = request.getHeader("Referer");
			referer = removeParameterFromUrl(referer);
			response.sendRedirect(referer + "?error=true");
		} else {
			String encodedCheckoutItems = URLEncoder.encode(checkoutItems, "UTF-8");
			Cookie checkoutItemsCookie = new Cookie("checkoutItems", encodedCheckoutItems);
			checkoutItemsCookie.setMaxAge(5 * 60 * 60);
			response.addCookie(checkoutItemsCookie);
			response.sendRedirect("CheckoutPage?userIDAvailable=true");
		}
	}

}
