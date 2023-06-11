package publicAndCustomer;

import java.io.IOException;
import java.sql.*;
import java.util.*;
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
import com.stripe.param.PaymentIntentCreateParams;
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
		if(userIDAvailable!=null) {
			if(userIDAvailable.equals("true")) {
				userID=(String) request.getSession().getAttribute("userID");
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
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/login.jsp");
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

	private List<Book> getCheckoutItems(Connection connection, String userID, List<Map<String, Object>> checkoutItemsList)
	        throws SQLException {
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

	protected void paymentIntent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Stripe.apiKey = STRIPE_SECRET_KEY;

		String paymentMethodId = request.getParameter("paymentMethodId");
		String subtotal = request.getParameter("subtotal");
		double amountInDollars = Double.parseDouble(subtotal);
		long amount = Math.round(amountInDollars * 100);

		try {

			PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder().setCurrency("sgd")
					.setAmount(amount).setPaymentMethod(paymentMethodId).setConfirm(true).build();

			PaymentIntent paymentIntent = PaymentIntent.create(createParams);

			// Check if the payment was successful
			if (paymentIntent != null && paymentIntent.getStatus().equals("succeeded")) {
				// Redirect to the payment success page
				response.sendRedirect("paymentSuccess.jsp");
			} else {
				// Payment failed or not processed yet, no refund needed
				response.sendRedirect("paymentError.jsp");
			}
		} catch (StripeException e) {
			e.printStackTrace();
			response.getWriter().println("Error: " + e.getMessage());
		}
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
