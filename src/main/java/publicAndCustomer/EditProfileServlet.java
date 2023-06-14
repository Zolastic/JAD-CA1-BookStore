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
import utils.HttpServletRequestUploadWrapper;

/**
 * Servlet implementation class EditProfilePage
 */
@WebServlet("/EditProfile")
public class EditProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
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
		request.getRequestDispatcher("publicAndCustomer/editProfile.jsp").forward(request, response);
	}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sql = "UPDATE users SET name = ?, email = ?, img = ? WHERE userID = ?;";
		String sqlWithoutImage = "UPDATE users SET name = ?, email = ? WHERE userID = ?;";

		try (Connection connection = DBConnection.getConnection()) {
			HttpServletRequestUploadWrapper requestWrapper = new HttpServletRequestUploadWrapper(request);
	        String userID = requestWrapper.getParameter("userID");
	        String name = requestWrapper.getParameter("name");
	        String email = requestWrapper.getParameter("email");
	        String image = requestWrapper.getBase64Parameter("image");
	        System.out.println("image: " + image);
			boolean noImage = image == null;
			
	        String sqlUpdate = noImage ? sqlWithoutImage : sql;
	        
	        PreparedStatement ps = connection.prepareStatement(sqlUpdate);
	        		
			ps.setString(1, name);
			ps.setString(2, email);
			if (noImage) {
				ps.setString(3, userID);
			} else {
				ps.setString(3, image);
				ps.setString(4, userID);
			}

			int affectedRows = ps.executeUpdate();
			System.out.printf("affectedRows: " + affectedRows);
			// Load data for page
			loadData(request, response, connection, userID);

			if (affectedRows > 0) {
				RequestDispatcher success = request.getRequestDispatcher("publicAndCustomer/editProfile.jsp?userID=" + userID);
				success.forward(request, response);
			} else {
				RequestDispatcher error = request
						.getRequestDispatcher("publicAndCustomer/editProfile.jsp?errCode=400&userID=" + userID);
				error.forward(request, response);
			}

			System.out.println("Woots");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
