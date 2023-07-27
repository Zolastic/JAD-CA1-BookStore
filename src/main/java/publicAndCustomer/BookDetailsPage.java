package publicAndCustomer;

/**
 * Servlet implementation class BookDetailsPage
 */

/**
 * Author(s): Soh Jian Min (P2238856)
 * Description: JAD CA1
 */

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Book;
import dao.VerifyUserDAO;
import dao.BookDAO;
import dao.ReviewDAO;
import dao.CartDAO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.DBConnection;

@WebServlet("/BookDetailsPage")
public class BookDetailsPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	private BookDAO bookDAO = new BookDAO();
	private ReviewDAO reviewDAO = new ReviewDAO();
	private CartDAO cartDAO = new CartDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bookID = request.getParameter("bookID");
		String userID = null;
		userID = (String) request.getSession().getAttribute("userID");
		Book bookDetails = null;
		List<Map<String, Object>> reviews = new ArrayList<>();
		if (bookID != null) {
			try (Connection connection = DBConnection.getConnection()) {
				// Validate the userID
				userID = verifyUserDAO.validateUserID(connection, userID);
				// Get the book details
				bookDetails = bookDAO.getBookDetailsForBybookID(connection, bookID);
				// Get the reviews of the book
				reviews = reviewDAO.getBookReviews(connection, bookID);
				connection.close();
			} catch (SQLException e) {
				System.err.println("Error: " + e);
			}
		}
		request.setAttribute("bookDetails", bookDetails);
		request.setAttribute("reviews", reviews);
		request.setAttribute("validatedUserID", userID);
		RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/bookDetailsPage.jsp");
		dispatcher.forward(request, response);
	}

	// Function to add book to user's cart
	protected void addToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String validatedUserID = request.getParameter("validatedUserID");
		if (validatedUserID == null || validatedUserID.equals("null")) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
			dispatcher.forward(request, response);
		} else {
			String bookID = request.getParameter("bookID");
			int quantity = Integer.parseInt(request.getParameter("quantity"));
			String cartID = null;
			if (bookID == null || quantity == 0) {
				String referer = (request.getHeader("Referer") + "&addToCart=failed");
				response.sendRedirect(referer);
			} else {
				try (Connection connection = DBConnection.getConnection()) {
					cartID = cartDAO.getCartID(connection, validatedUserID);
					if(cartID==null) {
						String referer = (request.getHeader("Referer") + "&addToCart=failed");
						response.sendRedirect(referer);
					}else {
						int rowsAffected = cartDAO.addToCart(connection, cartID, bookID, quantity);
						if (rowsAffected > 0) {
							String referer = (request.getHeader("Referer") + "&addToCart=success");
							response.sendRedirect(referer);
						} else {
							String referer = (request.getHeader("Referer") + "&addToCart=failed");
							response.sendRedirect(referer);
						}
					}
					connection.close();
				} catch (SQLException e) {
					System.err.println("Error: " + e.getMessage());
					String referer = (request.getHeader("Referer") + "&addToCart=failed");
					response.sendRedirect(referer);
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Check for actions
		String action = request.getParameter("action");
		if (action != null && action.equals("addToCart")) {
			addToCart(request, response);
		} else {
			doGet(request, response);
		}
	}
}
