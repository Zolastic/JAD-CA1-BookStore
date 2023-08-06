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
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA2
 */

@WebServlet("/CheckoutPage")

public class CheckoutPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	private CheckoutDAO checkoutDAO = new CheckoutDAO();
	private AddressDAO addressDAO = new AddressDAO();

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


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Check action
			doGet(request, response);
	}

}