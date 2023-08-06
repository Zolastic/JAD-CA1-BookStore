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
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA2
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
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// check for actions
			doGet(request, response);
	}

}
