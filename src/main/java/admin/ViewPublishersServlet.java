package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
 * Servlet implementation class ViewPublishersServlet
 */
@WebServlet("/admin/ViewPublishers")
public class ViewPublishersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PublisherDAO publisherDAO = new PublisherDAO();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {

			loadData(request, connection);
			DispatchUtil.dispatch(request, response, "viewPublishers.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection) throws SQLException {
		String userInput = request.getParameter("userInput");
		List<Publisher> publishers = publisherDAO.searchPublishers(connection, userInput);
		request.setAttribute("publishers", publishers);
	}
 

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
