package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.AddressDAO;
import dao.UserDAO;
import model.Address;
import model.User;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class ViewUserByPostalCodeServlet
 */
@WebServlet("/admin/ViewUserByPostalCode")
public class ViewUserByPostalCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AddressDAO addressDAO = new AddressDAO();
	private UserDAO userDAO = new UserDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {

			loadData(request, connection);
			DispatchUtil.dispatch(request, response, "viewUsersOrderByPostalCode.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "viewUsers.jsp?statusCode=500");
		}
	}

	private void loadData(HttpServletRequest request, Connection connection) throws SQLException {
		ArrayList<Address> addresses = addressDAO.getAddressesOrderByPostalCode(connection);
		ArrayList<User> users = userDAO.getUserIDOrderByPostalCode(connection, addresses);
		request.setAttribute("addresses", addresses);
		request.setAttribute("users", users);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
