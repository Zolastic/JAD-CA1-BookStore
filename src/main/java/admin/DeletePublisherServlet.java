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

import utils.DBConnection;

/**
 * Servlet implementation class DeletePublisherServlet
 */
@WebServlet("/admin/DeletePublisher")
public class DeletePublisherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeletePublisherServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

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
		// TODO Auto-generated method stub
		String publisherID = request.getParameter("publisherID");
		String sqlStr = " DELETE FROM publisher WHERE publisherID = ?;";
		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, publisherID);

			int affectedRows = ps.executeUpdate();
			
			if (affectedRows > 0) {
				RequestDispatcher success = request.getRequestDispatcher("ViewPublishers");
				success.forward(request, response);
			} else {
				RequestDispatcher error = request.getRequestDispatcher("ViewPublishers?errCode=400");
				error.forward(request, response);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
