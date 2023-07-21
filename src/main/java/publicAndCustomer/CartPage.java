package publicAndCustomer;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

import model.Book;
import utils.DBConnection;
import dao.CartDAO;
import dao.VerifyUserDAO;

/**
 * Servlet implementation class CartPage
 */

/**
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA1
 */

@WebServlet("/CartPage")
public class CartPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	private CartDAO cartDAO = new CartDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String scrollPosition = request.getParameter("scrollPosition");
			String userIDAvailable = request.getParameter("userIDAvailable");

			String userID = null;
			if (userIDAvailable != null) {
				if (userIDAvailable.equals("true")) {
					userID = (String) request.getSession().getAttribute("userID");
				}
			}
			String cartID = null;
			List<Book> cartItems = new ArrayList<>();

			userID = verifyUserDAO.validateUserID(connection, userID);
			// if userID==null sent user to login
			if (userID == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
				return;
			}
			// get cart id
			cartID = cartDAO.getCartID(connection, userID);
			// get cart items
			cartItems = cartDAO.getCartItems(connection, userID);
			connection.close();
			request.setAttribute("scrollPosition", scrollPosition);
			request.setAttribute("cartID", cartID);
			request.setAttribute("cartItems", cartItems);
			request.setAttribute("validatedUserID", userID);
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/cartPage.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			System.err.println("Error: " + e);
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/login.jsp");
			dispatcher.forward(request, response);
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

	// handle select or deselect of cart items
	protected void selectCartItem(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

	// Handle select or deselect all cart items
	protected void selectAllCartItems(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String scrollPosition = request.getParameter("scrollPositionForSelectAll");
		String newSelection = request.getParameter("newSelection");
		String cartID = request.getParameter("cartID");
		if (cartID == null || newSelection == null || scrollPosition == null) {
			String referer = request.getHeader("Referer");
			referer = removeParameterFromUrl(referer);
			response.sendRedirect(referer + "?scrollPosition=" + scrollPosition + "&error=true");
		} else {
			try (Connection connection = DBConnection.getConnection()) {
				int newSelectionInt = Integer.parseInt(newSelection);
				int rowsAffected = cartDAO.updateAllCartItemSelection(connection, cartID, newSelectionInt);
				if (rowsAffected > 0) {
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

	// Handle delete cart items
	protected void deleteCartItemAction(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String scrollPosition = request.getParameter("scrollPositionForDelete");
		String bookID = request.getParameter("bookID");
		String cartID = request.getParameter("cartID");
		if (cartID == null || bookID == null || scrollPosition == null) {
			String referer = request.getHeader("Referer");
			referer = removeParameterFromUrl(referer);
			response.sendRedirect(
					referer + "?userIDAvailable=true" + "&scrollPosition=" + scrollPosition + "&error=true");
		} else {
			try (Connection connection = DBConnection.getConnection()) {
				boolean deleteSuccess = cartDAO.deleteCartItem(cartID, bookID);
				if (deleteSuccess) {
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

	// Handle user checkout
	protected void checkout(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

	// Handle update quantity
	protected void updateQuantity(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String scrollPosition = request.getParameter("scrollPositionForQty");
		String bookID = request.getParameter("bookID");
		String cartID = request.getParameter("cartID");
		String updatedQuantity = request.getParameter("updatedQuantity");
		if (cartID == null || bookID == null || scrollPosition == null || updatedQuantity == null) {
			String referer = request.getHeader("Referer");
			referer = removeParameterFromUrl(referer);
			response.sendRedirect(referer + "?scrollPosition=" + scrollPosition + "&error=true");
		} else {
			try (Connection connection = DBConnection.getConnection()) {
				int updatedQuantityInt = Integer.parseInt(updatedQuantity);
				int rowsAffected = cartDAO.updateCartItemQuantity(connection, cartID, bookID, updatedQuantityInt);
				if (rowsAffected > 0) {
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// check for actions
		String action = request.getParameter("action");
		if (action != null && action.equals("selectCartItem")) {
			selectCartItem(request, response);
		} else if (action != null && action.equals("selectAllCartItems")) {
			selectAllCartItems(request, response);
		} else if (action != null && action.equals("deleteCartItem")) {
			deleteCartItemAction(request, response);
		} else if (action != null && action.equals("checkout")) {
			checkout(request, response);
		} else if (action != null && action.equals("updateQuantity")) {
			updateQuantity(request, response);
		} else {
			doGet(request, response);
		}
	}

}
