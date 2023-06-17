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

import dao.BookDAO;
import dao.PublisherDAO;
import model.Book;
import model.Publisher;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class PublisherDetailsServlet
 */
@WebServlet("/admin/PublisherDetails")
public class PublisherDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private BookDAO bookDAO = new BookDAO();
	private PublisherDAO publisherDAO = new PublisherDAO();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String publisherID = request.getParameter("publisherID");
			loadData(request, connection, publisherID);
			DispatchUtil.dispatch(request, response, "publisherDetails.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}
	
	private void loadData(HttpServletRequest request, Connection connection, String publisherID) throws SQLException {
		List<Book> books = bookDAO.getBooksByPublisherID(connection, publisherID);
		Publisher publisher = publisherDAO.getPublisher(connection, publisherID);
		request.setAttribute("publisher", publisher);
		request.setAttribute("books", books);
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
