package publicAndCustomer;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Book;
import utils.DBConnection;

/**
 * Servlet implementation class allBooksPage
 */
@WebServlet("/AllBooksPage")
public class AllBooksPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AllBooksPage() {
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
		String userID = null;
		if (userIDAvailable != null) {
			if (userIDAvailable.equals("true")) {
				userID = (String) request.getSession().getAttribute("userID");
			}
		}

		List<Book> allBooks = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			String action = request.getParameter("action");

			if (action != null && action.equals("searchBookByTitle")) {
				String searchInput = request.getParameter("searchInput");
				if (searchInput != null) {

					allBooks = searchBookByTitle(connection, ("%" + searchInput + "%"));
					request.setAttribute("searchExecuted", "true");
					request.setAttribute("allBooks", allBooks);
					request.setAttribute("validatedUserID", userID);
					RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/allBooksPage.jsp?action=searchBookByTitle&searchInput="+searchInput);
					dispatcher.forward(request, response);
				}

			}
//			else if (action != null && action.equals("searchBookByISBN")) {
//				String searchInput = request.getParameter("searchInput");
//				if (genreID != null && searchInput != null) {
//					if (searchInput.length() != 0) {
//						allGenreBook = searchBookByISBN(connection, genreID, searchInput);
//					}
//
//				}
//
//			} 
			else {
				userID = validateUserID(connection, userID);

				allBooks = getAllBooks(connection);
			}

			connection.close();
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}

		request.setAttribute("allBooks", allBooks);
		request.setAttribute("validatedUserID", userID);
		RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/allBooksPage.jsp");
		dispatcher.forward(request, response);
	}

	private String validateUserID(Connection connection, String userID) throws SQLException {
		if (userID != null) {
			String sqlStr = "SELECT COUNT(*) FROM users WHERE users.userID=?";
			PreparedStatement ps = connection.prepareStatement(sqlStr);
			ps.setString(1, userID);
			ResultSet rs = ps.executeQuery();
			rs.next();
			int rowCount = rs.getInt(1);
			if (rowCount < 1) {
				userID = null;
			}
		}
		return userID;
	}

	private List<Book> getAllBooks(Connection connection) throws SQLException {
		List<Book> allBooks = new ArrayList<>();
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName;";
		Statement statement = connection.createStatement();
	    ResultSet rs = statement.executeQuery(sqlStr);
		while (rs.next()) {
			Book book = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
					rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
					rs.getString("description"), rs.getString("genre_name"), rs.getString("img"), rs.getInt("sold"),
					rs.getInt("inventory"), rs.getDouble("price"), 1, rs.getDouble("average_rating"));
			allBooks.add(book);
		}
		return allBooks;
	}

	private List<Book> searchBookByTitle(Connection connection, String searchInput) throws SQLException {
		List<Book> searchResults = new ArrayList<>();
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    WHERE book.title LIKE ?\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName;";
		PreparedStatement ps = connection.prepareStatement(sqlStr);
		ps.setString(1, searchInput);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Book searchResult = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
					rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
					rs.getString("description"), rs.getString("genre_name"), rs.getString("img"), rs.getInt("sold"),
					rs.getInt("inventory"), rs.getDouble("price"), 1, rs.getDouble("average_rating"));
			searchResults.add(searchResult);
		}
		return searchResults;
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
