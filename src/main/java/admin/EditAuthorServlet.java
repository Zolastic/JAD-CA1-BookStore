package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Author;
import model.Book;
import model.Genre;
import model.Publisher;
import utils.DBConnection;

/**
 * Servlet implementation class EditAuthorServlet
 */
@WebServlet("/admin/EditAuthor")
public class EditAuthorServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public EditAuthorServlet() {
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
		try (Connection connection = DBConnection.getConnection()) {
			String authorID = request.getParameter("authorID");
			loadData(request, connection, authorID);
			request.getRequestDispatcher("editAuthor.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection, String authorID) throws SQLException {
		Author author = getAuthor(connection, authorID);
		request.setAttribute("author", author);
	}

	private Author getAuthor(Connection connection, String authorID) throws SQLException {
		String sqlStr = "SELECT * FROM author WHERE authorID = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, authorID);

			ResultSet resultSet = ps.executeQuery();

			while (resultSet.next()) {
				String authorName = resultSet.getString("authorName");
				Author author = new Author(authorID, authorName);
				return author;
			}

			throw new RuntimeException("Book not found!!! authorID: " + authorID);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String authorID = request.getParameter("authorID");
		String authorName = request.getParameter("name");

		String sqlStr = "UPDATE author SET authorName = ? WHERE authorID = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, authorName);
			ps.setString(2, authorID);

			int affectedRows = ps.executeUpdate();

			loadData(request, connection, authorID);

			if (affectedRows > 0) {
				RequestDispatcher success = request.getRequestDispatcher("editAuthor.jsp?authorID=" + authorID);
				success.forward(request, response);
			} else {
				RequestDispatcher error = request.getRequestDispatcher("editAuthor.jsp?errCode=400&authorID=" + authorID);
				error.forward(request, response);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
