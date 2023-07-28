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

import org.apache.commons.fileupload.FileUploadException;

import dao.GenreDAO;
import utils.CloudinaryUtil;
import utils.DBConnection;
import utils.DispatchUtil;
import utils.HttpServletRequestUploadWrapper;

/**
 * Servlet implementation class AddGenreServlet
 */
@WebServlet("/admin/AddGenre")
public class AddGenreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GenreDAO genreDAO = new GenreDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			HttpServletRequestUploadWrapper requestWrapper = new HttpServletRequestUploadWrapper(request);

			String genreName = requestWrapper.getParameter("name");
			byte[] imageInByte = requestWrapper.getBytesParameter("image");

			SimpleEntry<String, String> imageResult = imageInByte.length > 0 ? CloudinaryUtil.uploadImage(imageInByte) : null;
			String imageURL = null;
			String imagePublicID = null;
			
			if (imageResult != null) {
				imageURL = imageResult.getKey();
				imagePublicID = imageResult.getValue();
			}

			int statusCode = genreDAO.addGenre(connection, genreName, imageURL, imagePublicID);

			DispatchUtil.dispatch(request, response, "addGenre.jsp?statusCode=" + statusCode);

		} catch (SQLException | FileUploadException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "addGenre.jsp?statusCode=500");
		}
	}

}
