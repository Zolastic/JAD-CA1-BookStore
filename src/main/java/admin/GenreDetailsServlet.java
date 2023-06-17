package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.GenreDAO;
import model.Book;
import model.Genre;
import utils.DBConnection;

/**
 * Servlet implementation class GenreDetialsServlet
 */
@WebServlet("/admin/GenreDetails")
public class GenreDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GenreDAO genreDAO = new GenreDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GenreDetailsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try (Connection connection = DBConnection.getConnection()) {
			String genreID = request.getParameter("genreID");
			loadData(request, connection, genreID);
			request.getRequestDispatcher("genreDetails.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection, String genreID) throws SQLException {
		List<Book> books = getBooks(connection, genreID);
		Genre genre = genreDAO.getGenre(connection, genreID);
		request.setAttribute("genre", genre);
		request.setAttribute("books", books);
	}
	
	private List<Book> getBooks(Connection connection, String genreID) throws SQLException {
		String sqlStr = "SELECT book_id as bookID, title FROM book\r\n"
				+ "JOIN genre ON book.genre_id = genre.genre_id\r\n"
				+ "WHERE genre.genre_id = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, genreID);

			ResultSet resultSet = ps.executeQuery();

			List<Book> books = new ArrayList<>();
			while (resultSet.next()) {
				String bookID = resultSet.getString("bookID");
				String title = resultSet.getString("title");
				books.add(new Book(bookID, title));
			}
			resultSet.close();
			return books;
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}