package publicAndCustomer;

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

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		String sqlStr = "SELECT * FROM users WHERE email = ? and password = ?;";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStr);) {
			ps.setString(1, email);
			ps.setString(2, password);
			
			ResultSet resultSet = ps.executeQuery();
			
			if (resultSet.next()) {
				String userID = resultSet.getString("userID");
				String role = resultSet.getString("role");
				HttpSession session = request.getSession();
		        session.setAttribute("userID", userID);
		        session.setAttribute("role", role);
		        request.getRequestDispatcher("publicAndCustomer/home.jsp").forward(request, response);
			} else {
				request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp?statusCode=401").forward(request, response);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}


}
