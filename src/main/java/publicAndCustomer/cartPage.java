package publicAndCustomer;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
 * Servlet implementation class cartPage
 */
@WebServlet("/cartPage")
public class cartPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public cartPage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String encodedUserID = request.getParameter("userID");
			String userID = null;
//			String userID="3";
			List<Book> books = new ArrayList<>();
			if (encodedUserID != null) {
				userID = URLDecoder.decode(encodedUserID, "UTF-8");
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
					if (userID == null) {
						RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/login.jsp");
						dispatcher.forward(request, response);
					}

					String sqlStr1 = "SELECT book.book_id, book.img, book.title, book.price, book.description, book.publication_date, book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(review.rating) AS DECIMAL(2,1)) AS average_rating, author.authorName, publisher.publisherName, cart_items.Qty, cart_items.selected\r\n"
							+ "    FROM book\r\n" + "    JOIN genre ON genre.genre_id = book.genre_id\r\n"
							+ "    LEFT JOIN review ON review.bookID = book.book_id\r\n"
							+ "    JOIN author ON book.authorID = author.authorID\r\n"
							+ "    JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
							+ "    JOIN cart ON cart.custID=?\r\n"
							+ "    JOIN cart_items ON cart.cartID=cart_items.cartID\r\n"
							+ "    WHERE cart_items.BookID=book.book_id\r\n"
							+ "    GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName;";
					PreparedStatement ps1 = connection.prepareStatement(sqlStr1);
					ps1.setString(1, userID);
					ResultSet resultSet = ps1.executeQuery();

					while (resultSet.next()) {
						String bookID = resultSet.getString("book_id");
						String isbn = resultSet.getString("ISBN");
						String title = resultSet.getString("title");
						String author = resultSet.getString("authorName");
						String publisher = resultSet.getString("publisherName");
						String publication_date = resultSet.getString("publication_date");
						String description = resultSet.getString("description");
						String genre_name = resultSet.getString("genre_name");
						String img = resultSet.getString("img");
						int sold = resultSet.getInt("sold");
						int inventory = resultSet.getInt("inventory");
						double price = resultSet.getDouble("price");
						double rating = resultSet.getDouble("average_rating");
						int quantity = resultSet.getInt("Qty");
						int selected = resultSet.getInt("selected");
						books.add(new Book(bookID, isbn, title, author, publisher, publication_date, description,
								genre_name, img, sold, inventory, price, rating, quantity, selected));
					}
				} else {
					RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/login.jsp");
					dispatcher.forward(request, response);
				}
				request.setAttribute("cartItems", books);
				request.setAttribute("validatedUserID", userID);
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/cartPage.jsp");
				dispatcher.forward(request, response);
				connection.close();
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/login.jsp");
				dispatcher.forward(request, response);
			}
		} catch (SQLException e) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/login.jsp");
			dispatcher.forward(request, response);
			System.err.println("Error :" + e);
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
