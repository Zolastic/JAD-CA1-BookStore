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

import org.apache.commons.fileupload.FileUploadException;

import utils.DBConnection;
import utils.HttpServletRequestUploadWrapper;

/**
 * Servlet implementation class AddGenreServlet
 */
@WebServlet("/admin/AddGenre")
public class AddGenreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddGenreServlet() {
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
		String genreExistenceSqlStr = "SELECT * FROM genre WHERE genre_name = ?";
		String addGenreSqlStr = "INSERT INTO genre (genre_id, genre_name, genre_img) VALUES (?, ?, ?);";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement genreExistencePS = connection.prepareStatement(genreExistenceSqlStr);
				PreparedStatement addGenrePS = connection.prepareStatement(addGenreSqlStr);) {
			HttpServletRequestUploadWrapper requestWrapper = new HttpServletRequestUploadWrapper(request);

			String genreName = requestWrapper.getParameter("name");
			String image = requestWrapper.getBase64Parameter("image");

			genreExistencePS.setString(1, genreName);
			ResultSet resultSet = genreExistencePS.executeQuery();

			if (resultSet.next()) {
				RequestDispatcher error = request.getRequestDispatcher("addGenre.jsp?statusCode=409");
				error.forward(request, response);
				return;
			}

			addGenrePS.setString(1, (UUID.randomUUID()).toString());
			addGenrePS.setString(2, genreName);
			addGenrePS.setString(3, image);

			int affectedRows = addGenrePS.executeUpdate();

			if (affectedRows > 0) {
				RequestDispatcher success = request.getRequestDispatcher("addGenre.jsp?statusCode=200");
				success.forward(request, response);
			} else {
				RequestDispatcher error = request.getRequestDispatcher("addGenre.jsp?statusCode=500");
				error.forward(request, response);
			}
		} catch (SQLException | FileUploadException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
