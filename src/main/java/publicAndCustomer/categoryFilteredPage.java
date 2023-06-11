package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import model.Book;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Genre;
import utils.DBConnection;

/**
 * Servlet implementation class categoryFilteredPage
 */
@WebServlet("/categoryFilteredPage")
public class categoryFilteredPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public categoryFilteredPage() {
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
		String genreID = request.getParameter("genreID");
		String genreName = request.getParameter("genreName");
		String userID = null;
		if (userIDAvailable != null) {
			if (userIDAvailable.equals("true")) {
				userID = (String) request.getSession().getAttribute("userID");
			}
		}

		List<Book> allGenreBook = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			String action = request.getParameter("action");

			if (action != null && action.equals("searchBookByTitle")) {
				String searchInput = request.getParameter("searchInput");
				if (genreID != null && searchInput != null) {

						searchInput="%"+searchInput+"%";
						allGenreBook = searchBookByTitle(connection, genreID, searchInput);
						request.setAttribute("searchExecuted", "true");

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

				if (genreID != null) {
					allGenreBook = getBooksByGenre(connection, genreID);
				}
			}

			connection.close();
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}

		request.setAttribute("allGenreBook", allGenreBook);
		request.setAttribute("genreName", genreName);
		request.setAttribute("validatedUserID", userID);
		RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/categoryFilteredPage.jsp");
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

	private List<Book> getBooksByGenre(Connection connection, String genreID) throws SQLException {
		List<Book> allGenreBook = new ArrayList<>();
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    WHERE book.genre_id = ?\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName;";
		PreparedStatement ps = connection.prepareStatement(sqlStr);
		ps.setString(1, genreID);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Book genreBook = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
					rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
					rs.getString("description"), rs.getString("genre_name"), rs.getString("img"), rs.getInt("sold"),
					rs.getInt("inventory"), rs.getDouble("price"), 1, rs.getDouble("average_rating"));
			allGenreBook.add(genreBook);
		}
		return allGenreBook;
	}

	private List<Book> searchBookByTitle(Connection connection, String genreID, String searchInput)
			throws SQLException {
		List<Book> allGenreBook = new ArrayList<>();
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    WHERE book.genre_id = ?\r\n" + "	   AND book.title LIKE ?\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName;";
		PreparedStatement ps = connection.prepareStatement(sqlStr);
		ps.setString(1, genreID);
		ps.setString(2, searchInput);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			Book genreBook = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
					rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
					rs.getString("description"), rs.getString("genre_name"), rs.getString("img"), rs.getInt("sold"),
					rs.getInt("inventory"), rs.getDouble("price"), 1, rs.getDouble("average_rating"));
			allGenreBook.add(genreBook);
		}
		return allGenreBook;
	}

//	private List<Book> searchBookByISBN(Connection connection, String genreID, String searchInput) throws SQLException {
//		List<Book> allGenreBook = new ArrayList<>();
//		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
//				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
//				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
//				+ "    JOIN author ON book.authorID = author.authorID\r\n"
//				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
//				+ "    WHERE book.genre_id = ?\r\n" + "	   AND book.ISBN LIKE ?\r\n"
//				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName;";
//		PreparedStatement ps = connection.prepareStatement(sqlStr);
//		ps.setString(1, genreID);
//		ps.setString(2, searchInput);
//		ResultSet rs = ps.executeQuery();
//		while (rs.next()) {
//			Book genreBook = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
//					rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
//					rs.getString("description"), rs.getString("genre_name"), rs.getString("img"), rs.getInt("sold"),
//					rs.getInt("inventory"), rs.getDouble("price"), 1, rs.getDouble("average_rating"));
//			allGenreBook.add(genreBook);
//		}
//		return allGenreBook;
//	}

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
