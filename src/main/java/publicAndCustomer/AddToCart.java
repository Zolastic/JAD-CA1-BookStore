package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CartDAO;
import dao.VerifyUserDAO;
import utils.DBConnection;

/**
 * Servlet implementation class AddToCart
 */
@WebServlet("/AddToCart")
public class AddToCart extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private CartDAO cartDAO = new CartDAO();
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
					String userID = (String) request.getSession().getAttribute("userID");
					if(userID.equals(validatedUserID)) {
						userID = verifyUserDAO.validateUserID(connection, userID);
					}else {
						RequestDispatcher dispatcher = request.getRequestDispatcher("/publicAndCustomer/registrationPage.jsp");
						dispatcher.forward(request, response);
						return;
					}
					if (userID == null) {
						RequestDispatcher dispatcher = request.getRequestDispatcher("/publicAndCustomer/registrationPage.jsp");
						dispatcher.forward(request, response);
						return;
					}
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

}
