package publicAndCustomer;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.net.URLDecoder;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Cookie;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.RefundCreateParams;

import model.Book;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.DBConnection;
import dao.VerifyUserDAO;
/**
 * Servlet implementation class CheckoutPage
 */

/**
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA1
 */

@WebServlet("/CheckoutPage")

public class CheckoutPage extends HttpServlet {
	private static final String STRIPE_SECRET_KEY = "sk_test_51NHoftHrRFi94qYrcY4Yxv3NiAwj1ea5D7zuxCZtQ0kT9beLlwCh8GbjFKSgPz3s9K8QJ0U5mjet6H8vRL4VZBLq001eDbwLUL";
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CheckoutPage() {
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
			Cookie[] cookies = request.getCookies();
			String userIDAvailable = request.getParameter("userIDAvailable");
			String userID = null;
			if (userIDAvailable != null) {
				if (userIDAvailable.equals("true")) {
					userID = (String) request.getSession().getAttribute("userID");
				}
			}
			List<Book> checkoutItems = new ArrayList<>();
			String checkoutItemsString = null;
			List<Map<String, Object>> checkoutItemsArrayString = new ArrayList<>();

			userID = verifyUserDAO.validateUserID(connection, userID);
			if (userID == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
				return;
			}
			// Get the checkout items from the cookies
			if (cookies != null) {
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("checkoutItems")) {
						checkoutItemsString = cookie.getValue();
						String encodedCartItems = cookie.getValue();
						checkoutItemsString = URLDecoder.decode(encodedCartItems, "UTF-8");
						JSONArray jsonArray = new JSONArray(checkoutItemsString);
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							Map<String, Object> bookItem = new HashMap<>();
							String bookID = jsonObject.getString("bookID");
							int quantity = jsonObject.getInt("quantity");
							bookItem.put("bookID", bookID);
							bookItem.put("quantity", quantity);
							checkoutItemsArrayString.add(bookItem);
						}
						if (checkoutItemsString != null) {
							break;
						}
					}
				}
			}
			checkoutItems = getCheckoutItems(connection, userID, checkoutItemsArrayString);
			connection.close();
			request.setAttribute("checkoutItems", checkoutItems);
			request.setAttribute("validatedUserID", userID);
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/checkoutPage.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/cartPage.jsp");
			dispatcher.forward(request, response);
			System.err.println("Error: \" + e);\r\n");
		}
	}

	// Get all the checkout items details
	private List<Book> getCheckoutItems(Connection connection, String userID,
			List<Map<String, Object>> checkoutItemsList){
		List<Book> checkoutItems = new ArrayList<>();

		for (Map<String, Object> itemMap : checkoutItemsList) {
			String bookID = (String) itemMap.get("bookID");
			int quantity = (int) itemMap.get("quantity");

			String simpleProc = "{call getBookDetails(?)}";
			try (CallableStatement cs = connection.prepareCall(simpleProc)) {
				cs.setString(1, bookID);
				cs.execute();
				try (ResultSet resultSetForBookDetails = cs.getResultSet()) {
					if (resultSetForBookDetails.next()) {
						Book book = new Book(resultSetForBookDetails.getString("book_id"),
								resultSetForBookDetails.getString("ISBN"), resultSetForBookDetails.getString("title"),
								resultSetForBookDetails.getString("authorName"),
								resultSetForBookDetails.getString("publisherName"),
								resultSetForBookDetails.getString("publication_date"),
								resultSetForBookDetails.getString("description"),
								resultSetForBookDetails.getString("genre_name"),
								resultSetForBookDetails.getString("img"), resultSetForBookDetails.getInt("sold"),
								resultSetForBookDetails.getInt("inventory"), resultSetForBookDetails.getDouble("price"),
								quantity, resultSetForBookDetails.getDouble("average_rating"));
						checkoutItems.add(book);
					}
				}
			} catch (SQLException e) {
				System.err.println("Error: " + e.getMessage());
			}
		}

		return checkoutItems;
	}

	// Function to generate an uuid
	private String uuidGenerator() {
		UUID uuid = UUID.randomUUID();
		return (uuid.toString());
	}

	// insert checkout items into DB transaction history after payment success
	private String insertTransactionHistory(Connection connection, double subtotal, String custID, String address)
			throws SQLException {
		String transactionHistoryUUID = uuidGenerator();

		String sql = "INSERT INTO transaction_history (transaction_historyID, transactionDate, subtotal, custID, address) VALUES (?, ?, ?, ?, ?)";
		String transactionDate = getCurrentDateTime();
		try (PreparedStatement statement = connection.prepareStatement(sql)) {

			statement.setString(1, transactionHistoryUUID);
			statement.setString(2, transactionDate);
			statement.setDouble(3, subtotal);
			statement.setString(4, custID);
			statement.setString(5, address);

			int rowsAffected = statement.executeUpdate();

			statement.close();

			if (rowsAffected == 1) {
				return transactionHistoryUUID;
			} else {
				return null;
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			return null;
		}
	}

	// Function to get DATETIME
	private String getCurrentDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date currentDate = new Date();
		return dateFormat.format(currentDate);
	}

	// insert checkout items into DB transaction history items after payment success
	private Boolean insertTransactionHistoryItems(Connection connection, List<Book> checkoutItems,
			String transactionHistoryUUID) throws SQLException {
		Boolean success = true;
		String sql = "INSERT INTO transaction_history_items (transaction_historyID, transaction_history_itemID, bookID, Qty) VALUES (?, ?, ?, ?)";

		try (PreparedStatement statement = connection.prepareStatement(sql)) {
			for (Book book : checkoutItems) {
				String transactionHistoryItemUUID = uuidGenerator();
				statement.setString(1, transactionHistoryUUID);
				statement.setString(2, transactionHistoryItemUUID);
				statement.setString(3, book.getBookID());
				statement.setInt(4, book.getQuantity());
				int rowsAffected = statement.executeUpdate();
				if (rowsAffected != 1) {
					success = false;
					break;
				}
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			success = false;
		}

		return success;
	}

	// Delete the cart items that is already purchased after success payment
	private int deleteFromCart(Connection connection, List<Book> checkoutItems, String custID){
		int count = 0;
		String cartID = getCartID(connection, custID);
		if (cartID != null) {
			for (Book book : checkoutItems) {
				String deleteQuery = "DELETE FROM cart_items WHERE cartID=? AND BookID=?";
				try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
					deleteStatement.setString(1, cartID);
					deleteStatement.setString(2, book.getBookID());
					int rowsDeleted = deleteStatement.executeUpdate();
					count += rowsDeleted;
				} catch (SQLException e) {
					System.err.println("Error: " + e.getMessage());
				}
			}
		}
		return count;
	}

	// Update Book's inventory and sold
	private int updateBooks(Connection connection, List<Book> checkoutItems){
	    int count = 0;
	    for (Book book : checkoutItems) {
	        String updateQuery = "UPDATE book SET inventory = (inventory - ?), sold = (sold + ?) WHERE book_id=?";
	        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
	            updateStatement.setInt(1, book.getQuantity());
	            updateStatement.setInt(2, book.getQuantity());
	            updateStatement.setString(3, book.getBookID());
	            int rowsUpdated = updateStatement.executeUpdate();
	            count += rowsUpdated;
	        } catch (SQLException e) {
	            System.err.println("Error: " + e.getMessage());
	        }
	    }
	    return count;
	}

	// Get cart id with custID
	private String getCartID(Connection connection, String custID){
		String cartID = null;
		String selectQuery = "SELECT cartID FROM cart WHERE custID=?";
		try (PreparedStatement selectStatement = connection.prepareStatement(selectQuery)) {
			selectStatement.setString(1, custID);
			ResultSet resultSet = selectStatement.executeQuery();
			if (resultSet.next()) {
				cartID = resultSet.getString("cartID");
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}
		return cartID;
	}

	// Payment intent for stripe
	protected void paymentIntent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Stripe.apiKey = STRIPE_SECRET_KEY;
		String paymentMethodId = request.getParameter("paymentMethodId");
		String subtotal = request.getParameter("subtotal");
		String address = request.getParameter("address");
		String userID = (String) request.getSession().getAttribute("userID");
		double amountInDollars = Double.parseDouble(subtotal);
		// Stripe needs to take subtotal in cents
		long amount = Math.round(amountInDollars * 100);
		Cookie[] cookies = request.getCookies();
		List<Book> checkoutItems = new ArrayList<>();
		String checkoutItemsString = null;
		List<Map<String, Object>> checkoutItemsArrayString = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			if (cookies != null) {
				// Get the checkout items in cookies
				for (Cookie cookie : cookies) {
					if (cookie.getName().equals("checkoutItems")) {
						checkoutItemsString = cookie.getValue();
						String encodedCartItems = cookie.getValue();
						checkoutItemsString = URLDecoder.decode(encodedCartItems, "UTF-8");
						JSONArray jsonArray = new JSONArray(checkoutItemsString);
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							Map<String, Object> bookItem = new HashMap<>();

							String bookID = jsonObject.getString("bookID");
							int quantity = jsonObject.getInt("quantity");

							bookItem.put("bookID", bookID);
							bookItem.put("quantity", quantity);

							checkoutItemsArrayString.add(bookItem);
						}

						if (checkoutItemsString != null) {
							break;
						}
					}
				}
			}
			if (userID != null && address != null && checkoutItemsArrayString.size() != 0) {
				try {
					// get the Book Class version of checkout items
					checkoutItems = getCheckoutItems(connection, userID, checkoutItemsArrayString);
					// Create payment intent's parameters
					PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder().setCurrency("sgd")
							.setAmount(amount).setPaymentMethod(paymentMethodId).setConfirm(true).build();
					// Create payment intent
					PaymentIntent paymentIntent = PaymentIntent.create(createParams);
					// if payment succeeded do the necessary insertion
					if (paymentIntent != null && paymentIntent.getStatus().equals("succeeded")) {
						// If insertion of transaction history or transaction history items failed do a
						// refund
						String transactionHistoryUUID = insertTransactionHistory(connection, amountInDollars, userID,
								address);
						if (transactionHistoryUUID != null) {
							Boolean success = insertTransactionHistoryItems(connection, checkoutItems,
									transactionHistoryUUID);
							if (!success) {
								RefundCreateParams refundParams = new RefundCreateParams.Builder()
										.setPaymentIntent(paymentIntent.getId()).build();
								Refund refund = Refund.create(refundParams);
								if (refund.getStatus().equals("succeeded")) {
									clearCheckoutItemsCookie(response);
									response.sendRedirect("PaymentError?userIDAvailable=true");
								} else {
									clearCheckoutItemsCookie(response);
									response.sendRedirect("PaymentError?error=RefundFailed&userIDAvailable=true");
								}
							} else {
								// Payment success
								deleteFromCart(connection, checkoutItems, userID);
								updateBooks(connection, checkoutItems);
								clearCheckoutItemsCookie(response);
								response.sendRedirect("PaymentSuccess?userIDAvailable=true");
							}
						} else {
							RefundCreateParams refundParams = new RefundCreateParams.Builder()
									.setPaymentIntent(paymentIntent.getId()).build();

							Refund refund = Refund.create(refundParams);

							if (refund.getStatus().equals("succeeded")) {
								clearCheckoutItemsCookie(response);
								response.sendRedirect("PaymentError?userIDAvailable=true");
							} else {
								clearCheckoutItemsCookie(response);
								response.sendRedirect("PaymentError?error=RefundFailed&userIDAvailable=true");
							}
						}
					} else {
						// if payment failed but payment already processed
						if (paymentIntent != null) {
							RefundCreateParams refundParams = new RefundCreateParams.Builder()
									.setPaymentIntent(paymentIntent.getId()).build();

							Refund refund = Refund.create(refundParams);

							if (refund.getStatus().equals("succeeded")) {
								clearCheckoutItemsCookie(response);
								response.sendRedirect("PaymentError?userIDAvailable=true");
							} else {
								clearCheckoutItemsCookie(response);
								response.sendRedirect("PaymentError?error=RefundFailed&userIDAvailable=true");
							}
						} else {
							clearCheckoutItemsCookie(response);
							response.sendRedirect("PaymentError?userIDAvailable=true");
						}
					}
				} catch (StripeException e) {
					System.err.println("Error: " + e.getMessage());
					clearCheckoutItemsCookie(response);
					response.sendRedirect("PaymentError?userIDAvailable=true");
				}
			} else {
				clearCheckoutItemsCookie(response);
				response.sendRedirect("PaymentError?userIDAvailable=true");
			}

		} catch (SQLException e1) {
			System.err.println("Error: " + e1.getMessage());
			clearCheckoutItemsCookie(response);
			response.sendRedirect("PaymentError?userIDAvailable=true");
		}
	}

	// function to clearCheckoutItemsCookie
	private void clearCheckoutItemsCookie(HttpServletResponse response) {
		Cookie checkoutItemsCookie = new Cookie("checkoutItems", "");
		checkoutItemsCookie.setMaxAge(0);
		response.addCookie(checkoutItemsCookie);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Check action
		String action = request.getParameter("action");
		if (action != null && action.equals("payment")) {
			paymentIntent(request, response);
		} else {
			doGet(request, response);
		}
	}

}
