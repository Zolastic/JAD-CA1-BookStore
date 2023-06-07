package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.*;
import utils.DBConnection;

/**
 * Servlet implementation class viewBooks
 */
@WebServlet("/viewBooks")
public class ViewBooks extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ViewBooks() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		try (Connection connection = DBConnection.getConnection()) {

			loadData(request, connection);
			request.getRequestDispatcher("admin/viewBooks.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection) throws SQLException {
		List<Book> books = getBooks(connection);
		request.setAttribute("books", books);
	}

	private List<Book> getBooks(Connection connection) throws SQLException {
		try (Statement genreStatement = connection.createStatement();
				ResultSet genreResultSet = genreStatement.executeQuery("SELECT * FROM book;");) {

			List<Book> books = new ArrayList<>();
			while (genreResultSet.next()) {
				int bookID = genreResultSet.getInt("book_id");
				String isbn = genreResultSet.getString("isbn");
				String title = genreResultSet.getString("title");
				String author = genreResultSet.getString("authorID");
				String publisher = genreResultSet.getString("publisherID");
				String publication_date = genreResultSet.getString("publication_date");
				String description = genreResultSet.getString("description");
				String img = genreResultSet.getString("img");
				int genreID = genreResultSet.getInt("genre_id");
				int sold = genreResultSet.getInt("sold");
				int inventory = genreResultSet.getInt("inventory");
				double price = genreResultSet.getInt("price");
				books.add(new Book(bookID, isbn, title, author, publisher, publication_date, description, genreID, img,
						sold, inventory, price));
			}

			return books;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
