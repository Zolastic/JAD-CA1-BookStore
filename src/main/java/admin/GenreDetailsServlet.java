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
import dao.GenreDAO;
import model.Book;
import model.Genre;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class GenreDetialsServlet
 */
@WebServlet("/admin/GenreDetails")
public class GenreDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookDAO bookDAO = new BookDAO();
	private GenreDAO genreDAO = new GenreDAO();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String genreID = request.getParameter("genreID");
			loadData(request, connection, genreID);
			request.getRequestDispatcher("genreDetails.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "index.jsp");
		}
	}

	private void loadData(HttpServletRequest request, Connection connection, String genreID) throws SQLException {
		List<Book> books = bookDAO.getBooksByGenreId(connection, genreID);
		Genre genre = genreDAO.getGenreById(connection, genreID);
		request.setAttribute("genre", genre);
		request.setAttribute("books", books);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
