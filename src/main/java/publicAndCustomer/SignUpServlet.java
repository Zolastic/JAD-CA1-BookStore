package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import utils.DBConnection;

/**
 * Servlet implementation class SignUp
 */
@WebServlet("/SignUp")
public class SignUpServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SignUpServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
		String duplicateCheckSqlStr = "SELECT * FROM users WHERE email = ?;";
		String insertSqlStr = "INSERT INTO users (userID, name, email, password, role) VALUES (?, ?, ?, ?, \"customer\");";
		String insertCartSqlStr = "INSERT INTO cart (cartID, custID) VALUES (?, ?);";
		String insertUserOtpSqlStr = "INSERT INTO user_otp (user_id) VALUES (?);";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement duplicateCheckPS = connection.prepareStatement(duplicateCheckSqlStr);
				PreparedStatement insertPS = connection.prepareStatement(insertSqlStr);
				PreparedStatement insertCartPS = connection.prepareStatement(insertCartSqlStr);
				PreparedStatement insertUserOtpCartPS = connection.prepareStatement(insertUserOtpSqlStr);) {
			duplicateCheckPS.setString(1, email);
			ResultSet resultSet = duplicateCheckPS.executeQuery();

			if (resultSet.next()) {
				response.sendRedirect(request.getContextPath()
						+ "/publicAndCustomer/registrationPage.jsp?signUp=true&statusCode=409");
				return;
			} else {
				String customerID = (UUID.randomUUID()).toString();
				insertPS.setString(1, customerID);
				insertPS.setString(2, name);
				insertPS.setString(3, email);
				insertPS.setString(4, password);
				int affectedUserRows = insertPS.executeUpdate();

				if (affectedUserRows == 0) {
					response.sendRedirect(request.getContextPath()
							+ "/publicAndCustomer/registrationPage.jsp?signUp=true&statusCode=500");
					return;
				}

				insertCartPS.setString(1, (UUID.randomUUID()).toString());
				insertCartPS.setString(2, customerID);
				int affectedCartRows = insertCartPS.executeUpdate();

				if (affectedCartRows == 0) {
					response.sendRedirect(request.getContextPath()
							+ "/publicAndCustomer/registrationPage.jsp?signUp=true&statusCode=500");
					return;
				}

				insertUserOtpCartPS.setString(1, customerID);
				int affectedOtpRows = insertUserOtpCartPS.executeUpdate();

				if (affectedOtpRows == 0) {
					response.sendRedirect(request.getContextPath()
							+ "/publicAndCustomer/registrationPage.jsp?signUp=true&statusCode=500");
					return;
				}

				response.sendRedirect(request.getContextPath() + "/publicAndCustomer/registrationPage.jsp");
				return;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

}
