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
 * Servlet implementation class EditUserPasswordServlet
 */
@WebServlet("/admin/EditUserPassword")
public class EditUserPasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAO();

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String userID = request.getParameter("userID");
			loadData(request, response, connection, userID);
			request.getRequestDispatcher("editUserPassword.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "index.jsp");
		}
	}
	
	private void loadData(HttpServletRequest request, HttpServletResponse response, Connection connection,
			String userID) throws SQLException, ServletException, IOException {
		User user = userDAO.getUserInfoByID(connection, userID);

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "viewUsers.jsp");
			return;
		}
		request.setAttribute("user", user);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userID = request.getParameter("userID");
		String newPassword = request.getParameter("newPassword");
		String confirmNewPassword = request.getParameter("confirmNewPassword");

		try (Connection connection = DBConnection.getConnection()) {
			loadData(request, response, connection, userID);
			int statusCode = userDAO.updateUserPassword(connection, userID, newPassword, confirmNewPassword);
			System.out.println("statusCode: " + statusCode);
			
			DispatchUtil.dispatch(request, response, "editUserPassword.jsp?statusCode=" + statusCode + "&userID=" + userID);

		} catch (Exception e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "editUserPassword.jsp?statusCode=500&userID=" + userID);
		}
	}

}
