package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.*;
import utils.DBConnection;

/**
 * Servlet implementation class AddBook
 */
@WebServlet("/AddBook")
public class AddBook extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			
			loadData(request, connection);
			request.getRequestDispatcher("admin/add-book.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection) throws SQLException {
		List<Genre> genres = getGenres(connection);
		List<Author> authors = getAuthors(connection);
		List<Publisher> publishers = getPublishers(connection);
		request.setAttribute("genres", genres);
		request.setAttribute("authors", authors);
		request.setAttribute("publishers", publishers);
	}
	
	private List<Genre> getGenres(Connection connection) throws SQLException {
		try (Statement genreStatement = connection.createStatement();
			 ResultSet genreResultSet = genreStatement
							.executeQuery("SELECT genre_id as genreId, genre_name as genreName FROM genre;");) {
				
				List<Genre> genres = new ArrayList<>();
				while (genreResultSet.next()) {
					int genreId = genreResultSet.getInt("genreId");
					String genreName = genreResultSet.getString("genreName");
					genres.add(new Genre(genreId, genreName));
				}
				
				return genres;
			} 
	}
	
	private List<Author> getAuthors(Connection connection) throws SQLException {
		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement
							.executeQuery("SELECT * FROM author;");) {
				
				List<Author> authors = new ArrayList<>();
				while (resultSet.next()) {
					int authorId = resultSet.getInt("authorID");
					String authorName = resultSet.getString("authorName");
					authors.add(new Author(authorId, authorName));
				}
				
				return authors;
			} 
	}
	
	private List<Publisher> getPublishers(Connection connection) throws SQLException {
		try (Statement statement = connection.createStatement();
			 ResultSet resultSet = statement
							.executeQuery("SELECT * FROM publisher;");) {
				
				List<Publisher> publishers = new ArrayList<>();
				while (resultSet.next()) {
					int publisherId = resultSet.getInt("publisherID");
					String publisherName = resultSet.getString("publisherName");
					publishers.add(new Publisher(publisherId, publisherName));
				}
				
				return publishers;
			} 
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String title = request.getParameter("title");
		int price = Integer.parseInt(request.getParameter("price"));
		int author = Integer.parseInt(request.getParameter("author"));
		int publisher = Integer.parseInt(request.getParameter("publisher"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		String pubDate = request.getParameter("date");
		String isbn = request.getParameter("isbn");
		String description = request.getParameter("description");
		int genreId = Integer.parseInt(request.getParameter("genre"));
		
		String sqlStr = "INSERT INTO BOOK (title, price, authorID, publisherID, Qty, publication_date, ISBN, description, genre_id, book_id)\r\n"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
		try (Connection conn = DBConnection.getConnection();
			 PreparedStatement ps = conn.prepareStatement(sqlStr)) {
			ps.setString(1, title);
			ps.setInt(2, price);
			ps.setInt(3, author);
			ps.setInt(4, publisher);
			ps.setInt(5, quantity);
			ps.setString(6, pubDate);
			ps.setString(7, isbn);
			ps.setString(8, description);
			ps.setInt(9, genreId);
			ps.setString(10, (UUID.randomUUID()).toString());
			
			int affectedRows = ps.executeUpdate();
			// Load data for page
			loadData(request, conn);
			
			if (affectedRows > 0) {
				RequestDispatcher success = request.getRequestDispatcher("admin/add-book.jsp");
				success.forward(request, response);
			} else {
				RequestDispatcher error = request.getRequestDispatcher("admin/add-book.jsp?errCode=400");
				error.forward(request, response);
			}

			System.out.println("Woots");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
