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

/**
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA1
 */

@WebServlet("/AllBooksPage")
public class AllBooksPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AllBooksPage() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = null;
		String userIDAvailable = request.getParameter("userIDAvailable");
		String userID = null;
		int totalPages=1;
		int page = getPageFromParams(request);
		if (userIDAvailable != null) {
			if (userIDAvailable.equals("true")) {
				userID = (String) request.getSession().getAttribute("userID");
			}
		}
		List<Book> allBooks = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			String action = request.getParameter("action");
			// If the user is searching for a book
			if (action != null && action.equals("searchBookByTitle")) {
				String searchInput = request.getParameter("searchInput");
				if (searchInput != null) {
					// Function to search to show the search results
					allBooks = searchBookByTitle(connection, ("%" + searchInput + "%"), page);
					totalPages =getTotalPagesForSearch(connection, ("%" + searchInput + "%"));
					// Validate the user id
					userID = validateUserID(connection, userID);
					request.setAttribute("searchExecuted", "true");
					request.setAttribute("allBooks", allBooks);
					request.setAttribute("totalPages", totalPages);
					request.setAttribute("validatedUserID", userID);
					url = "publicAndCustomer/allBooksPage.jsp?action=searchBookByTitle&searchInput=" + searchInput;
				}
			} else {
				userID = validateUserID(connection, userID);
				allBooks = getAllBooks(connection, page);
				totalPages =getTotalPagesForAllBooks(connection);
				request.setAttribute("totalPages", totalPages);
				request.setAttribute("allBooks", allBooks);
				request.setAttribute("validatedUserID", userID);
				url = "publicAndCustomer/allBooksPage.jsp";
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
				userID=null;
				System.err.println("Error: " + e.getMessage());
			}
		}
		return userID;
	}

	// Get all the books from db
	private List<Book> getAllBooks(Connection connection, int page) {
		List<Book> allBooks = new ArrayList<>();
		int pageSize = 10; // Number of books per page
		int offset = (page - 1) * pageSize;
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName LIMIT ?, ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setInt(1, offset);
			ps.setInt(2, pageSize);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Book book = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
							rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
							rs.getString("description"), rs.getString("genre_name"), rs.getString("img"),
							rs.getInt("sold"), rs.getInt("inventory"), rs.getDouble("price"), 1,
							rs.getDouble("average_rating"));
					allBooks.add(book);
				}
			}
		} catch (SQLException e) {
			allBooks=null;
			System.err.println("Error: " + e.getMessage());
		}
		return allBooks;
	}
	
	// Get total page for all books
	private int getTotalPagesForAllBooks(Connection connection) throws SQLException {
		int pageSize=10;
	    String countSqlStr = "SELECT COUNT(*) FROM book";
	    try (PreparedStatement countPs = connection.prepareStatement(countSqlStr)) {
	        try (ResultSet countRs = countPs.executeQuery()) {
	            countRs.next();
	            int totalBooks = countRs.getInt(1);
	            return (int) Math.ceil((double) totalBooks / pageSize);
	        }
	    }
	}

	// Get the search results
	private List<Book> searchBookByTitle(Connection connection, String searchInput, int page) throws SQLException {
		List<Book> searchResults = new ArrayList<>();
		int pageSize = 10; // Number of books per page
		int offset = (page - 1) * pageSize;
		String sqlStr = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(IFNULL(review.rating,0)) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName\r\n"
				+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
				+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
				+ "    JOIN author ON book.authorID = author.authorID\r\n"
				+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "    WHERE book.title LIKE ?\r\n"
				+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName LIMIT ?, ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
	        ps.setString(1, searchInput);
	        ps.setInt(2, offset);
	        ps.setInt(3, pageSize);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Book searchResult = new Book(rs.getString("book_id"), rs.getString("ISBN"), rs.getString("title"),
	                        rs.getString("authorName"), rs.getString("publisherName"), rs.getString("publication_date"),
	                        rs.getString("description"), rs.getString("genre_name"), rs.getString("img"), rs.getInt("sold"),
	                        rs.getInt("inventory"), rs.getDouble("price"), 1, rs.getDouble("average_rating"));
	                searchResults.add(searchResult);
	            }
	        }
	    } catch (SQLException e) {
	    	searchResults=null;
	        System.err.println("Error: " + e.getMessage());
	    }
	    return searchResults;
	}
	
	// Total Pages for search book by title
	private int getTotalPagesForSearch(Connection connection, String searchInput) throws SQLException {
		int pageSize=10;
	    String countSqlStr = "SELECT COUNT(*) FROM book WHERE book.title LIKE ?";
	    try (PreparedStatement count = connection.prepareStatement(countSqlStr)) {
	        count.setString(1,  searchInput);
	        try (ResultSet countRs = count.executeQuery()) {
	            countRs.next();
	            int totalBooks = countRs.getInt(1);
	            return (int) Math.ceil((double) totalBooks / pageSize);
	        }
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
