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
 * Servlet implementation class PaymentError
 */

/**
 * Author(s): Soh Jian Min (P2238856)
 * Description: JAD CA1
 */


@WebServlet("/PaymentError")
public class PaymentError extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PaymentError() {
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
			String error = request.getParameter("error");
			// To better confirm user comes from appropriate link before checking, and in the previous page userID is available
			String userIDAvailable = request.getParameter("userIDAvailable");
			String userID = null;
			if (userIDAvailable != null) {
				if (userIDAvailable.equals("true")) {
					userID = (String) request.getSession().getAttribute("userID");
				}
			}
			// validate user
			userID = validateUserID(connection, userID);
			if (userID == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
				return;
			}
			request.setAttribute("validatedUserID", userID);
			if (error != null) {
				if (error.equals("RefundFailed")) {
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("publicAndCustomer/paymentError.jsp?error=RefundFailed");
					dispatcher.forward(request, response);
				}
			}
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/paymentError.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/paymentError.jsp");
			dispatcher.forward(request, response);
			System.err.println("Error: \" + e);\r\n");
		}

	}

	// Function to validate user id
	private String validateUserID(Connection connection, String userID) {
	    if (userID != null) {
	        String sqlStr = "SELECT COUNT(*) FROM users WHERE users.userID=?";
	        try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
	            ps.setString(1, userID);
	            try (ResultSet rs = ps.executeQuery()) {
	                if (rs.next()) {
	                    int rowCount = rs.getInt(1);
	                    if (rowCount < 1) {
	                        userID = null;
	                    }
	                }
	            }
	        } catch (SQLException e) {
	        	userID=null;
	            System.err.println("Error: " + e.getMessage());
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
