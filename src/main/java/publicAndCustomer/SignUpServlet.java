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
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement duplicateCheckps = connection.prepareStatement(duplicateCheckSqlStr);
				PreparedStatement insertps = connection.prepareStatement(insertSqlStr);) {
			duplicateCheckps.setString(1, email);
			ResultSet resultSet = duplicateCheckps.executeQuery();

			if (resultSet.next()) {
				response.sendRedirect(request.getContextPath()
						+ "/publicAndCustomer/registrationPage.jsp?signUp=true&statusCode=409");
				return;
			} else {
				insertps.setString(1, (UUID.randomUUID()).toString());
				insertps.setString(2, name);
				insertps.setString(3, email);
				insertps.setString(4, password);

				int affectedRows = insertps.executeUpdate();

				if (affectedRows > 0) {
					response.sendRedirect(request.getContextPath() + "/publicAndCustomer/registrationPage.jsp");
					return;
				} else {
					response.sendRedirect(request.getContextPath()
							+ "/publicAndCustomer/registrationPage.jsp?signUp=true&statusCode=500");
					return;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

}
