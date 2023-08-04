package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AddressDAO;
import dao.CountryDAO;
import dao.VerifyUserDAO;
import model.Address;
import model.Country;
import utils.DBConnection;

/**
 * Servlet implementation class AddAddressPage
 */
@WebServlet("/AddAddressPage")
public class AddAddressPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	private AddressDAO addressDAO = new AddressDAO();
	private CountryDAO countryDAO = new CountryDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String userIDAvailable = request.getParameter("userIDAvailable");
			String userID = null;
			if (userIDAvailable != null) {
				if (userIDAvailable.equals("true")) {
					userID = (String) request.getSession().getAttribute("userID");
				}
			}
			userID = verifyUserDAO.validateUserID(connection, userID);
			if (userID == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
				return;
			}
			List<Country> countries = countryDAO.getAllCountry(connection);
			connection.close();
			request.setAttribute("countries", countries);
			request.setAttribute("validatedUserID", userID);
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/addAddressPage.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("ModifyAddressPage");
			dispatcher.forward(request, response);
			System.err.println("Error: \" + e);\r\n");
		}
	}
	// Function to generate an uuid
	private String uuidGenerator() {
		UUID uuid = UUID.randomUUID();
		return (uuid.toString());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Insert New Address
		String unit_number = request.getParameter("unit_number");
		String block_number = request.getParameter("block_number");
		String street_address = request.getParameter("street_address");
		String postal_code = request.getParameter("postal_code");
		String countryInfo = request.getParameter("country");
		String userId = request.getParameter("userId");

		if (userId == null || unit_number == null || block_number == null || street_address == null
				|| postal_code == null || countryInfo == null || unit_number.isEmpty() || block_number.isEmpty()
				|| street_address.isEmpty() || postal_code.isEmpty() || countryInfo.isEmpty()) {
			String referer = request.getHeader("Referer");
			response.sendRedirect(referer + "&error=emptyInput");
		} else {
			String[] countryData = countryInfo.split(",");
			String countryId = countryData[0];
			String countryName = countryData[1];

			try (Connection connection = DBConnection.getConnection()) {
				String addr_id = uuidGenerator();
				Address addr = new Address(addr_id, unit_number, block_number, street_address, postal_code, countryId,
						countryName);
				int rowsAffected = addressDAO.insertNewAddress(connection, addr, userId);
				if (rowsAffected > 0) {
					String referer = request.getHeader("Referer");
					response.sendRedirect(referer + "&success=true");
				} else {
					String referer = request.getHeader("Referer");
					response.sendRedirect(referer + "&error=errInsert");
				}
			} catch (SQLException e) {
				System.err.println("Error: " + e);
				String referer = request.getHeader("Referer");
				response.sendRedirect(referer + "&error=conndbError");
			} catch (Exception e) {
				System.err.println("Error: " + e);
				String referer = request.getHeader("Referer");
				response.sendRedirect(referer + "&error=unexpectedError");
			}
		}

	}

}
