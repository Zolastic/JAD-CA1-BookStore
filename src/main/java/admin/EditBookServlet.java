package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.AbstractMap.SimpleEntry;

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
import utils.CloudinaryUtil;
import utils.DBConnection;
import utils.DispatchUtil;
import utils.HttpServletRequestUploadWrapper;

/**
 * Servlet implementation class EditBook
 */
@WebServlet("/admin/EditBook")
public class EditBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GenreDAO genreDAO = new GenreDAO();
	private AuthorDAO authorDAO = new AuthorDAO();
	private PublisherDAO publisherDAO = new PublisherDAO();
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
			request.getRequestDispatcher("editBook.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "index.jsp?statusCode=500");
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
		String bookID = null;

		try (Connection connection = DBConnection.getConnection()) {
			HttpServletRequestUploadWrapper requestWrapper = new HttpServletRequestUploadWrapper(request);

			bookID = requestWrapper.getParameter("bookID");
			String title = requestWrapper.getParameter("title");
			double price = Double.parseDouble(requestWrapper.getParameter("price"));
			String author = requestWrapper.getParameter("author");
			String publisher = requestWrapper.getParameter("publisher");
			int quantity = Integer.parseInt(requestWrapper.getParameter("quantity"));
			String pubDate = requestWrapper.getParameter("date");
			String isbn = requestWrapper.getParameter("isbn");
			String description = requestWrapper.getParameter("description");
			String genreId = requestWrapper.getParameter("genre");
			int sold = Integer.parseInt(requestWrapper.getParameter("sold"));
			byte[] imageInByte = requestWrapper.getBytesParameter("image");

			SimpleEntry<String, String> imageResult = imageInByte.length > 0 ? CloudinaryUtil.uploadImage(imageInByte) : null;
			String imageURL = null;
			String imagePublicID = null;
			
			if (imageResult != null) {
				imageURL = imageResult.getKey();
				imagePublicID = imageResult.getValue();
			}

			
			int statusCode = bookDAO.updateBook(connection, bookID, title, price, author, publisher, quantity, pubDate,
					isbn, description, genreId, sold, imageURL, imagePublicID);
			// Load data for page
			loadData(request, connection, bookID);

			DispatchUtil.dispatch(request, response, "editBook.jsp?statusCode=" + statusCode + "&bookID=" + bookID);
			System.out.println("Woots");
		} catch (Exception e) {
			DispatchUtil.dispatch(request, response, "editBook.jsp?statusCode=500&bookID=" + bookID);
		}
	}
}
