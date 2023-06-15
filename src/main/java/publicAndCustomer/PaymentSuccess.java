package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.DBConnection;

/**
 * Servlet implementation class paymentSuccess
 */

/**
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA1
 */

@WebServlet("/PaymentSuccess")
public class PaymentSuccess extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PaymentSuccess() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			// To better confirm user comes from appropriate link before checking, and in
			// the previous page userID is available
			String userIDAvailable = request.getParameter("userIDAvailable");
			String userID = null;
			if (userIDAvailable != null) {
				if (userIDAvailable.equals("true")) {
					userID = (String) request.getSession().getAttribute("userID");
				}
			}
			// Validate userID
			userID = validateUserID(connection, userID);
			if (userID == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
				return;
			}
			request.setAttribute("validatedUserID", userID);
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/paymentSuccess.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/paymentSuccess.jsp");
			dispatcher.forward(request, response);
			System.err.println("Error: \" + e);\r\n");
		}

	}

	// Function to validate user id
	private String validateUserID(Connection connection, String userID) throws SQLException {
		if (userID != null) {
			String sqlStr = "SELECT COUNT(*) FROM users WHERE users.userID=?";
			PreparedStatement ps = connection.prepareStatement(sqlStr);
			ps.setString(1, userID);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int rowCount = rs.getInt(1);
			if (rowCount < 1) {
				userID = null;
			}
		}
		return userID;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
