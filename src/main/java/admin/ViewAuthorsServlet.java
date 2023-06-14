package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AuthorDAO;
import model.Author;
import utils.DBConnection;

/**
 * Servlet implementation class ViewAuthorsServlet
 */
@WebServlet("/admin/ViewAuthors")
public class ViewAuthorsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AuthorDAO authorDAO = new AuthorDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewAuthorsServlet() {
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
		try (Connection connection = DBConnection.getConnection()) {

			loadData(request, connection);
			request.getRequestDispatcher("viewAuthors.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection) throws SQLException {
		String userInput = request.getParameter("userInput");
		List<Author> authors = authorDAO.searchAuthors(connection, userInput);
		request.setAttribute("authors", authors);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}