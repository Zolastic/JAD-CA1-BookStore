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
 * Servlet implementation class OTPServlet
 */
@WebServlet("/OTP")
public class OTPServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAO();
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
			DispatchUtil.dispatch(request, response, "publicAndCustomer/registrationPage.jsp?statusCode=401");
			return;
		}
		
		String otp = first + second + third + fourth + fifth + sixth;
		try (Connection connection = DBConnection.getConnection()) {
			User user = userDAO.getUserInfo(connection, otpUserID);
			if (user == null) {
				DispatchUtil.dispatch(request, response, "publicAndCustomer/registrationPage.jsp?statusCode=401");
				return;
			}
			
			userOTPDAO.updateOTP(connection, otpUserID, user.getSecret());
			String otpImage = OTPManagement.generateBase64Image(user.getSecret(), user.getEmail());
	        request.setAttribute("otpImage", otpImage);
			
			if (!userOTPDAO.verifyOTP(connection, otpUserID, otp)) {
				DispatchUtil.dispatch(request, response, "publicAndCustomer/registrationPage.jsp?statusCode=401&type=OTP");
				return;
			}
			
			String role = user.getRole();
			session.removeAttribute("otpUserID");
	        session.setAttribute("userID", otpUserID);
	        session.setAttribute("role", role);
	        request.getRequestDispatcher("home.jsp").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "publicAndCustomer/registrationPage.jsp?statusCode=401");
		}
	}

}
