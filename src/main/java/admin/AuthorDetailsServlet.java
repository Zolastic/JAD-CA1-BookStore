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
import dao.BookDAO;
import model.Author;
import model.Book;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class AuthorDetails
 */
@WebServlet("/admin/AuthorDetails")
public class AuthorDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AuthorDAO authorDAO = new AuthorDAO();
	private BookDAO bookDAO = new BookDAO();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String authorID = request.getParameter("authorID");
			loadData(request, connection, authorID);
			DispatchUtil.dispatch(request, response, "authorDetails.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "index.jsp?statusCode=500");
		}
	}
	
	private void loadData(HttpServletRequest request, Connection connection, String authorID) throws SQLException {
		List<Book> books = bookDAO.getBooksByAuthorID(connection, authorID);
		Author author = authorDAO.getAuthorById(connection, authorID);
		request.setAttribute("author", author);
		request.setAttribute("books", books);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
