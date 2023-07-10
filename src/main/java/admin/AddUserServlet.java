package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.CartDAO;
import dao.UserDAO;
import dao.UserOTPDAO;
import utils.DBConnection;
import utils.OTPManagement;

/**
 * Servlet implementation class AddUser
 */
@WebServlet("/admin/AddUser")
public class AddUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAO();
	private CartDAO cartDAO = new CartDAO();
	private UserOTPDAO userOTPDAO = new UserOTPDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddUserServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String name = request.getParameter("name");
		String email = request.getParameter("email");
		String password = request.getParameter("password");

		try (Connection connection = DBConnection.getConnection()) {

			if (userDAO.checkForExistingUserEmail(connection, email)) {
				response.sendRedirect(request.getContextPath()
						+ "/admin/addUser.jsp?statusCode=409");
				return;
			}
			
			String customerID = UUID.randomUUID().toString();
			String secret = OTPManagement.generateSecret();

			if (userDAO.addUser(connection, name, email, password, customerID, secret) == 0) {
				response.sendRedirect(request.getContextPath()
						+ "/admin/addUser.jsp?statusCode=500");
				return;
			}

			

			if (cartDAO.createCartForUser(connection, customerID) == 0) {
				response.sendRedirect(request.getContextPath()
						+ "/admin/addUser.jsp?statusCode=500");
				return;
			}

			

			if (userOTPDAO.createOTPRowForUser(connection, customerID) == 0) {
				response.sendRedirect(request.getContextPath()
						+ "/admin/addUser.jsp?statusCode=500");
				return;
			}

			response.sendRedirect(request.getContextPath() + "/admin/addUser.jsp?statusCode=200");
			return;

		} catch (SQLException e) {
			e.printStackTrace();
			response.sendRedirect(request.getContextPath()
					+ "/admin/addUser.jsp?statusCode=500");
			return;
		}
	}
} 
