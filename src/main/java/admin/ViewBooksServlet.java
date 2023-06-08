package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.*;
import utils.DBConnection;

/**
 * Servlet implementation class viewBooks
 */
@WebServlet("/viewBooks")
public class ViewBooks extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewBooks() {
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

			loadData(request, connection);
			request.getRequestDispatcher("admin/viewBooks.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection) throws SQLException {
		List<Book> books = getBooks(connection);
		request.setAttribute("books", books);
	}

	private List<Book> getBooks(Connection connection) throws SQLException {
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(
						"SELECT book.book_id as bookID, book.img, book.title, book.price, book.description, \r\n"
								+ "book.publication_date as publicationDate, book.ISBN, book.inventory, genre.genre_name as genreName, book.sold, \r\n"
								+ "ROUND(AVG(IFNULL(rating, 0)), 1) as rating , author.authorName, publisher.publisherName \r\n"
								+ "FROM book \r\n" + "JOIN genre ON genre.genre_id = book.genre_id \r\n"
								+ "LEFT JOIN review ON review.bookID = book.book_id \r\n"
								+ "JOIN author ON book.authorID = author.authorID \r\n"
								+ "JOIN publisher ON book.publisherID = publisher.publisherID \r\n"
								+ "GROUP BY book.book_id, book.img, book.title, book.price, \r\n"
								+ "genre.genre_name, book.sold, book.inventory, author.authorName, \r\n"
								+ "publisher.publisherName;");) {

			List<Book> books = new ArrayList<>();
			while (resultSet.next()) {
				int bookID = resultSet.getInt("bookID");
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
				books.add(new Book(bookID, isbn, title, author, publisher, publication_date, description, genreName,
						img, sold, inventory, price, rating));
			}

			return books;
		}
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
