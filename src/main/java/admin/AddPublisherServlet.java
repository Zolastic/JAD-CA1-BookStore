package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.DBConnection;

/**
 * Servlet implementation class AddPublisherServlet
 */
@WebServlet("/admin/AddPublisher")
public class AddPublisherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String publisherNmae = request.getParameter("name");

		String publisherExistencesqlStr = "SELECT * FROM publisher WHERE publisherName = ?";
		String addPublishersqlStr = "INSERT INTO publisher (publisherID, publisherName) VALUES (?, ?);";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement publisherExistencePS = connection.prepareStatement(publisherExistencesqlStr);
				PreparedStatement addPublisherPS = connection.prepareStatement(addPublishersqlStr);) {
			
			publisherExistencePS.setString(1, publisherNmae);
			ResultSet resultSet = publisherExistencePS.executeQuery();
			
			if (resultSet.next()) {
				RequestDispatcher error = request.getRequestDispatcher("addPublisher.jsp?statusCode=409");
				error.forward(request, response);
				return;
			}
			
			addPublisherPS.setString(1, (UUID.randomUUID()).toString());
			addPublisherPS.setString(2, publisherNmae);

			int affectedRows = addPublisherPS.executeUpdate();

			if (affectedRows > 0) {
				RequestDispatcher success = request.getRequestDispatcher("addPublisher.jsp?statusCode=200");
				success.forward(request, response);
			} else {
				RequestDispatcher error = request.getRequestDispatcher("addPublisher.jsp?statusCode=500");
				error.forward(request, response);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
