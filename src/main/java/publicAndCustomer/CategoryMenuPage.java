package publicAndCustomer;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Book;
import model.Genre;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.DBConnection;
import dao.VerifyUserDAO;
/**
 * Servlet implementation class CategoryMenuPage
 */

/**
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA1
 */

@WebServlet("/CategoryMenuPage")
public class CategoryMenuPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CategoryMenuPage() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userIDAvailable = request.getParameter("userIDAvailable");
		String userID = null;
		if (userIDAvailable != null) {
			if (userIDAvailable.equals("true")) {
				userID = (String) request.getSession().getAttribute("userID");
			}
		}
		List<Genre> allGenre = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			// Validate the userID
			userID = verifyUserDAO.validateUserID(connection, userID);
			// Get all genre
			allGenre = getAllGenres(connection);
			connection.close();
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}
		request.setAttribute("allGenre", allGenre);
		request.setAttribute("validatedUserID", userID);
		RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/categoryMenuPage.jsp");
		dispatcher.forward(request, response);
	}

	// Get all the genre
	private List<Genre> getAllGenres(Connection connection) {
		List<Genre> allGenre = new ArrayList<>();
		try (Statement stmt = connection.createStatement()) {
			String sqlStr = "SELECT * FROM genre;";
			ResultSet rs = stmt.executeQuery(sqlStr);

			while (rs.next()) {
				Genre genre = new Genre(rs.getString("genre_id"), rs.getString("genre_name"),
						rs.getString("genre_img"));
				allGenre.add(genre);
			}
		} catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
		}

		return allGenre;
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
