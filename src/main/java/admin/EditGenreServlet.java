package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.GenreDAO;
import model.Genre;
import utils.DBConnection;
import utils.HttpServletRequestUploadWrapper;

/**
 * Servlet implementation class EditGenreServlet
 */
@WebServlet("/admin/EditGenre")
public class EditGenreServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private GenreDAO genreDAO = new GenreDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditGenreServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
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
		Genre genre = genreDAO.getGenre(connection, genreID);
		request.setAttribute("genre", genre);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sqlStrWithImage = "UPDATE genre SET genre_name = ?, genre_img = ? WHERE genre_id = ?;";

		String sqlStrWithoutImage = "UPDATE genre SET genre_name = ? WHERE genre_id = ?;";

		try (Connection connection = DBConnection.getConnection();) {
			HttpServletRequestUploadWrapper requestWrapper = new HttpServletRequestUploadWrapper(request);

			String genreID = requestWrapper.getParameter("genreID");
			String genreName = requestWrapper.getParameter("name");
			String image = requestWrapper.getBase64Parameter("image");
			boolean noImage = image == null;
			
			String sqlUpdate = noImage ? sqlStrWithoutImage : sqlStrWithImage;
			
			PreparedStatement ps = connection.prepareStatement(sqlUpdate);

			ps.setString(1, genreName);
			
			if (noImage) {
				ps.setString(2, genreID);
			} else {
				ps.setString(2, image);
				ps.setString(3, genreID);
			}


			int affectedRows = ps.executeUpdate();
			System.out.printf("affectedRows: " + affectedRows);
			// Load data for page
			loadData(request, connection, genreID);

			if (affectedRows > 0) {
				RequestDispatcher success = request.getRequestDispatcher("editGenre.jsp?statusCode=200&genreID=" + genreID);
				success.forward(request, response);
			} else {
				RequestDispatcher error = request.getRequestDispatcher("editGenre.jsp?statusCode=500&genreID=" + genreID);
				error.forward(request, response);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
