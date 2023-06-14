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

import utils.DBConnection;
import model.Book;

/**
 * Servlet implementation class Home
 */
@WebServlet("/publicAndCustomer/Home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Home() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setData(request);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
		dispatcher.forward(request, response);
	}

	// function to be run everytime home.jsp is run
	public void setData(HttpServletRequest request) {
		List<Book> popularBooks = new ArrayList<>();
		String validatedUserID = null;
		String userID=(String) request.getSession().getAttribute("userID");

		try (Connection connection = DBConnection.getConnection()) {
			popularBooks = popularBooks(connection);
			if(userID!=null) {
				validatedUserID = validateUserID(connection, userID);
			}
			connection.close();
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}

		request.setAttribute("popularBooks", popularBooks);
		request.setAttribute("validatedUserID", validatedUserID);
	}

	// Getting popular books
	private List<Book> popularBooks(Connection connection) throws SQLException {
		List<Book> popularBooks = new ArrayList<>();

		String query = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(review.rating) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName FROM book JOIN genre ON genre.genre_id = book.genre_id LEFT JOIN review ON review.bookID = book.book_id JOIN author ON book.authorID = author.authorID JOIN publisher ON book.publisherID = publisher.publisherID GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName ORDER BY book.sold DESC LIMIT 6;";

		try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {

			while (resultSet.next()) {
				String bookID = resultSet.getString("book_id");
				String iSBN = resultSet.getString("ISBN");
				String title = resultSet.getString("title");
				String author = resultSet.getString("authorName");
				String publisher = resultSet.getString("publisherName");
				String publicationDate = resultSet.getString("publication_date");
				String description = resultSet.getString("description");
				String genreName = resultSet.getString("genre_name");
				String img = resultSet.getString("img");
				int sold = resultSet.getInt("sold");
				int inventory = resultSet.getInt("inventory");
				double price = resultSet.getDouble("price");
				double rating = resultSet.getDouble("average_rating");

				Book popularBook = new Book(bookID, iSBN, title, author, publisher, publicationDate, description,
						genreName, img, sold, inventory, price, rating);
				popularBooks.add(popularBook);
			}
		}

		return popularBooks;
	}

	// Validate the userID
	private String validateUserID(Connection connection, Object sessionUserID) throws SQLException {
	    String userID = (String) sessionUserID;
	    String validatedUserID = null;

	    String sqlStr = "SELECT COUNT(*) FROM users WHERE users.userID=?";
	    PreparedStatement ps = connection.prepareStatement(sqlStr);
	        ps.setString(1, userID);
	        ResultSet rs = ps.executeQuery();
			rs.next();
			int rowCount = rs.getInt(1);
			if (rowCount < 1) {
				userID = null;
			}
		
		return userID;
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
