package publicAndCustomer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import model.User;
import utils.DBConnection;

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
		try (Connection connection = DBConnection.getConnection()) {
			String userID = request.getParameter("userID");
			loadData(request, connection, userID);
			request.getRequestDispatcher("publicAndCustomer/editProfile.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}
	
	private void loadData(HttpServletRequest request, Connection connection, String userID) throws SQLException {
		User user = getUserInfo(connection, userID);
		request.setAttribute("user", user);
	}
	
	private User getUserInfo(Connection connection, String userID) throws SQLException {
		String sqlStr = "SELECT * FROM users WHERE userID = ?;\r\n";
		try (Statement statement = connection.createStatement();
				PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, userID);

			ResultSet resultSet = ps.executeQuery();

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String password = resultSet.getString("password");
				String role = resultSet.getString("role");
				String img = resultSet.getString("img");
				resultSet.close();
				User user = new User(userID, name, email, password, role, img);
				return user;
			}

			throw new RuntimeException("User not found!!! userID: " + userID);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sqlStr = "UPDATE users SET name = ?, email = ?, img = ? WHERE userID = ?;";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			// Create a factory for disk-based file items
	        FileItemFactory factory = new DiskFileItemFactory();

	        // Create a new file upload handler
	        ServletFileUpload upload = new ServletFileUpload(factory);
	        List<FileItem> items = upload.parseRequest(request);
	        
	        String userID = getParameter(items, "userID");
	        String name = getParameter(items, "name");
	        String email = getParameter(items, "email");
	        String image = getBase64Parameter(items, "image");
	        System.out.println("image: " + image);
			
			ps.setString(1, name);
			ps.setString(2, email);
			ps.setString(3, image);
			ps.setString(4, userID);

			int affectedRows = ps.executeUpdate();
			System.out.printf("affectedRows: " + affectedRows);
			// Load data for page
			loadData(request, connection, userID);

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
	
	private String getParameter(List<FileItem> items, String name) {
		String value = items.stream().filter(item -> item.getFieldName().equals(name)).findFirst()
			.map(item -> item.getString())
			.orElse(null);
		
		return value;
	}
	
	private String getBase64Parameter(List<FileItem> items, String name) throws IOException {
		FileItem fileItem = items.stream().filter(item -> item.getFieldName().equals(name))
				.findFirst()
				.orElse(null);

		if (fileItem == null) {
			return null;
		}

		byte[] bytes = IOUtils.toByteArray(fileItem.getInputStream());
	    byte[] encodedBytes = Base64.getEncoder().encode(bytes);
	    String base64String = new String(encodedBytes);
	    return base64String;
	}

}
