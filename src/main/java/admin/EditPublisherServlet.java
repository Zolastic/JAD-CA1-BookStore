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

import dao.PublisherDAO;
import model.Publisher;
import utils.DBConnection;

/**
 * Servlet implementation class EditPublisher
 */
@WebServlet("/admin/EditPublisher")
public class EditPublisherServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PublisherDAO publisherDAO = new PublisherDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditPublisherServlet() {
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
			String publisherID = request.getParameter("publisherID");
			loadData(request, connection, publisherID);
			request.getRequestDispatcher("editPublisher.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection, String publisherID) throws SQLException {
		Publisher publisher = publisherDAO.getPublisher(connection, publisherID);
		request.setAttribute("publisher", publisher);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String publisherID = request.getParameter("publisherID");
		String publisherName = request.getParameter("name");

		String sqlStr = "UPDATE publisher SET publisherName = ? WHERE publisherID = ?";

		try (Connection connection = DBConnection.getConnection();
				PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, publisherName);
			ps.setString(2, publisherID);

			int affectedRows = ps.executeUpdate();

			loadData(request, connection, publisherID);

			if (affectedRows > 0) {
				RequestDispatcher success = request.getRequestDispatcher("editPublisher.jsp?publisherID=" + publisherID);
				success.forward(request, response);
			} else {
				RequestDispatcher error = request.getRequestDispatcher("editPublisher.jsp?errCode=400&publisherID=" + publisherID);
				error.forward(request, response);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
