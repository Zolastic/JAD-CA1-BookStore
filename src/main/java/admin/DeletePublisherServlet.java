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
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class DeletePublisherServlet
 */
@WebServlet("/admin/DeletePublisher")
public class DeletePublisherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PublisherDAO publisherDAO = new PublisherDAO();
       
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
		String publisherID = request.getParameter("publisherID");
		try (Connection connection = DBConnection.getConnection()) {
			int statusCode = publisherDAO.deletePublisher(connection, publisherID);
			
			DispatchUtil.dispatch(request, response, "ViewPublishers?errCode=" + statusCode);
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "ViewPublishers?errCode=500");
		}
	}

}
