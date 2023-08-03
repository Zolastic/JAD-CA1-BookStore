package auth;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import dao.UserOTPDAO;
import model.User;
import utils.DBConnection;
import utils.DispatchUtil;
import utils.OTPManagement;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAO();
	private UserOTPDAO userOTPDAO = new UserOTPDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		try (Connection connection = DBConnection.getConnection()) {
			User user = userDAO.validateUserCredentials(connection, email, password);
			if (user == null) {
				DispatchUtil.dispatch(request, response, "publicAndCustomer/registrationPage.jsp?statusCode=401");
				return;
			}
			
			String userID = user.getUserID();
			String secret = user.getSecret();
			HttpSession session = request.getSession();
	        session.setAttribute("otpUserID", userID);
	        String otpImage = OTPManagement.generateBase64Image(secret, user.getEmail());
	        request.setAttribute("otpImage", otpImage);
			
			DispatchUtil.dispatch(request, response, "publicAndCustomer/registrationPage.jsp?type=OTP");
			
		} catch (Exception e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "publicAndCustomer/registrationPage.jsp?statusCode=401");
		}
	}


}
