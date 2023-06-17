package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadException;

import dao.GenreDAO;
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
			String image = requestWrapper.getBase64Parameter("image");

			int statusCode = genreDAO.addGenre(connection, genreName, image);

			DispatchUtil.dispatch(request, response, "addGenre.jsp?statusCode=" + statusCode);
			
		} catch (SQLException | FileUploadException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "addGenre.jsp?statusCode=500" );
		}
	}

}
