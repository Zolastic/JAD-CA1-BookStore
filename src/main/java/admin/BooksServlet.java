package admin;

import utils.DBConnection;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * Servlet implementation class BooksServlet
 */
@WebServlet("/BooksServlet")
public class BooksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BooksServlet() {
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
		
		String title = request.getParameter("title");
		int price = Integer.parseInt(request.getParameter("price"));
		int author = Integer.parseInt(request.getParameter("author"));
		int publisher = Integer.parseInt(request.getParameter("publisher"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		String pubDate = request.getParameter("date");
		String isbn = request.getParameter("isbn");
		String description = request.getParameter("description");
		int genreId = Integer.parseInt(request.getParameter("genre"));
		
		try {
			Connection conn = DBConnection.getConnection();
			String sqlStr = "INSERT INTO BOOK (title, price, authorID, publisherID, Qty, publication_date, ISBN, description, genre_id, book_id)\r\n"
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
			PreparedStatement ps = conn.prepareStatement(sqlStr);
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
			
			RequestDispatcher success = request.getRequestDispatcher("admin/add-book.jsp");
			RequestDispatcher error = request.getRequestDispatcher("admin/add-book.jsp?errCode=400");
			
			if (affectedRows > 0) {
				success.forward(request, response);
			} else {
				error.forward(request, response);
			}
			
			
			

			// Step 7: Close connection
			conn.close();

			System.out.println("Woots");

		} catch (Exception e) {
			System.err.println("Error :" + e);
		}
		
		doGet(request, response);
	}

}
