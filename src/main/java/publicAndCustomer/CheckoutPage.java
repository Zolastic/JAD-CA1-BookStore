package publicAndCustomer;

import java.io.IOException;
import java.sql.*;
import java.util.*;
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
import model.Address;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.DBConnection;
import dao.VerifyUserDAO;
import dao.CheckoutDAO;
import dao.AddressDAO;

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
	private CheckoutDAO checkoutDAO = new CheckoutDAO();
	private AddressDAO addressDAO = new AddressDAO();
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
			List<Address> addresses = new ArrayList<>();
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
			checkoutItems = checkoutDAO.getCheckoutItems(connection, userID, checkoutItemsArrayString);
			addresses = addressDAO.getAddressByUserId(connection, userID);
			
			connection.close();
			request.setAttribute("checkoutItems", checkoutItems);
			request.setAttribute("addresses", addresses);
			request.setAttribute("validatedUserID", userID);
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/checkoutPage.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/cartPage.jsp");
			dispatcher.forward(request, response);
			System.err.println("Error: \" + e);\r\n");
		}
	}


	// Payment intent for stripe
	protected void paymentIntent(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Stripe.apiKey = STRIPE_SECRET_KEY;
		String paymentMethodId = request.getParameter("paymentMethodId");
		String addr_id = request.getParameter("addr_id");
		String totalAmount = request.getParameter("totalAmount");
		String userID = (String) request.getSession().getAttribute("userID");
		double amountInDollars = Double.parseDouble(totalAmount);
		// Stripe needs to take totalAmount in cents
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
			if (userID != null && addr_id != null&& checkoutItemsArrayString.size() != 0) {
				try {
					// get the Book Class version of checkout items
					checkoutItems = checkoutDAO.getCheckoutItems(connection, userID, checkoutItemsArrayString);
					// Create payment intent's parameters
					PaymentIntentCreateParams createParams = new PaymentIntentCreateParams.Builder().setCurrency("sgd")
							.setAmount(amount).setPaymentMethod(paymentMethodId).setConfirm(true).build();
					// Create payment intent
					PaymentIntent paymentIntent = PaymentIntent.create(createParams);
					// if payment succeeded do the necessary insertion
					if (paymentIntent != null && paymentIntent.getStatus().equals("succeeded")) {
						// If insertion of transaction history or transaction history items failed do a
						// refund
						String transactionHistoryUUID = checkoutDAO.insertTransactionHistory(connection, amountInDollars, userID,
								addr_id, paymentIntent.getId());
						if (transactionHistoryUUID != null) {
							Boolean success = checkoutDAO.insertTransactionHistoryItems(connection, checkoutItems,
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
								checkoutDAO.deleteFromCart(connection, checkoutItems, userID);
								checkoutDAO.updateBooks(connection, checkoutItems);
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