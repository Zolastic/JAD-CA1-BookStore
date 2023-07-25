package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BookDAO;
import model.Book;
import utils.DBConnection;

/**
 * Servlet implementation class FilterCustomerByBookMain
 */
@WebServlet("/admin/FilterCustomersByBookMain")
public class FilterCustomersByBookMain extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookDAO bookDAO = new BookDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<Book> allBooks = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			allBooks = bookDAO.getAllBooksWOPage(connection);
			request.setAttribute("allBooks", allBooks);
			connection.close();
		} catch (SQLException e) {
			System.err.println("Error: " + e);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("filterCustomersByBookMain.jsp?error=connectionErr");
			dispatcher.forward(request, response);
			return;
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher("filterCustomersByBookMain.jsp");
		dispatcher.forward(request, response);
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
