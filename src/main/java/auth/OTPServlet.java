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

import dao.UserDAO;
import dao.UserOTPDAO;
import model.User;
import utils.DBConnection;
import utils.OTPManagement;

/**
 * Servlet implementation class OTPServlet
 */
@WebServlet("/OTP")
public class OTPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserOTPDAO userOTPDAO = new UserOTPDAO();
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String otpUserID = (String) session.getAttribute("otpUserID");
		String first = request.getParameter("first");
		String second = request.getParameter("second");
		String third = request.getParameter("third");
		String fourth = request.getParameter("fourth");
		String fifth = request.getParameter("fifth");
		String sixth = request.getParameter("sixth");
		
		
		if (otpUserID == null || first == null || second == null || third == null || fourth == null || fifth == null || sixth == null) {
			request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp?statusCode=401").forward(request, response);
			return;
		}
		
		String otp = first + second + third + fourth + fifth + sixth;
		String otpSQL = "SELECT * FROM user_otp WHERE user_id = ? AND otp = ? AND TIMESTAMPDIFF(MINUTE, otp_creation_timestamp, CURRENT_TIMESTAMP()) < 5;";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement otpPS = connection.prepareStatement(otpSQL)) {
			otpPS.setString(1, otpUserID);
			otpPS.setString(2, otp);
			
			User user = UserDAO.getUserInfo(connection, otpUserID);
			if (user == null) {
				request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp?statusCode=401").forward(request, response);
				return;
			}
			
			userOTPDAO.updateOTP(connection, otpUserID, user.getSecret());
			String otpImage = OTPManagement.generateBase64Image(user.getSecret(), user.getEmail());
	        request.setAttribute("otpImage", otpImage);
			
			ResultSet otpResultSet = otpPS.executeQuery();
			if (!otpResultSet.next()) {
				request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp?statusCode=401&type=OTP").forward(request, response);
				return;
			}
			
			String role = user.getRole();
			session.removeAttribute("otpUserID");
	        session.setAttribute("userID", otpUserID);
	        session.setAttribute("role", role);
	        request.getRequestDispatcher("home.jsp").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
