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
 * Servlet implementation class EditUserServlet
 */
@WebServlet("/admin/EditUserProfile")
public class EditUserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAO();
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String userID = request.getParameter("userID");
			User user = loadData(request, response, connection, userID);
			if (user == null) {
				DispatchUtil.dispatch(request, response, "viewUsers.jsp");
				return;
			}
			DispatchUtil.dispatch(request, response, "editUserProfile.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "viewUsers.jsp");
		}
	}
	
	private User loadData(HttpServletRequest request, HttpServletResponse response, Connection connection,
			String userID) throws SQLException, ServletException, IOException {
		User user = userDAO.getUserInfo(connection, userID);
		request.setAttribute("user", user);
		return user;	
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userID = null;
		try (Connection connection = DBConnection.getConnection()) {
	        userID = request.getParameter("userID");
	        String name = request.getParameter("name");
	        String email = request.getParameter("email");
	        String image = null;
		
			int statusCode = userDAO.updateUser(connection, name, email, image, userID);
			
			User user = loadData(request, response, connection, userID);
			if (user == null) {
				DispatchUtil.dispatch(request, response, "viewUsers.jsp");
				return;
			}
			
			DispatchUtil.dispatch(request, response, "editUserProfile.jsp?statusCode=" + statusCode + "&userID=" + userID);

		} catch (Exception e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "editUserProfile.jsp?statusCode=500&userID=" + userID);
		}
	}

}
