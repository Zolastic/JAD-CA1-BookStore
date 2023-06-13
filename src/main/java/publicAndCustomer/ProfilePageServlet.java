package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import model.User;
import utils.CustomerSessionValidation;
import utils.DBConnection;

/**
 * Servlet implementation class ProfilePage
 */
@WebServlet("/ProfilePage")
public class ProfilePageServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProfilePageServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		if (!CustomerSessionValidation.validate(request, response)) {
			return;
		}

		try (Connection connection = DBConnection.getConnection()) {
			String userID = request.getParameter("userID");
			loadData(request, response, connection, userID);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, HttpServletResponse response, Connection connection,
			String userID) throws SQLException, ServletException, IOException {
		User user = UserDAO.getUserInfo(connection, userID);

		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/publicAndCustomer/registrationPage.jsp");
			return;
		}
		request.setAttribute("user", user);
		request.getRequestDispatcher("publicAndCustomer/profilePage.jsp").forward(request, response);
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
