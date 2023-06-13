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
 * Servlet implementation class AddAuthorServlet
 */
@WebServlet("/admin/AddAuthor")
public class AddAuthorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddAuthorServlet() {
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
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String authorNmae = request.getParameter("name");

		String authorExistencesqlStr = "SELECT * FROM author WHERE authorName = ?";
		String addAuthorsqlStr = "INSERT INTO author (authorID, authorName) VALUES (?, ?);";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement authorExistencePS = connection.prepareStatement(authorExistencesqlStr);
				PreparedStatement addAuthorPS = connection.prepareStatement(addAuthorsqlStr);) {
			
			authorExistencePS.setString(1, authorNmae);
			ResultSet resultSet = authorExistencePS.executeQuery();
			
			if (resultSet.next()) {
				RequestDispatcher error = request.getRequestDispatcher("addAuthor.jsp?statusCode=409");
				error.forward(request, response);
				return;
			}
			
			addAuthorPS.setString(1, (UUID.randomUUID()).toString());
			addAuthorPS.setString(2, authorNmae);

			int affectedRows = addAuthorPS.executeUpdate();

			if (affectedRows > 0) {
				RequestDispatcher success = request.getRequestDispatcher("addAuthor.jsp?statusCode=200");
				success.forward(request, response);
			} else {
				RequestDispatcher error = request.getRequestDispatcher("addAuthor.jsp?statusCode=500");
				error.forward(request, response);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
