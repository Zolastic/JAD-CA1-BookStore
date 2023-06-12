package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.User;
import utils.DBConnection;

/**
 * Servlet implementation class ProfilePage
 */
@WebServlet("/ProfilePage")
public class ProfilePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProfilePage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		if (session == null) {
			res.sendRedirect(req.getContextPath() + "/publicAndCustomer/registrationPage.jsp");
			return;
		}
		
		String userIDFromSession = (String) session.getAttribute("userID");
		if (userIDFromSession == null) {
			res.sendRedirect(req.getContextPath() + "/publicAndCustomer/registrationPage.jsp");
			return;
		}
		
		try (Connection connection = DBConnection.getConnection()) {
			String userID = request.getParameter("userID");
			loadData(request, connection, userID);
			request.getRequestDispatcher("publicAndCustomer/profilePage.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}
	
	private void loadData(HttpServletRequest request, Connection connection, String userID) throws SQLException {
		User user = getUserInfo(connection, userID);
		request.setAttribute("user", user);
	}
	
	private User getUserInfo(Connection connection, String userID) throws SQLException {
		String sqlStr = "SELECT * FROM users WHERE userID = ?;\r\n";
		try (Statement statement = connection.createStatement();
				PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, userID);

			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String password = resultSet.getString("password");
				String role = resultSet.getString("role");
				String img = resultSet.getString("img");
				resultSet.close();
				User user = new User(userID, name, email, password, role, img);
				return user;
			}

			throw new RuntimeException("User not found!!! userID: " + userID);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
