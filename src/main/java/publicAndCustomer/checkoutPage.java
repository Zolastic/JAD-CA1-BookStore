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

/**
 * Servlet implementation class checkoutPage
 */
@WebServlet("/checkoutPage")

public class checkoutPage extends HttpServlet {
	private static final String STRIPE_SECRET_KEY = "sk_test_51NHoftHrRFi94qYrcY4Yxv3NiAwj1ea5D7zuxCZtQ0kT9beLlwCh8GbjFKSgPz3s9K8QJ0U5mjet6H8vRL4VZBLq001eDbwLUL";
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public checkoutPage() {
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

			userID = validateUserID(connection, userID);
			if (userID == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
				return;
			}
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

	private List<Book> getCheckoutItems(Connection connection, String userID,
			List<Map<String, Object>> checkoutItemsList) throws SQLException {
		List<Book> checkoutItems = new ArrayList<>();

		for (Map<String, Object> itemMap : checkoutItemsList) {
			String bookID = (String) itemMap.get("bookID");
			int quantity = (int) itemMap.get("quantity");

			String simpleProc = "{call getBookDetails(?)}";
			CallableStatement cs = connection.prepareCall(simpleProc);
			cs.setString(1, bookID);
			cs.execute();
			ResultSet resultSetForBookDetails = cs.getResultSet();

			if (resultSetForBookDetails.next()) {
				Book book = new Book(resultSetForBookDetails.getString("book_id"),
						resultSetForBookDetails.getString("ISBN"), resultSetForBookDetails.getString("title"),
						resultSetForBookDetails.getString("authorName"),
						resultSetForBookDetails.getString("publisherName"),
						resultSetForBookDetails.getString("publication_date"),
						resultSetForBookDetails.getString("description"),
						resultSetForBookDetails.getString("genre_name"), resultSetForBookDetails.getString("img"),
						resultSetForBookDetails.getInt("sold"), resultSetForBookDetails.getInt("inventory"),
						resultSetForBookDetails.getDouble("price"), quantity,
						resultSetForBookDetails.getDouble("average_rating"));
				checkoutItems.add(book);
			}
		}

		return checkoutItems;
	}

	private String extractBookID(String itemString) {
		Pattern pattern = Pattern.compile("bookID:\\s*\"(\\d+)\"");
		Matcher matcher = pattern.matcher(itemString);

		if (matcher.find()) {
			return matcher.group(1);
		}

		return null;
	}

	private int extractQuantity(String itemString) {
		Pattern pattern = Pattern.compile("quantity:\\s*(\\d+)");
		Matcher matcher = pattern.matcher(itemString);

		if (matcher.find()) {
			return Integer.parseInt(matcher.group(1));
		}

		return 0;
	}

	private String uuidGenerator() {
		UUID uuid = UUID.randomUUID();
		return (uuid.toString());
	}
	private String insertTransactionHistory(Connection connection, double subtotal, String custID, String address)
	        throws SQLException {
	    String transactionHistoryUUID = uuidGenerator();
	    
	    String sql = "INSERT INTO transaction_history (transaction_historyID, transactionDate, subtotal, custID, address) VALUES (?, ?, ?, ?, ?)";
	    String transactionDate = getCurrentDateTime();
	    PreparedStatement statement = connection.prepareStatement(sql);
	    
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
	}
	
	private String getCurrentDateTime() {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date currentDate = new Date();
	    return dateFormat.format(currentDate);
	}

	private Boolean insertTransactionHistoryItems(Connection connection, List<Book> checkoutItems, String transactionHistoryUUID) throws SQLException {
	    Boolean success = true;
	    
	    String sql = "INSERT INTO transaction_history_items (transaction_historyID, transaction_history_itemID, bookID, Qty) VALUES (?, ?, ?, ?)";

	    PreparedStatement statement = connection.prepareStatement(sql);
	    
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
	    
	    statement.close();
	    
	    return success;
	}


	private int deleteFromCart(Connection connection, List<Book> checkoutItems, String custID) throws SQLException {
	    int count = 0;
	    
	    String cartID = getCartID(connection, custID);
	    
	    if (cartID != null) {
	        for (Book book : checkoutItems) {
	            String deleteQuery = "DELETE FROM cart_items WHERE cartID=? AND BookID=?";
	            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
	            deleteStatement.setString(1, cartID);
	            deleteStatement.setString(2, book.getBookID());
	            
	            int rowsDeleted = deleteStatement.executeUpdate();
	            count += rowsDeleted;
	            
	            deleteStatement.close();
	        }
	    }
	    
	    return count;
	}
	private String getCartID(Connection connection, String customerID) throws SQLException {
	    String cartID = null;
	    
	    String selectQuery = "SELECT cartID FROM cart WHERE custID=?";
	    PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
	    selectStatement.setString(1, customerID);
	    
	    ResultSet resultSet = selectStatement.executeQuery();
	    
	    if (resultSet.next()) {
	        cartID = resultSet.getString("cartID");
	    }
	    
	    resultSet.close();
	    selectStatement.close();
	    
	    return cartID;
	}


	protected void paymentIntent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Stripe.apiKey = STRIPE_SECRET_KEY;

		String paymentMethodId = request.getParameter("paymentMethodId");
		String subtotal = request.getParameter("subtotal");
		String address = request.getParameter("address");
		
		String userID = (String) request.getSession().getAttribute("userID");
		double amountInDollars = Double.parseDouble(subtotal);
		long amount = Math.round(amountInDollars * 100);
		Cookie[] cookies = request.getCookies();
		List<Book> checkoutItems = new ArrayList<>();
		String checkoutItemsString = null;
		List<Map<String, Object>> checkoutItemsArrayString = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
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
			System.out.println(address);
			System.out.println(checkoutItemsArrayString.size());
			System.out.println(userID);
			if (userID != null && address != null || checkoutItemsArrayString.size()!=0) {
				try {
					checkoutItems = getCheckoutItems(connection, userID, checkoutItemsArrayString);

					PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder().setCurrency("sgd")
							.setAmount(amount).setPaymentMethod(paymentMethodId).setConfirm(true).build();

					PaymentIntent paymentIntent = PaymentIntent.create(createParams);

					if (paymentIntent != null && paymentIntent.getStatus().equals("succeeded")) {
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
									response.sendRedirect("paymentError?userIDAvailable=true");
									System.out.println("a");
								} else {
									clearCheckoutItemsCookie(response);
									response.sendRedirect("paymentError?error=RefundFailed&userIDAvailable=true");
								}
							} else {
								deleteFromCart(connection, checkoutItems, userID);
								clearCheckoutItemsCookie(response);
								response.sendRedirect("paymentSuccess?userIDAvailable=true");
							}
						} else {
							RefundCreateParams refundParams = new RefundCreateParams.Builder()
									.setPaymentIntent(paymentIntent.getId()).build();

							Refund refund = Refund.create(refundParams);

							if (refund.getStatus().equals("succeeded")) {
								clearCheckoutItemsCookie(response);
								response.sendRedirect("paymentError?userIDAvailable=true");
								System.out.println("b");
							} else {
								clearCheckoutItemsCookie(response);
								response.sendRedirect("paymentError?error=RefundFailed&userIDAvailable=true");
							}
						}
					} else {
						if (paymentIntent != null) {
							RefundCreateParams refundParams = new RefundCreateParams.Builder()
									.setPaymentIntent(paymentIntent.getId()).build();

							Refund refund = Refund.create(refundParams);

							if (refund.getStatus().equals("succeeded")) {
								clearCheckoutItemsCookie(response);
								response.sendRedirect("paymentError?userIDAvailable=true");
								System.out.println("c");
							} else {
								clearCheckoutItemsCookie(response);
								response.sendRedirect("paymentError?error=RefundFailed&userIDAvailable=true");
								System.out.println("d");
							}
						} else {
							clearCheckoutItemsCookie(response);
							response.sendRedirect("paymentError?userIDAvailable=true");
							System.out.println("e");
						}
					}
				} catch (StripeException e) {
					System.err.println("Error: " + e.getMessage());
					clearCheckoutItemsCookie(response);
					response.sendRedirect("paymentError?userIDAvailable=true");
					System.out.println("f");
				}
			} else {
				clearCheckoutItemsCookie(response);
				response.sendRedirect("paymentError?userIDAvailable=true");
				System.out.println("g");
			}

		} catch (SQLException e1) {
			System.err.println("Error: " + e1.getMessage());
			clearCheckoutItemsCookie(response);
			response.sendRedirect("paymentError?userIDAvailable=true");
			System.out.println("h");
		}
	}

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
		// TODO Auto-generated method stub

		String action = request.getParameter("action");
		if (action != null && action.equals("payment")) {
			paymentIntent(request, response);
		} else {
			doGet(request, response);
		}
	}

}
