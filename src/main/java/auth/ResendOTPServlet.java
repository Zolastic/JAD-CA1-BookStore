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
import utils.OTPManagement;

/**
 * Servlet implementation class OTPServlet
 */
@WebServlet("/ResendOTP")
public class ResendOTPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAO();
	private UserOTPDAO userOTPDAO = new UserOTPDAO();
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String otpUserID = (String) session.getAttribute("otpUserID");
		
		if (otpUserID == null) {
			request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp?statusCode=401").forward(request, response);
			return;
		}
		
		try (Connection connection = DBConnection.getConnection()) {
			
			User user = userDAO.getUserInfoByID(connection, otpUserID);
			if (user == null) {
				request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp?statusCode=401").forward(request, response);
				return;
			}
			
			userOTPDAO.updateOTP(connection, otpUserID, user.getSecret());
			String otpImage = OTPManagement.generateBase64Image(user.getSecret(), user.getEmail());
	        request.setAttribute("otpImage", otpImage);
			
	        request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp?type=OTP").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
