package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import dao.UserDAO;
import model.User;
import utils.DBConnection;

/**
 * Servlet implementation class ChangePasswordServlet
 */
@WebServlet("/ChangePassword")
public class ChangePasswordServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ChangePasswordServlet() {
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
			loadData(request, response, connection, userID);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}
	
    private void loadData(HttpServletRequest request, HttpServletResponse response, Connection connection,
			String userID) throws SQLException, ServletException, IOException {
		User user = UserDAO.getUserInfo(connection, userID);

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/publicAndCustomer/registrationPage.jsp");
			return;
		}
		request.setAttribute("user", user);
		request.getRequestDispatcher("publicAndCustomer/changePassword.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userID = request.getParameter("userID");
		String currentPassword = request.getParameter("currentPassword");
		String newPassword = request.getParameter("newPassword");
		String confirmNewPassword = request.getParameter("confirmNewPassword");
		
		String currentPasswordValidationSqlStr = "SELECT password FROM users WHERE userID = ? AND password = ?;";
		String updatePasswordSqlStr = "UPDATE users SET password = ? WHERE userID = ?;";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement currentPasswordValidationPS = connection.prepareStatement(currentPasswordValidationSqlStr);
				PreparedStatement updatePasswordPS = connection.prepareStatement(updatePasswordSqlStr);) {
			
			if (!newPassword.equals(confirmNewPassword)) {
				loadData(request, response, connection, userID);
				RequestDispatcher error = request
						.getRequestDispatcher("publicAndCustomer/changePassword.jsp?statusCode=400&userID=" + userID);
				error.forward(request, response);
				return;
			}
			
			currentPasswordValidationPS.setString(1, userID);
			currentPasswordValidationPS.setString(2, currentPassword);
			ResultSet currentPasswordValidationRS = currentPasswordValidationPS.executeQuery();
			
			if (!currentPasswordValidationRS.next()) {
				loadData(request, response, connection, userID);
				RequestDispatcher error = request
						.getRequestDispatcher("publicAndCustomer/changePassword.jsp?statusCode=401&userID=" + userID);
				error.forward(request, response);
				return;
			}
			
			updatePasswordPS.setString(1, newPassword);
			updatePasswordPS.setString(2, userID);
			
			int affectedRows = updatePasswordPS.executeUpdate();
			
			loadData(request, response, connection, userID);
			
			if (affectedRows > 0) {
				RequestDispatcher success = request.getRequestDispatcher("publicAndCustomer/changePassword.jsp?statusCode=200&userID=" + userID);
				success.forward(request, response);
			} else {
				RequestDispatcher error = request
						.getRequestDispatcher("publicAndCustomer/changePassword.jsp?statusCode=500&userID=" + userID);
				error.forward(request, response);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
