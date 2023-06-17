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
import utils.DispatchUtil;

/**
 * Servlet implementation class viewBooks
 */
@WebServlet("/admin/ViewBooks")
public class ViewBooksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookDAO bookDAO = new BookDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {

			loadData(request, connection);
			DispatchUtil.dispatch(request, response, "viewBooks.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection) throws SQLException {
		String userInput = request.getParameter("userInput");
		List<Book> books = bookDAO.searchBooks(connection, userInput);
		request.setAttribute("books", books);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
