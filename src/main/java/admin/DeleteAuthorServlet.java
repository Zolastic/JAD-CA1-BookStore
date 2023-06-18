package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AuthorDAO;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class DeleteAuthorServlet
 */
@WebServlet("/admin/DeleteAuthor")
public class DeleteAuthorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AuthorDAO authorDAO = new AuthorDAO();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String authorID = request.getParameter("authorID");
		try (Connection connection = DBConnection.getConnection()) {
			int statusCode = authorDAO.deleteAuthor(connection, authorID);
			
			DispatchUtil.dispatch(request, response, "ViewAuthors?errCode=" + statusCode);
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "ViewAuthors?errCode=500");
		}
	}

}
