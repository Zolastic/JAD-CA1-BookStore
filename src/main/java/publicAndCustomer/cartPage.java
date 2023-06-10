package publicAndCustomer;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;

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
//			String userIDAvailable = request.getParameter("userIDAvailable");
//			
//			String userID = null;
//			if(userIDAvailable!=null) {
//				if(userIDAvailable.equals("true")) {
//					userID=(String) request.getSession().getAttribute("userID");
//				}
//			}
			String scrollPosition = request.getParameter("scrollPosition");
//			String scrollPosition = "";
			String userID = "3";
			String cartID = null;
			List<Book> books = new ArrayList<>();
			if (userID != null) {

//				if (userID != null) {

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
				String sqlString = "SELECT cartID FROM cart WHERE custID = ?;";
				PreparedStatement sqlStatement = connection.prepareStatement(sqlString);
				sqlStatement.setString(1, userID);
				ResultSet rs1 = sqlStatement.executeQuery();
				while (rs1.next()) {
					cartID = rs1.getString("cartID");
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
				connection.close();
			} else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/login.jsp");
				dispatcher.forward(request, response);
			}
			request.setAttribute("scrollPosition", scrollPosition);
			request.setAttribute("cartID", cartID);
			request.setAttribute("cartItems", books);
			request.setAttribute("validatedUserID", userID);
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/cartPage.jsp");
			dispatcher.forward(request, response);


//		else {
//				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/login.jsp");
//				dispatcher.forward(request, response);
//			}
		} catch (SQLException e) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/login.jsp");
			dispatcher.forward(request, response);
			System.err.println("Error :" + e);
		}
	}

	private String removeParameterFromUrl(String url) {
		int questionMarkIndex = url.indexOf("?");
		if (questionMarkIndex >= 0) {
			String baseUrl = url.substring(0, questionMarkIndex);
			return baseUrl;
		}

		return url;
	}

	protected void selectCartItem(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bookID = request.getParameter("bookID");
		String cartID = request.getParameter("cartID");
		String scrollPosition = request.getParameter("scrollPositionForSelect");
		String newSelection = request.getParameter("newSelection");
		if (bookID == null || cartID == null || newSelection == null || scrollPosition == null) {
			String referer = request.getHeader("Referer");
			referer = removeParameterFromUrl(referer);
			response.sendRedirect(referer + "?scrollPosition=" + scrollPosition+ "&error=true");
		} else {
		try (Connection connection = DBConnection.getConnection()) {
				int newSelectionInt = Integer.parseInt(newSelection);
				String updateQuery = "UPDATE cart_items SET selected=? WHERE cartID=? AND BookID=?;";
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
				updateStatement.setInt(1, newSelectionInt);
				updateStatement.setString(2, cartID);
				updateStatement.setString(3, bookID);

				int rowsAffected = updateStatement.executeUpdate();

				if (rowsAffected > 0) {
					String referer = request.getHeader("Referer");
					referer = removeParameterFromUrl(referer);
					response.sendRedirect(referer + "?scrollPosition=" + scrollPosition);
				} else {
					String referer = request.getHeader("Referer");
					referer = removeParameterFromUrl(referer);
					response.sendRedirect(referer + "?scrollPosition=" + scrollPosition+ "&error=true");
				}
			}catch (SQLException e) {
			System.err.println("Error: " + e);
			String referer = request.getHeader("Referer");
			referer = removeParameterFromUrl(referer);
			response.sendRedirect(referer + "?scrollPosition=" + scrollPosition+ "&error=true");

		}
		} 
	}

	protected void selectAllCartItems(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String scrollPosition = request.getParameter("scrollPositionForSelectAll");
		String newSelection = request.getParameter("newSelection");
		String cartID = request.getParameter("cartID");
		if (cartID == null || newSelection == null || scrollPosition == null) {
			String referer = request.getHeader("Referer");
			referer = removeParameterFromUrl(referer);
			response.sendRedirect(referer + "?scrollPosition=" + scrollPosition+ "&error=true");
		} else {
		try (Connection connection = DBConnection.getConnection()) {

				int newSelectionInt = Integer.parseInt(newSelection);
				String updateQuery = "UPDATE cart_items SET selected=? WHERE cartID=?";
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
				updateStatement.setInt(1, newSelectionInt);
				updateStatement.setString(2, cartID);

				int rowsAffected = updateStatement.executeUpdate();

				if (rowsAffected > 0) {
					String referer = request.getHeader("Referer");
					referer = removeParameterFromUrl(referer);
					response.sendRedirect(referer + "?scrollPosition=" + scrollPosition);
				} else {
					String referer = request.getHeader("Referer");
					referer = removeParameterFromUrl(referer);
					response.sendRedirect(referer + "?scrollPosition=" + scrollPosition+ "&error=true");
				}
			}catch (SQLException e) {
			System.err.println("Error: " + e);

			String referer = request.getHeader("Referer");
			referer = removeParameterFromUrl(referer);
			response.sendRedirect(referer + "?scrollPosition=" + scrollPosition+ "&error=true");
		}
		} 
	}

	protected void deleteCartItem(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String scrollPosition = request.getParameter("scrollPositionForDelete");
		String bookID = request.getParameter("bookID");
		String cartID = request.getParameter("cartID");
		if (cartID == null || bookID == null || scrollPosition == null) {
			String referer = request.getHeader("Referer");
			referer = removeParameterFromUrl(referer);
			response.sendRedirect(referer + "?scrollPosition=" + scrollPosition+ "&error=true");
		} else {
			try (Connection connection = DBConnection.getConnection()) {
				String deleteQuery = "DELETE FROM cart_items WHERE cartID=? AND BookID=?;";
				PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
				deleteStatement.setString(1, cartID);
				deleteStatement.setString(2, bookID);
				int rowsAffected = deleteStatement.executeUpdate();

				if (rowsAffected > 0) {
					String referer = request.getHeader("Referer");
					referer = removeParameterFromUrl(referer);
					response.sendRedirect(referer + "?scrollPosition=" + scrollPosition);
				} else {
					String referer = request.getHeader("Referer");
					referer = removeParameterFromUrl(referer);
					response.sendRedirect(referer + "?scrollPosition=" + scrollPosition+ "&error=true");
				}
			} catch (SQLException e) {
				System.err.println("Error: " + e);
				String referer = request.getHeader("Referer");
				referer = removeParameterFromUrl(referer);
				response.sendRedirect(referer + "?scrollPosition=" + scrollPosition+ "&error=true");
			}

		}
	}

	protected void checkout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String cartItems = request.getParameter("selectedCartItems");
	    String cartID = request.getParameter("cartID");
	    
	    if (cartID == null || cartItems == null) {
	        String referer = request.getHeader("Referer");
	        referer = removeParameterFromUrl(referer);
	        response.sendRedirect(referer + "?error=true");
	    } else {
	        String encodedCartItems = URLEncoder.encode(cartItems, "UTF-8");

	        Cookie cartItemsCookie = new Cookie("cartItems", encodedCartItems);
	        cartItemsCookie.setMaxAge(5 * 60 * 60);
	        response.addCookie(cartItemsCookie);
	        
	        Cookie cartIDCookie = new Cookie("cartID", cartID);
	        cartIDCookie.setMaxAge(5 * 60 * 60);
	        response.addCookie(cartIDCookie);

	        response.sendRedirect("http://localhost:8080/CA1-assignment/checkoutPage");
	    }
	}

	protected void updateQuantity(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String scrollPosition = request.getParameter("scrollPositionForQty");
		String bookID = request.getParameter("bookID");
		String cartID = request.getParameter("cartID");
		String updatedQuantity = request.getParameter("updatedQuantity");
		if (cartID == null || bookID == null || scrollPosition == null || updatedQuantity == null) {
			String referer = request.getHeader("Referer");
			referer = removeParameterFromUrl(referer);
			response.sendRedirect(referer + "?scrollPosition=" + scrollPosition+ "&error=true");
		} else {
		try (Connection connection = DBConnection.getConnection()) {
				int updatedQuantityInt = Integer.parseInt(updatedQuantity);
				String updateQuery = "UPDATE cart_items SET Qty=? WHERE cartID=? AND BookID=?;";
				PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
				updateStatement.setInt(1, updatedQuantityInt);
				updateStatement.setString(2, cartID);
				updateStatement.setString(3, bookID);

				int rowsAffected = updateStatement.executeUpdate();

				if (rowsAffected > 0) {
					String referer = request.getHeader("Referer");
					referer = removeParameterFromUrl(referer);
					response.sendRedirect(referer + "?scrollPosition=" + scrollPosition);
				} else {
					String referer = request.getHeader("Referer");
					referer = removeParameterFromUrl(referer);
					response.sendRedirect(referer + "?scrollPosition=" + scrollPosition+ "&error=true");
				}
			}catch (SQLException e) {
			System.err.println("Error: " + e);
			String referer = request.getHeader("Referer");
			referer = removeParameterFromUrl(referer);
			response.sendRedirect(referer + "?scrollPosition=" + scrollPosition+ "&error=true");
		}
		} 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getParameter("action");
		if (action != null && action.equals("selectCartItem")) {
			selectCartItem(request, response);
		} else if (action != null && action.equals("selectAllCartItems")) {
			selectAllCartItems(request, response);
		} else if (action != null && action.equals("deleteCartItem")) {
			deleteCartItem(request, response);
		} else if (action != null && action.equals("checkout")) {
			checkout(request, response);
		} else if (action != null && action.equals("updateQuantity")) {
			updateQuantity(request, response);
		} else {
			doGet(request, response);
		}
	}

}
