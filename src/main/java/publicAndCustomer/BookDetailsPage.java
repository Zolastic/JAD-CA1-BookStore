package publicAndCustomer;

/**
 * Servlet implementation class BookDetailsPage
 */

/**
 * Author(s): Soh Jian Min (P2238856)
 * Description: JAD CA1
 */

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import model.Book;
import dao.VerifyUserDAO;
import dao.BookDAO;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.DBConnection;

@WebServlet("/BookDetailsPage")
public class BookDetailsPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	private BookDAO bookDAO = new BookDAO();

	public BookDetailsPage() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bookID = request.getParameter("bookID");
		String userID = null;
		userID = (String) request.getSession().getAttribute("userID");
		Book bookDetails = null;
		List<Map<String, Object>> reviews = new ArrayList<>();
		if (bookID != null) {
			try (Connection connection = DBConnection.getConnection()) {
				// Validate the userID
				userID = verifyUserDAO.validateUserID(connection, userID);
				// Get the book details
				bookDetails = bookDAO.getBookDetailsForCustomer(connection, bookID);
				// Get the reviews of the book
				reviews = getBookReviews(connection, bookID);
				connection.close();
			} catch (SQLException e) {
				System.err.println("Error: " + e);
			}
		}
		request.setAttribute("bookDetails", bookDetails);
		request.setAttribute("reviews", reviews);
		request.setAttribute("validatedUserID", userID);
		RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/bookDetailsPage.jsp");
		dispatcher.forward(request, response);
	}


	// Get all reviews of a book
	private List<Map<String, Object>> getBookReviews(Connection connection, String bookID) {
	    List<Map<String, Object>> reviews = new ArrayList<>();
	    String sqlStr = "SELECT review.*, users.name, users.img FROM review, users WHERE review.custID=users.userID AND bookID=?;";
	    try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
	        ps.setString(1, bookID);
	        try (ResultSet rs = ps.executeQuery()) {
	            while (rs.next()) {
	                Map<String, Object> review = new HashMap<>();
	                review.put("userName", rs.getString("name"));
	                review.put("userImg", rs.getString("img"));
	                review.put("review_text", rs.getString("review_text"));
	                review.put("ratingByEachCust", rs.getDouble("rating"));
	                review.put("ratingDate", rs.getString("ratingDate"));
	                reviews.add(review);
	            }
	        }
	    } catch (SQLException e) {
	        System.err.println("Error: " + e.getMessage());
	    }
	    return reviews;
	}


	// Function to add book to user's cart
	protected void addToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String validatedUserID = request.getParameter("validatedUserID");
		if (validatedUserID == null || validatedUserID.equals("null")) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
			dispatcher.forward(request, response);
		} else {
			String bookID = request.getParameter("bookID");
			int quantity = Integer.parseInt(request.getParameter("quantity"));
			String cartID = null;
			if (bookID == null || quantity == 0) {
				String referer = (request.getHeader("Referer") + "&addToCart=failed");
				response.sendRedirect(referer);
			} else {
				try (Connection connection = DBConnection.getConnection()) {
					String queryToGetCartID = "SELECT cartID FROM cart WHERE custID = ?";
					PreparedStatement statement = connection.prepareStatement(queryToGetCartID);
					statement.setString(1, validatedUserID);

					ResultSet resultSet = statement.executeQuery();

					if (resultSet.next()) {
						cartID = resultSet.getString("cartID");
					}

					String checkQuery = "SELECT Qty FROM cart_items WHERE cartID = ? AND BookID = ?";
					PreparedStatement checkStatement = connection.prepareStatement(checkQuery);
					checkStatement.setString(1, cartID);
					checkStatement.setString(2, bookID);
					ResultSet checkResultSet = checkStatement.executeQuery();

					if (checkResultSet.next()) {
						int currentQuantity = checkResultSet.getInt("Qty");
						quantity += currentQuantity;

						String updateQuery = "UPDATE cart_items SET Qty = ? WHERE cartID = ? AND BookID = ?";
						PreparedStatement updateStatement = connection.prepareStatement(updateQuery);
						updateStatement.setInt(1, quantity);
						updateStatement.setString(2, cartID);
						updateStatement.setString(3, bookID);
						int rowsAffected = updateStatement.executeUpdate();

						if (rowsAffected > 0) {
							String referer = (request.getHeader("Referer") + "&addToCart=success");
							response.sendRedirect(referer);
						} else {
							String referer = (request.getHeader("Referer") + "&addToCart=failed");
							response.sendRedirect(referer);
						}
					} else {
						String insertQuery = "INSERT INTO cart_items (cartID, Qty, BookID) VALUES (?, ?, ?)";
						PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
						insertStatement.setString(1, cartID);
						insertStatement.setInt(2, quantity);
						insertStatement.setString(3, bookID);

						int rowsAffected = insertStatement.executeUpdate();

						if (rowsAffected > 0) {
							String referer = (request.getHeader("Referer") + "&addToCart=success");
							response.sendRedirect(referer);
						} else {
							String referer = (request.getHeader("Referer") + "&addToCart=failed");
							response.sendRedirect(referer);
						}
					}

					connection.close();
				} catch (SQLException e) {
					System.err.println("Error: " + e.getMessage());
					String referer = (request.getHeader("Referer") + "&addToCart=failed");
					response.sendRedirect(referer);
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Check for actions
		String action = request.getParameter("action");
		if (action != null && action.equals("addToCart")) {
			addToCart(request, response);
		} else {
			doGet(request, response);
		}
	}
}
