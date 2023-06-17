package auth;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserOTPDAO;
import utils.DBConnection;
import utils.OTPManagement;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserOTPDAO userOTPDAO = new UserOTPDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String validateCredentialsSqlStr = "SELECT * FROM users WHERE email = ? and password = ?;";
		
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement validateCredentialsPS = connection.prepareStatement(validateCredentialsSqlStr)) {
			
			validateCredentialsPS.setString(1, email);
			validateCredentialsPS.setString(2, password);
			ResultSet resultSet = validateCredentialsPS.executeQuery();
			
			if (!resultSet.next()) {
				request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp?statusCode=401").forward(request, response);
				return;
			}
			
			String userID = resultSet.getString("userID");
			String secret = resultSet.getString("secret");
			HttpSession session = request.getSession();
	        session.setAttribute("otpUserID", userID);
	        String otpImage = OTPManagement.generateBase64Image(secret, resultSet.getString("email"));
	        request.setAttribute("otpImage", otpImage);
			
			if (!userOTPDAO.updateOTP(connection, userID, secret)) {
				request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp?statusCode=401").forward(request, response);
				return;
			}
			
			request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp?type=OTP").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			// redirect to error page
		}
	}


}
