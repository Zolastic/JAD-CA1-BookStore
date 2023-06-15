package auth;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utils.DBConnection;
import utils.OTPCreation;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String validateCredentialsSqlStr = "SELECT * FROM users WHERE email = ? and password = ?;";
		String updateOtpSqlStr = "UPDATE user_otp SET  otp = ?, otp_creation_timestamp = CURRENT_TIMESTAMP() WHERE user_id = ?;";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement validateCredentialsPS = connection.prepareStatement(validateCredentialsSqlStr);
				PreparedStatement updateOtpPS = connection.prepareStatement(updateOtpSqlStr);) {
			
			validateCredentialsPS.setString(1, email);
			validateCredentialsPS.setString(2, password);
			ResultSet resultSet = validateCredentialsPS.executeQuery();
			
			if (!resultSet.next()) {
				request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp?statusCode=401").forward(request, response);
				return;
			}
			
			
			String userID = resultSet.getString("userID");
			HttpSession session = request.getSession();
	        session.setAttribute("otpUserID", userID);
			String otp = OTPCreation.createOTP();
			updateOtpPS.setString(1, otp);
			updateOtpPS.setString(2, userID);
			int affectedRows = updateOtpPS.executeUpdate();
			
			if (affectedRows == 0) {
				request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp?statusCode=401").forward(request, response);
				return;
			}
			
			request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp?type=OTP").forward(request, response);
			
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}


}
