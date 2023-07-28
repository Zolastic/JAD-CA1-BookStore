package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.GenreDAO;
import utils.CloudinaryUtil;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class DeleteGenreServlet
 */
@WebServlet("/admin/DeleteGenre")
public class DeleteGenreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GenreDAO genreDAO = new GenreDAO();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String genreID = request.getParameter("genreID");
		try (Connection connection = DBConnection.getConnection()) {
			String imagePublicID = genreDAO.getGenreImagePublicID(connection, genreID);
			int statusCode = genreDAO.deleteGenre(connection, genreID);
			statusCode = CloudinaryUtil.deleteImageFromCld(imagePublicID);
			
			DispatchUtil.dispatch(request, response, "ViewGenres?errCode=" + statusCode);
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "ViewGenres?errCode=500");
		}
	}

}
