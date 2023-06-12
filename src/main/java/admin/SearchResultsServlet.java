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

import dao.BookDAO;
import model.Book;
import utils.DBConnection;

/**
 * Servlet implementation class SearchResultsServlet
 */
@WebServlet("/admin/SearchResults")
public class SearchResultsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private BookDAO bookDAO = new BookDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SearchResultsServlet() {
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
			String userInput = request.getParameter("userInput");
			loadData(request, connection, userInput);
			request.getRequestDispatcher("viewBooks.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection, String userInput) throws SQLException {
		List<Book> books = bookDAO.searchBooks(connection, userInput);
		request.setAttribute("books", books);
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
