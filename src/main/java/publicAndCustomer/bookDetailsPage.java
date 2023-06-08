package publicAndCustomer;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import model.Book;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.DBConnection;
import java.net.URLDecoder;

@WebServlet("/bookDetailsPage")
public class bookDetailsPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public bookDetailsPage() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bookID = request.getParameter("bookID");
		String encodedUserID = request.getParameter("userID");
		String userID = null;
		Book bookDetails = null;
		List<Map<String, Object>> reviews = new ArrayList<>();
		if (bookID != null) {
			try (Connection connection = DBConnection.getConnection()) {
				if (encodedUserID != null) {
					userID = URLDecoder.decode(encodedUserID, "UTF-8");
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

				String simpleProc = "{call getBookDetails(?)}";
				CallableStatement cs = connection.prepareCall(simpleProc);
				cs.setString(1, bookID);
				cs.execute();
				ResultSet resultSetForBookDetails = cs.getResultSet();

				if (resultSetForBookDetails.next()) {
					bookDetails = new Book(resultSetForBookDetails.getString("book_id"),
							resultSetForBookDetails.getString("ISBN"), resultSetForBookDetails.getString("title"),
							resultSetForBookDetails.getString("authorName"),
							resultSetForBookDetails.getString("publisherName"),
							resultSetForBookDetails.getString("publication_date"),
							resultSetForBookDetails.getString("description"),
							resultSetForBookDetails.getString("genre_name"), resultSetForBookDetails.getString("img"),
							resultSetForBookDetails.getInt("sold"), resultSetForBookDetails.getInt("inventory"),
							resultSetForBookDetails.getDouble("price"), 1,
							resultSetForBookDetails.getDouble("average_rating"));
					String sqlStr = "SELECT review.*, users.name FROM review, users WHERE review.custID=users.userID AND bookID=?;";
					PreparedStatement ps = connection.prepareStatement(sqlStr);
					ps.setString(1, bookID);
					ResultSet rs = ps.executeQuery();

					while (rs.next()) {

						Map<String, Object> review = new HashMap<>();
						review.put("userName", rs.getString("name"));
						review.put("review_text", rs.getString("review_text"));
						review.put("ratingByEachCust", rs.getDouble("rating"));
						review.put("ratingDate", rs.getString("ratingDate"));
						reviews.add(review);

					}
				}
				connection.close();
			} catch (SQLException e) {
				System.err.println("Error :" + e);
			}

		}
		request.setAttribute("bookDetails", bookDetails);
		request.setAttribute("reviews", reviews);
		request.setAttribute("validatedUserID", userID);
		RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/bookDetailsPage.jsp");
		dispatcher.forward(request, response);

	}

	protected void addToCart(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    String validatedUserID = request.getParameter("validatedUserID");
	    if (validatedUserID == null) {
	        RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/login.jsp");
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
	                String cartItemsID = UUID.randomUUID().toString();

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
	                    String insertQuery = "INSERT INTO cart_items (cartItemsID, cartID, Qty, BookID) VALUES (?, ?, ?, ?)";
	                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
	                    insertStatement.setString(1, cartItemsID);
	                    insertStatement.setString(2, cartID);
	                    insertStatement.setInt(3, quantity);
	                    insertStatement.setString(4, bookID);

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
	                System.out.println("Error: " + e);
	                String referer = (request.getHeader("Referer") + "&addToCart=failed");
	                response.sendRedirect(referer);
	            }
	        }
	    }
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if (action != null && action.equals("addToCart")) {
			addToCart(request, response);
		} else {
			doGet(request, response);
		}
	}
}
