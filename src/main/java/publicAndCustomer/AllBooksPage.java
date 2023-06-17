package publicAndCustomer;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BookDAO;
import dao.VerifyUserDAO;
import model.Book;
import utils.DBConnection;

/**
 * Servlet implementation class allBooksPage
 */

/**
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA1
 */

@WebServlet("/AllBooksPage")
public class AllBooksPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	private BookDAO bookDAO = new BookDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AllBooksPage() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = null;
		String userIDAvailable = request.getParameter("userIDAvailable");
		String userID = null;
		int totalPages=1;
		int page = getPageFromParams(request);
		if (userIDAvailable != null) {
			if (userIDAvailable.equals("true")) {
				userID = (String) request.getSession().getAttribute("userID");
			}
		}
		List<Book> allBooks = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			String action = request.getParameter("action");
			// If the user is searching for a book
			if (action != null && action.equals("searchBookByTitle")) {
				String searchInput = request.getParameter("searchInput");
				if (searchInput != null) {
					// Function to search to show the search results
					allBooks = bookDAO.searchBookByTitle(connection, ("%" + searchInput + "%"), page);
					totalPages =bookDAO.getTotalPagesForSearch(connection, ("%" + searchInput + "%"));
					// Validate the user id
					userID = verifyUserDAO.validateUserID(connection, userID);
					request.setAttribute("searchExecuted", "true");
					request.setAttribute("allBooks", allBooks);
					request.setAttribute("totalPages", totalPages);
					request.setAttribute("validatedUserID", userID);
					url = "publicAndCustomer/allBooksPage.jsp?action=searchBookByTitle&searchInput=" + searchInput;
				}
			} else {
				userID = verifyUserDAO.validateUserID(connection, userID);
				allBooks = bookDAO.getAllBooks(connection, page);
				totalPages = bookDAO.getTotalPagesForAllBooks(connection);
				request.setAttribute("totalPages", totalPages);
				request.setAttribute("allBooks", allBooks);
				request.setAttribute("validatedUserID", userID);
				url = "publicAndCustomer/allBooksPage.jsp";
			}
			connection.close();
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

	// Get page from parameter
	private int getPageFromParams(HttpServletRequest request) {
		String pageStr = request.getParameter("page");
		int page = 1; // Default page value is 1
		if (pageStr != null) {
			try {
				page = Integer.parseInt(pageStr);
			} catch (NumberFormatException e) {
				// invalid set to page 1
				page = 1;
			}
		}
		return page;
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
