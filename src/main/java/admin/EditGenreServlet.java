package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.AbstractMap.SimpleEntry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.GenreDAO;
import model.Genre;
import utils.CloudinaryUtil;
import utils.DBConnection;
import utils.DispatchUtil;
import utils.HttpServletRequestUploadWrapper;

/**
 * Servlet implementation class EditGenreServlet
 */
@WebServlet("/admin/EditGenre")
public class EditGenreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GenreDAO genreDAO = new GenreDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String genreID = request.getParameter("genreID");
			loadData(request, connection, genreID);
			request.getRequestDispatcher("editGenre.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection, String genreID) throws SQLException {
		Genre genre = genreDAO.getGenreById(connection, genreID);
		request.setAttribute("genre", genre);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String genreID = null;
		try (Connection connection = DBConnection.getConnection()) {
			HttpServletRequestUploadWrapper requestWrapper = new HttpServletRequestUploadWrapper(request);

			genreID = requestWrapper.getParameter("genreID");
			String genreName = requestWrapper.getParameter("name");
			byte[] imageInByte = requestWrapper.getBytesParameter("image");

			SimpleEntry<String, String> imageResult = imageInByte.length > 0 ? CloudinaryUtil.uploadImage(imageInByte) : null;
			String imageURL = null;
			String imagePublicID = null;
			
			if (imageResult != null) {
				imageURL = imageResult.getKey();
				imagePublicID = imageResult.getValue();
			}
			
			int statusCode = genreDAO.updateGenre(connection, genreID, genreName, imageURL, imagePublicID);
			// Load data for page
			loadData(request, connection, genreID);

			DispatchUtil.dispatch(request, response, "editGenre.jsp?statusCode=" + statusCode + "&genreID=" + genreID);
		} catch (Exception e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "editGenre.jsp?statusCode=500&genreID=" + genreID);
		}
	}

}
