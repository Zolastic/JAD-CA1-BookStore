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
import dao.UserAddressDAO;
import dao.UserDAO;
import model.UserAddress;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class ViewUserByPostalCodeServlet
 */
@WebServlet("/admin/ViewUserByPostalCode")
public class ViewUserOrderByPostalCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserAddressDAO userAddressDAO = new UserAddressDAO();

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
		ArrayList<UserAddress> users = userAddressDAO.getUserOrderByPostalCode(connection);
		request.setAttribute("addresses", users);
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
