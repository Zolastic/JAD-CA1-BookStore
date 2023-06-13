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

import dao.AuthorDAO;
import model.Author;
import model.Book;
import utils.DBConnection;

/**
 * Servlet implementation class AuthorDetails
 */
@WebServlet("/admin/AuthorDetails")
public class AuthorDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AuthorDAO authorDAO = new AuthorDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthorDetailsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try (Connection connection = DBConnection.getConnection()) {
			String authorID = request.getParameter("authorID");
			loadData(request, connection, authorID);
			request.getRequestDispatcher("authorDetails.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}
	
	private void loadData(HttpServletRequest request, Connection connection, String authorID) throws SQLException {
		List<Book> books = getBook(connection, authorID);
		Author author = authorDAO.getAuthor(connection, authorID);
		request.setAttribute("author", author);
		request.setAttribute("books", books);
	}
	
	private List<Book> getBook(Connection connection, String authorID) throws SQLException {
		String sqlStr = "SELECT book_id as bookID, title FROM book\r\n"
				+ "JOIN author ON book.authorID = author.authorID\r\n"
				+ "WHERE author.authorID = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, authorID);

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
