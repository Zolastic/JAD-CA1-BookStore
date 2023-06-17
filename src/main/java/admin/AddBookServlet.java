package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AuthorDAO;
import dao.GenreDAO;
import dao.PublisherDAO;
import model.Author;
import model.Genre;
import model.Publisher;
import utils.DBConnection;
import utils.HttpServletRequestUploadWrapper;

/**
 * Servlet implementation class AddBook
 */
@WebServlet("/admin/AddBook")
public class AddBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	GenreDAO genreDAO = new GenreDAO();
	AuthorDAO authorDAO = new AuthorDAO();
	PublisherDAO publisherDAO = new PublisherDAO();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			
			loadData(request, connection);
			request.getRequestDispatcher("addBook.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
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
		String sqlStr = "INSERT INTO BOOK (title, price, authorID, publisherID, inventory, publication_date, ISBN, description, genre_id, img, book_id)\r\n"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sqlStr)) {
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
	        
			ps.setString(1, title);
			ps.setDouble(2, price);
			ps.setInt(3, author);
			ps.setInt(4, publisher);
			ps.setInt(5, quantity);
			ps.setString(6, pubDate);
			ps.setString(7, isbn);
			ps.setString(8, description);
			ps.setInt(9, genreId);
			ps.setString(10, image);
			ps.setString(11, (UUID.randomUUID()).toString());
			
			int affectedRows = ps.executeUpdate();
			// Load data for page
			loadData(request, conn);
			
			if (affectedRows > 0) {
				RequestDispatcher success = request.getRequestDispatcher("addBook.jsp?statusCode=500");
				success.forward(request, response);
			} else {
				RequestDispatcher error = request.getRequestDispatcher("addBook.jsp?statusCode=500");
				error.forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
