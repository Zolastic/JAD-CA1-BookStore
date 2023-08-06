package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AddressDAO;
import dao.VerifyUserDAO;
import utils.DBConnection;

/**
 * Servlet implementation class DeleteAddress
 */
@WebServlet("/DeleteAddress")
public class DeleteAddress extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AddressDAO addressDAO = new AddressDAO();
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userID = (String) request.getSession().getAttribute("userID");
		String addr_id = request.getParameter("addr_id");
		String pageBack = request.getParameter("from");
		if (addr_id == null || pageBack == null) {
			response.sendRedirect(request.getContextPath() + "/ModifyAddressPage?userIDAvailable=true&from=" + pageBack
					+ "&deleteError=true");
		} else {
			try (Connection connection = DBConnection.getConnection()) {
				userID = verifyUserDAO.validateUserID(connection, userID);
				if (userID == null) {
					RequestDispatcher dispatcher = request.getRequestDispatcher("/publicAndCustomer/registrationPage.jsp");
					dispatcher.forward(request, response);
					return;
				}
				boolean deleteSuccess = addressDAO.deleteAddr(addr_id);
				if (deleteSuccess) {
					response.sendRedirect(
							request.getContextPath() + "/ModifyAddressPage?userIDAvailable=true&from=" + pageBack);
				} else {
					response.sendRedirect(request.getContextPath() + "/ModifyAddressPage?userIDAvailable=true&from="
							+ pageBack + "&deleteError=true");
				}
			} catch (SQLException e) {
				System.err.println("Error: " + e);

				response.sendRedirect(request.getContextPath() + "/ModifyAddressPage?userIDAvailable=true&from="
						+ pageBack + "&deleteError=otherError");

			}
		}
	}

}
