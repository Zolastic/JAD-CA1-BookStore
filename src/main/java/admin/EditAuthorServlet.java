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
import model.Author;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class EditAuthorServlet
 */
@WebServlet("/admin/EditAuthor")
public class EditAuthorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AuthorDAO authorDAO = new AuthorDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String authorID = request.getParameter("authorID");
			loadData(request, connection, authorID);
			DispatchUtil.dispatch(request, response, "editAuthor.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "index.jsp?statusCode=500");
		}
	}

	private void loadData(HttpServletRequest request, Connection connection, String authorID) throws SQLException {
		Author author = authorDAO.getAuthorById(connection, authorID);
		request.setAttribute("author", author);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String authorID = request.getParameter("authorID");
		String authorName = request.getParameter("name");

		try (Connection connection = DBConnection.getConnection()) {
			int statusCode = authorDAO.updateAuthor(connection, authorID, authorName);

			loadData(request, connection, authorID);

			DispatchUtil.dispatch(request, response, "editAuthor.jsp?statusCode=" + statusCode + "&authorID=" + authorID);
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "editAuthor.jsp?statusCode=500&authorID=" + authorID);
		}
	}

}
