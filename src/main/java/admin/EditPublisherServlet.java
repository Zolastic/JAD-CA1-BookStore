package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.PublisherDAO;
import model.Publisher;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class EditPublisher
 */
@WebServlet("/admin/EditPublisher")
public class EditPublisherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PublisherDAO publisherDAO = new PublisherDAO();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String publisherID = request.getParameter("publisherID");
			loadData(request, connection, publisherID);
			request.getRequestDispatcher("editPublisher.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection, String publisherID) throws SQLException {
		Publisher publisher = publisherDAO.getPublisherById(connection, publisherID);
		request.setAttribute("publisher", publisher);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String publisherID = request.getParameter("publisherID");
		String publisherName = request.getParameter("name");


		try (Connection connection = DBConnection.getConnection()) {

			int statusCode = publisherDAO.updatePublisher(connection, publisherID, publisherName);

			loadData(request, connection, publisherID);

			DispatchUtil.dispatch(request, response, "editPublisher.jsp?statusCode=" + statusCode + "&ppublisherID=" + publisherID);
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "editPublisher.jsp?statusCode=500&ppublisherID=" + publisherID);
		}
	}

}
