package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AuthorDAO;
import dao.BookDAO;
import dao.GenreDAO;
import dao.PublisherDAO;
import model.Author;
import model.Book;
import model.Genre;
import model.Publisher;
import utils.DBConnection;
import utils.HttpServletRequestUploadWrapper;

/**
 * Servlet implementation class EditBook
 */
@WebServlet("/admin/EditBook")
public class EditBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	GenreDAO genreDAO = new GenreDAO();
	AuthorDAO authorDAO = new AuthorDAO();
	PublisherDAO publisherDAO = new PublisherDAO();
	BookDAO bookDAO = new BookDAO();

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditBookServlet() {
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
			String bookID = request.getParameter("bookID");
			loadData(request, connection, bookID);
			request.getRequestDispatcher("editBook.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection, String bookID) throws SQLException {
		List<Genre> genres = genreDAO.getGenres(connection);
		List<Author> authors = authorDAO.getAuthors(connection);
		List<Publisher> publishers = publisherDAO.getPublishers(connection);
		Book book = bookDAO.getBook(connection, bookID);
		request.setAttribute("genres", genres);
		request.setAttribute("authors", authors);
		request.setAttribute("publishers", publishers);
		request.setAttribute("book", book);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sqlStrWithImage = " UPDATE book SET title = ?, price = ?, authorID = ?, publisherID = ?, inventory = ?, \r\n"
				+ " publication_date = ?, ISBN = ?, description = ?,\r\n" + " genre_id = ?, sold = ?, img = ?\r\n"
				+ " WHERE book_id = ?;";

		String sqlStrWithoutImage = " UPDATE book SET title = ?, price = ?, authorID = ?, publisherID = ?, inventory = ?, \r\n"
				+ " publication_date = ?, ISBN = ?, description = ?,\r\n" + " genre_id = ?, sold = ?\r\n"
				+ " WHERE book_id = ?;";

		try (Connection connection = DBConnection.getConnection();) {
			HttpServletRequestUploadWrapper requestWrapper = new HttpServletRequestUploadWrapper(request);

			String bookID = requestWrapper.getParameter("bookID");
			String title = requestWrapper.getParameter("title");
			double price = Double.parseDouble(requestWrapper.getParameter("price"));
			int author = Integer.parseInt(requestWrapper.getParameter("author"));
			int publisher = Integer.parseInt(requestWrapper.getParameter("publisher"));
			int quantity = Integer.parseInt(requestWrapper.getParameter("quantity"));
			String pubDate = requestWrapper.getParameter("date");
			String isbn = requestWrapper.getParameter("isbn");
			String description = requestWrapper.getParameter("description");
			int genreId = Integer.parseInt(requestWrapper.getParameter("genre"));
			int sold = Integer.parseInt(requestWrapper.getParameter("sold"));
			String image = requestWrapper.getBase64Parameter("image");
			boolean noImage = image == null;

			String sqlUpdate = noImage ? sqlStrWithoutImage : sqlStrWithImage;

			PreparedStatement ps = connection.prepareStatement(sqlUpdate);

			ps.setString(1, title);
			ps.setDouble(2, price);
			ps.setInt(3, author);
			ps.setInt(4, publisher);
			ps.setInt(5, quantity);
			ps.setString(6, pubDate);
			ps.setString(7, isbn);
			ps.setString(8, description);
			ps.setInt(9, genreId);
			ps.setInt(10, sold);

			if (noImage) {
				ps.setString(11, bookID);
			} else {
				ps.setString(11, image);
				ps.setString(12, bookID);
			}

			int affectedRows = ps.executeUpdate();
			System.out.printf("affectedRows: " + affectedRows);
			// Load data for page
			loadData(request, connection, bookID);

			if (affectedRows > 0) {
				RequestDispatcher success = request.getRequestDispatcher("editBook.jsp?statusCode=200&bookID=" + bookID);
				success.forward(request, response);
			} else {
				RequestDispatcher error = request.getRequestDispatcher("editBook.jsp?statusCode=500&bookID=" + bookID);
				error.forward(request, response);
			}

			System.out.println("Woots");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
