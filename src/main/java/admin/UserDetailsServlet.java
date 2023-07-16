package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.UserDAO;
import model.User;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class UserDetailsServlet
 */
@WebServlet("/admin/UserDetails")
public class UserDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAO();
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String userID = request.getParameter("userID");
			loadData(request, connection, userID);
			DispatchUtil.dispatch(request, response, "userDetails.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}
	
	private void loadData(HttpServletRequest request, Connection connection, String userID) throws SQLException {
		User user = userDAO.getUserInfo(connection, userID);
		request.setAttribute("user", user);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
