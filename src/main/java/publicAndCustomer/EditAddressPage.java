package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
 * Servlet implementation class EditAddressPage
 */
@WebServlet("/EditAddressPage")
public class EditAddressPage extends HttpServlet {
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
			String addr_id = request.getParameter("addr_id");
			String userID = null;
			if (userIDAvailable != null) {
				if (userIDAvailable.equals("true")) {
					userID = (String) request.getSession().getAttribute("userID");
				}
			}
			userID = verifyUserDAO.validateUserID(connection, userID);
			if (userID == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
				return;
			}
			if (addr_id == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/modifyAddressPage.jsp");
				dispatcher.forward(request, response);
				return;
			}
			Address addressDetails = addressDAO.getAddressByAddrId(connection, addr_id);
			List<Country> countries = countryDAO.getAllCountry(connection);
			connection.close();
			request.setAttribute("addressDetails", addressDetails);
			request.setAttribute("countries", countries);
			request.setAttribute("validatedUserID", userID);
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/editAddressPage.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("ModifyAddressPage");
			dispatcher.forward(request, response);
			System.err.println("Error: \" + e);\r\n");
		}
	}
	// Handle submit edit
	protected void submitEditAddress(HttpServletRequest request, HttpServletResponse response)
		    throws ServletException, IOException {
		System.out.println("hi im here");
		    String addr_id = request.getParameter("addr_id");
		    String unit_number = request.getParameter("unit_number");
		    String block_number = request.getParameter("block_number");
		    String street_address = request.getParameter("street_address");
		    String postal_code = request.getParameter("postal_code");
		    String countryInfo = request.getParameter("country");

		    if (addr_id == null || unit_number == null || block_number == null || street_address == null || postal_code == null || countryInfo == null) {
		        String referer = request.getHeader("Referer");
		        response.sendRedirect(referer + "&error=emptyInput" + "&addr_id=" + addr_id);
		    } else {
		        String[] countryData = countryInfo.split(",");
		        String countryId = countryData[0];
		        String countryName = countryData[1];

		        try (Connection connection = DBConnection.getConnection()) {
		            Address addr = new Address(addr_id, unit_number, block_number, street_address, postal_code, countryId, countryName);
		            int rowsAffected = addressDAO.editAddress(connection, addr);
		            if (rowsAffected > 0) {
		                String referer = request.getHeader("Referer");
		                response.sendRedirect(referer + "&addr_id=" + addr_id + "&success=true");
		            } else {
		                String referer = request.getHeader("Referer");
		                response.sendRedirect(referer + "&error=errEdit&addr_id=" + addr_id);
		            }
		        } catch (SQLException e) {
		            System.err.println("Error: " + e);
		            String referer = request.getHeader("Referer");
		            response.sendRedirect(referer + "&error=conndbError" + "&addr_id=" + addr_id);
		        }
		    }
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// check for actions
		String action = request.getParameter("action");
		if (action != null && action.equals("submitEdit")) {
			submitEditAddress(request, response);
		} else {
			doGet(request, response);
		}
	}
}
