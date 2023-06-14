package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import model.*;
import utils.DBConnection;
import utils.HttpServletRequestUploadWrapper;

/**
 * Servlet implementation class EditBook
 */
@WebServlet("/admin/EditBook")
public class EditBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

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
		List<Genre> genres = getGenres(connection);
		List<Author> authors = getAuthors(connection);
		List<Publisher> publishers = getPublishers(connection);
		Book book = getBook(connection, bookID);
		request.setAttribute("genres", genres);
		request.setAttribute("authors", authors);
		request.setAttribute("publishers", publishers);
		request.setAttribute("book", book);
	}

	private List<Genre> getGenres(Connection connection) throws SQLException {
		try (Statement genreStatement = connection.createStatement();
				ResultSet genreResultSet = genreStatement.executeQuery(
						"SELECT genre_id as genreId, genre_name as genreName, genre_img as image FROM genre;");) {

			List<Genre> genres = new ArrayList<>();
			while (genreResultSet.next()) {
				String genreId = genreResultSet.getString("genreId");
				String genreName = genreResultSet.getString("genreName");
				String genreImage = genreResultSet.getString("image");
				genres.add(new Genre(genreId, genreName, genreImage));
			}

			return genres;
		}
	}

	private List<Author> getAuthors(Connection connection) throws SQLException {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM author;");) {

			List<Author> authors = new ArrayList<>();
			while (resultSet.next()) {
				String authorId = resultSet.getString("authorID");
				String authorName = resultSet.getString("authorName");
				authors.add(new Author(authorId, authorName));
			}

			return authors;
		}
	}

	private List<Publisher> getPublishers(Connection connection) throws SQLException {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery("SELECT * FROM publisher;");) {

			List<Publisher> publishers = new ArrayList<>();
			while (resultSet.next()) {
				String publisherId = resultSet.getString("publisherID");
				String publisherName = resultSet.getString("publisherName");
				publishers.add(new Publisher(publisherId, publisherName));
			}

			return publishers;
		}
	}

	private Book getBook(Connection connection, String bookID) throws SQLException {
		String sqlStr = "SELECT book.book_id as bookId, book.img, book.title, book.price, book.description, \r\n"
				+ "book.publication_date as publicationDate, book.ISBN, book.inventory, genre.genre_name as genreName, book.sold, \r\n"
				+ "ROUND(AVG(IFNULL(rating, 0)), 1) as rating , author.authorName, publisher.publisherName \r\n"
				+ "FROM book \r\n" + "JOIN genre ON genre.genre_id = book.genre_id \r\n"
				+ "LEFT JOIN review ON review.bookID = book.book_id \r\n"
				+ "JOIN author ON book.authorID = author.authorID \r\n"
				+ "JOIN publisher ON book.publisherID = publisher.publisherID \r\n" + "WHERE book.book_id = ?;";
		try (Statement statement = connection.createStatement();
				PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, bookID);

			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				String isbn = resultSet.getString("isbn");
				String title = resultSet.getString("title");
				String author = resultSet.getString("authorName");
				String publisher = resultSet.getString("publisherName");
				String publication_date = resultSet.getString("publicationDate");
				String description = resultSet.getString("description");
				String img = resultSet.getString("img");
				String genreName = resultSet.getString("genreName");
				int sold = resultSet.getInt("sold");
				int inventory = resultSet.getInt("inventory");
				double price = resultSet.getDouble("price");
				double rating = resultSet.getDouble("rating");
				resultSet.close();
				Book book = new Book(bookID, isbn, title, author, publisher, publication_date, description, genreName,
						img, sold, inventory, price, rating);
				return book;
			}

			throw new RuntimeException("Book not found!!! bookID: " + bookID);
		}

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
				RequestDispatcher success = request.getRequestDispatcher("editBook.jsp?bookID=" + bookID);
				success.forward(request, response);
			} else {
				RequestDispatcher error = request.getRequestDispatcher("editBook.jsp?errCode=400&bookID=" + bookID);
				error.forward(request, response);
			}

			System.out.println("Woots");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
