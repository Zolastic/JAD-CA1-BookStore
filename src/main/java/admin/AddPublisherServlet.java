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

import dao.PublisherDAO;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class AddPublisherServlet
 */
@WebServlet("/admin/AddPublisher")
public class AddPublisherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PublisherDAO publisherDAO = new PublisherDAO();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String publisherNmae = request.getParameter("name");

		try (Connection connection = DBConnection.getConnection()) {
			int statusCode = publisherDAO.addPublisher(connection, publisherNmae);

			DispatchUtil.dispatch(request, response, "addPublisher.jsp?statusCode=" + statusCode);
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "addPublisher.jsp?statusCode=500");
		}
	}

}
