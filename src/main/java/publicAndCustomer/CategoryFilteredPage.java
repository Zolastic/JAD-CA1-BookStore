package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DBConnection;
import dao.GenreDAO;
import dao.VerifyUserDAO;

/**
 * Servlet implementation class CategoryFilteredPage
 */

/**
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA1
 */

@WebServlet("/CategoryFilteredPage")
public class CategoryFilteredPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	private GenreDAO genreDAO=new GenreDAO();
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CategoryFilteredPage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userIDAvailable = request.getParameter("userIDAvailable");
		String genreID = request.getParameter("genreID");
		String genreName = request.getParameter("genreName");
		String userID = null;
		String url = null;
		int totalPages = 1;
		int page = getPageFromParams(request);
		if (userIDAvailable != null) {
			if (userIDAvailable.equals("true")) {
				userID = (String) request.getSession().getAttribute("userID");
			}
		}
		List<Book> allGenreBook = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			// Check for action
			String action = request.getParameter("action");
			userID = verifyUserDAO.validateUserID(connection, userID);
			if (action != null && action.equals("searchBookByTitle")) {
				String searchInput = request.getParameter("searchInput");
				if (genreID != null && searchInput != null) {
					// Get search results
					allGenreBook = genreDAO.searchBookByTitle(connection, genreID, ("%" + searchInput + "%"), page);
					totalPages = genreDAO.getTotalPagesByGenreSearch(connection, genreID, ("%" + searchInput + "%"));
					request.setAttribute("searchExecuted", "true");
					request.setAttribute("allGenreBook", allGenreBook);
					request.setAttribute("totalPages", totalPages);
					request.setAttribute("genreName", genreName);
					request.setAttribute("validatedUserID", userID);
					url = "publicAndCustomer/categoryFilteredPage.jsp?action=searchBookByTitle&searchInput="
							+ searchInput;
				}
			} else {
				if (genreID != null) {
					// Get all books in that particular genre
					allGenreBook = genreDAO.getBooksByGenre(connection, genreID, page);
					totalPages = genreDAO.getTotalPagesByGenre(connection, genreID);
					request.setAttribute("allGenreBook", allGenreBook);
					request.setAttribute("totalPages", totalPages);
					request.setAttribute("genreName", genreName);
					request.setAttribute("validatedUserID", userID);
					url = "publicAndCustomer/categoryFilteredPage.jsp";
				}
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
