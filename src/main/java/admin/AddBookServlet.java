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

import dao.AuthorDAO;
import dao.BookDAO;
import dao.GenreDAO;
import dao.PublisherDAO;
import model.Author;
import model.Genre;
import model.Publisher;
import utils.DBConnection;
import utils.DispatchUtil;
import utils.HttpServletRequestUploadWrapper;

/**
 * Servlet implementation class AddBook
 */
@WebServlet("/admin/AddBook")
public class AddBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GenreDAO genreDAO = new GenreDAO();
	private AuthorDAO authorDAO = new AuthorDAO();
	private PublisherDAO publisherDAO = new PublisherDAO();
	private BookDAO bookDAO = new BookDAO();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			
			loadData(request, connection);
			DispatchUtil.dispatch(request, response, "addBook.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "addBook.jsp?statusCode=500");
		}
	}

	private void loadData(HttpServletRequest request, Connection connection) throws SQLException {
		List<Genre> genres = genreDAO.getGenres(connection);
		List<Author> authors = authorDAO.getAuthors(connection);
		List<Publisher> publishers = publisherDAO.getPublishers(connection);
		request.setAttribute("genres", genres);
		request.setAttribute("authors", authors);
		request.setAttribute("publishers", publishers);
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (Connection conn = DBConnection.getConnection()) {
			 HttpServletRequestUploadWrapper requestWrapper = new HttpServletRequestUploadWrapper(request);
	        
	        String title = requestWrapper.getParameter("title");
			double price = Double.parseDouble(requestWrapper.getParameter("price"));
			int author = Integer.parseInt(requestWrapper.getParameter("author"));
			int publisher = Integer.parseInt(requestWrapper.getParameter("publisher"));
			int quantity = Integer.parseInt(requestWrapper.getParameter("quantity"));
			String pubDate = requestWrapper.getParameter("date");
			String isbn = requestWrapper.getParameter("isbn");
			String description = requestWrapper.getParameter("description");
			int genreId = Integer.parseInt(requestWrapper.getParameter("genre"));
			String image = requestWrapper.getBase64Parameter("image");
	        
			int statusCode = bookDAO.addBook(title, price, author, publisher, quantity, pubDate, isbn, description, genreId, image);
			
			loadData(request, conn);
			DispatchUtil.dispatch(request, response, "addBook.jsp?statusCode=" + statusCode);
		} catch (Exception e) {
			DispatchUtil.dispatch(request, response, "addBook.jsp?statusCode=500");
		}
	}

}
