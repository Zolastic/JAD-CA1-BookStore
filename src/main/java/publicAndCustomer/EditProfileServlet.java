package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import model.User;
import utils.CloudinaryUtil;
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
			User user = loadData(request, response, connection, userID);
			if (user == null) {
				DispatchUtil.dispatch(request, response, "publicAndCustomer/registrationPage.jsp");
				return;
			}
			DispatchUtil.dispatch(request, response, "publicAndCustomer/editProfile.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "home.jsp");
		}
	}
	
	private User loadData(HttpServletRequest request, HttpServletResponse response, Connection connection,
			String userID) throws SQLException, ServletException, IOException {
		User user = userDAO.getUserInfoByID(connection, userID);
		request.setAttribute("user", user);
		return user;	
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
	        byte[] imageInByte = requestWrapper.getBytesParameter("image");

	        SimpleEntry<String, String> imageResult = imageInByte.length > 0 ? CloudinaryUtil.uploadImage(imageInByte) : null;
			String imageURL = null;
			String imagePublicID = null;
			
			if (imageResult != null) {
				imageURL = imageResult.getKey();
				imagePublicID = imageResult.getValue();
			}

			if (imagePublicID == "error") {
				User user = loadData(request, response, connection, userID);
				DispatchUtil.dispatch(request, response, "addBook.jsp?statusCode=500");
				return;
			}
		
			int statusCode = userDAO.updateUser(connection, name, email, imageURL, imagePublicID, userID);
			
			User user = loadData(request, response, connection, userID);
			if (user == null) {
				DispatchUtil.dispatch(request, response, "publicAndCustomer/registrationPage.jsp");
				return;
			}
			
			DispatchUtil.dispatch(request, response, "publicAndCustomer/editProfile.jsp?statusCode=" + statusCode + "&userID=" + userID);

		} catch (Exception e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "publicAndCustomer/editProfile.jsp?statusCode=500&userID=" + userID);
		}
	}
	
}
