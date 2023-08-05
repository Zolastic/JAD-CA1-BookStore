package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

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
 * Servlet implementation class BookDetailsServlet
 */
@WebServlet("/admin/BookDetails")
public class BookDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookDAO bookDAO = new BookDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String bookID = request.getParameter("bookID");
			loadData(request, connection, bookID);
			DispatchUtil.dispatch(request, response, "bookDetails.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "index.jsp");
		}
	}

	private void loadData(HttpServletRequest request, Connection connection, String bookID) throws SQLException {
		Book book = bookDAO.getBook(connection, bookID);
		request.setAttribute("book", book);
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
