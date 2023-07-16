package publicAndCustomer;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import model.Address;

import dao.VerifyUserDAO;
import dao.AddressDAO;
import utils.DBConnection;

/**
 * Servlet implementation class ModifyAddressPage
 */
@WebServlet("/ModifyAddressPage")
public class ModifyAddressPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	private AddressDAO addressDAO = new AddressDAO();

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
			List<Address> addresses = new ArrayList<>();

			userID = verifyUserDAO.validateUserID(connection, userID);
			if (userID == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("/publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
				return;
			}

			addresses = addressDAO.getAddressByUserId(connection, userID);

			connection.close();
			request.setAttribute("addresses", addresses);
			request.setAttribute("validatedUserID", userID);
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/modifyAddressPage.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/modifyAddressPage.jsp");
			dispatcher.forward(request, response);
			System.err.println("Error: \" + e);\r\n");
		}
	}

	// Handle delete cart items
	protected void deleteAddressAction(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    String addr_id = request.getParameter("addr_id");
	    String pageBack = request.getParameter("from");
	    if (addr_id == null || pageBack == null) {
	        response.sendRedirect("/CA1-assignment/ModifyAddressPage?userIDAvailable=true&from=" + pageBack + "&deleteError=true");
	    } else {
	        try (Connection connection = DBConnection.getConnection()) {
	            boolean deleteSuccess = addressDAO.deleteAddr(addr_id);
	            if (deleteSuccess) {
	                response.sendRedirect("/CA1-assignment/ModifyAddressPage?userIDAvailable=true&from=" + pageBack);
	            } else {
	                response.sendRedirect("/CA1-assignment/ModifyAddressPage?userIDAvailable=true&from=" + pageBack + "&deleteError=true");
	            }
	        } catch (SQLException e) {
	            System.err.println("Error: " + e);
	            if (e instanceof SQLIntegrityConstraintViolationException) {
	                response.sendRedirect("/CA1-assignment/ModifyAddressPage?userIDAvailable=true&from=" + pageBack + "&deleteError=fkConstraint");
	            } else {
	                response.sendRedirect("/CA1-assignment/ModifyAddressPage?userIDAvailable=true&from=" + pageBack + "&deleteError=otherError");
	            }
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
		if (action != null && action.equals("deleteAddress")) {
			deleteAddressAction(request, response);
		} else {
			doGet(request, response);
		}
		
	}

}
