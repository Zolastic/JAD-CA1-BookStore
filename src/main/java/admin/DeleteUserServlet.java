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
import utils.CloudinaryUtil;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class DeleteUserServlet
 */
@WebServlet("/admin/DeleteUser")
public class DeleteUserServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAO();
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userID = request.getParameter("userID");
		try (Connection connection = DBConnection.getConnection()) {
			String imagePublicID = userDAO.getUserImagePublicID(connection, userID);
			int statusCode = userDAO.deleteAccount(connection, userID);
			if (imagePublicID != null) {
				statusCode = CloudinaryUtil.deleteImageFromCld(imagePublicID);
			}
			
			DispatchUtil.dispatch(request, response, "ViewUsers?errCode=" + statusCode);
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "ViewUsers?errCode=500");
		}
	}

}
