package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import model.User;
import utils.DBConnection;
import utils.DispatchUtil;
import utils.HttpServletRequestUploadWrapper;

/**
 * Servlet implementation class EditProfilePage
 */
@WebServlet("/EditProfile")
public class EditProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO = new UserDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditProfileServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		if (session == null) {
			res.sendRedirect(req.getContextPath() + "/publicAndCustomer/registrationPage.jsp");
			return;
		}
		
		String userIDFromSession = (String) session.getAttribute("userID");
		if (userIDFromSession == null) {
			res.sendRedirect(req.getContextPath() + "/publicAndCustomer/registrationPage.jsp");
			return;
		}
		
		try (Connection connection = DBConnection.getConnection()) {
			String userID = request.getParameter("userID");
			loadData(request, response, connection, userID);
			DispatchUtil.dispatch(request, response, "publicAndCustomer/editProfile.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}
	
	private void loadData(HttpServletRequest request, HttpServletResponse response, Connection connection,
			String userID) throws SQLException, ServletException, IOException {
		User user = userDAO.getUserInfo(connection, userID);

		if (user == null) {
			DispatchUtil.dispatch(request, response, "/publicAndCustomer/registrationPage.jsp");
			return;
		}
		request.setAttribute("user", user);
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userID = null;
		try (Connection connection = DBConnection.getConnection()) {
			HttpServletRequestUploadWrapper requestWrapper = new HttpServletRequestUploadWrapper(request);
	        userID = requestWrapper.getParameter("userID");
	        String name = requestWrapper.getParameter("name");
	        String email = requestWrapper.getParameter("email");
	        String image = requestWrapper.getBase64Parameter("image");
		
			int statusCode = userDAO.updateUser(connection, name, email, image, userID);
			loadData(request, response, connection, userID);
			
			DispatchUtil.dispatch(request, response, "publicAndCustomer/editProfile.jsp?statusCode=" + statusCode + "&userID=" + userID);

		} catch (Exception e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "publicAndCustomer/editProfile.jsp?statusCode=500&userID=" + userID);
		}
	}
	
}
