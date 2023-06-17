package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.PublisherDAO;
import model.Book;
import model.Publisher;
import utils.DBConnection;

/**
 * Servlet implementation class PublisherDetailsServlet
 */
@WebServlet("/admin/PublisherDetails")
public class PublisherDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private PublisherDAO publisherDAO = new PublisherDAO();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PublisherDetailsServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		try (Connection connection = DBConnection.getConnection()) {
			String publisherID = request.getParameter("publisherID");
			loadData(request, connection, publisherID);
			request.getRequestDispatcher("publisherDetails.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}
	
	private void loadData(HttpServletRequest request, Connection connection, String publisherID) throws SQLException {
		List<Book> books = getBooks(connection, publisherID);
		Publisher publisher = publisherDAO.getPublisher(connection, publisherID);
		request.setAttribute("publisher", publisher);
		request.setAttribute("books", books);
	}
	
	private List<Book> getBooks(Connection connection, String publisherID) throws SQLException {
		String sqlStr = "SELECT book_id as bookID, title FROM book\r\n"
				+ "JOIN publisher ON book.publisherID = publisher.publisherID\r\n"
				+ "WHERE publisher.publisherID = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, publisherID);

			ResultSet resultSet = ps.executeQuery();

			List<Book> books = new ArrayList<>();
			while (resultSet.next()) {
				String bookID = resultSet.getString("bookID");
				String title = resultSet.getString("title");
				books.add(new Book(bookID, title));
			}
			resultSet.close();
			return books;
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
