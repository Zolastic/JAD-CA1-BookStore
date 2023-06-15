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
 * Servlet implementation class CategoryFilteredPage
 */

/**
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA1
 */

@WebServlet("/CategoryFilteredPage")
public class CategoryFilteredPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CategoryFilteredPage() {
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
		String url = null;
		int totalPages = 1;
		int page = getPageFromParams(request);
		if (userIDAvailable != null) {
			if (userIDAvailable.equals("true")) {
				userID = (String) request.getSession().getAttribute("userID");
			}
		}
		List<Book> allGenreBook = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			// Check for action
			String action = request.getParameter("action");
			userID = validateUserID(connection, userID);
			if (action != null && action.equals("searchBookByTitle")) {
				String searchInput = request.getParameter("searchInput");
				if (genreID != null && searchInput != null) {
					// Get search results
					allGenreBook = searchBookByTitle(connection, genreID, ("%" + searchInput + "%"), page);
					totalPages = getTotalPagesByGenreSearch(connection, genreID, ("%" + searchInput + "%"));
					request.setAttribute("searchExecuted", "true");
					request.setAttribute("allGenreBook", allGenreBook);
					request.setAttribute("totalPages", totalPages);
					request.setAttribute("genreName", genreName);
					request.setAttribute("validatedUserID", userID);
					url = "publicAndCustomer/categoryFilteredPage.jsp?action=searchBookByTitle&searchInput="
							+ searchInput;
				}
			} else {
				if (genreID != null) {
					// Get all books in that particular genre
					allGenreBook = getBooksByGenre(connection, genreID, page);
					totalPages = getTotalPagesByGenre(connection, genreID);
					request.setAttribute("allGenreBook", allGenreBook);
					request.setAttribute("totalPages", totalPages);
					request.setAttribute("genreName", genreName);
					request.setAttribute("validatedUserID", userID);
					url = "publicAndCustomer/categoryFilteredPage.jsp";
				}
			}
			connection.close();
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(url);
		dispatcher.forward(request, response);
	}

	// Get page from parameter
	private int getPageFromParams(HttpServletRequest request) {
		String pageStr = request.getParameter("page");
		int page = 1; // Default page value is 1
		if (pageStr != null) {
			try {
				page = Integer.parseInt(pageStr);
			} catch (NumberFormatException e) {
				// invalid set to page 1
				page = 1;
			}
		}
		return page;
	}

	// Function to validate user id
	private String validateUserID(Connection connection, String userID) {
		if (userID != null) {
			String sqlStr = "SELECT COUNT(*) FROM users WHERE users.userID=?";
			try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
				ps.setString(1, userID);
				try (ResultSet rs = ps.executeQuery()) {
					if (rs.next()) {
						int rowCount = rs.getInt(1);
						if (rowCount < 1) {
							userID = null;
						}
					}
				}
			} catch (SQLException e) {
				userID = null;
				System.err.println("Error: " + e.getMessage());
			}
		}
		return userID;
	}

	// Get book based on their genre
	private List<Book> getBooksByGenre(Connection connection, String genreID, int page) throws SQLException {
		List<Book> allGenreBook = new ArrayList<>();
		int pageSize = 10; // Number of books per page
		int offset = (page - 1) * pageSize;
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    WHERE book.genre_id = ?\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName LIMIT ?, ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, genreID);
			ps.setInt(2, offset);
			ps.setInt(3, pageSize);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Book genreBook = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
						rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
						rs.getString("description"), rs.getString("genre_name"), rs.getString("img"), rs.getInt("sold"),
						rs.getInt("inventory"), rs.getDouble("price"), 1, rs.getDouble("average_rating"));
				allGenreBook.add(genreBook);
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return allGenreBook;
	}

	// Get total pages of genre
	private int getTotalPagesByGenre(Connection connection, String genreID) throws SQLException {
		int pageSize = 10;
		String countSqlStr = "SELECT COUNT(*) FROM book WHERE book.genre_id = ?";
		try (PreparedStatement count = connection.prepareStatement(countSqlStr)) {
			count.setString(1, genreID);
			try (ResultSet countRs = count.executeQuery()) {
				if (countRs.next()) {
					int totalBooks = countRs.getInt(1);
					return (int) Math.ceil((double) totalBooks / pageSize);
				} else {
					return 0;
				}
			}
		}
	}

	// Get total pages of genre search
	private int getTotalPagesByGenreSearch(Connection connection, String genreID, String searchInput)
			throws SQLException {
		int pageSize = 10;
		String countSqlStr = "SELECT COUNT(*) FROM book WHERE book.genre_id = ? AND book.title LIKE ?";
		try (PreparedStatement count = connection.prepareStatement(countSqlStr)) {
			count.setString(1, genreID);
			count.setString(2, searchInput);
			try (ResultSet countRs = count.executeQuery()) {
				if (countRs.next()) {
					int totalBooks = countRs.getInt(1);
					return (int) Math.ceil((double) totalBooks / pageSize);
				} else {
					return 0;
				}
			}
		}
	}

	// search book by their title
	private List<Book> searchBookByTitle(Connection connection, String genreID, String searchInput, int page)
			throws SQLException {
		List<Book> allGenreBook = new ArrayList<>();
		int pageSize = 10; // Number of books per page
		int offset = (page - 1) * pageSize;
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    WHERE book.genre_id = ?\r\n" + "	   AND book.title LIKE ?\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName LIMIT ?, ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, genreID);
			ps.setString(2, searchInput);
			ps.setInt(3, offset);
			ps.setInt(4, pageSize);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Book genreBook = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
						rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
						rs.getString("description"), rs.getString("genre_name"), rs.getString("img"), rs.getInt("sold"),
						rs.getInt("inventory"), rs.getDouble("price"), 1, rs.getDouble("average_rating"));
				allGenreBook.add(genreBook);
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return allGenreBook;
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
