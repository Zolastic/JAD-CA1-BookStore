package publicAndCustomer;

import java.io.Console;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import model.Book;

import java.sql.CallableStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.DBConnection;

@WebServlet("/bookDetailsPage")
public class bookDetailsPage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public bookDetailsPage() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bookID = request.getParameter("bookID");
		System.out.println(bookID);
		Book bookDetails = null;
		List<Map<String, Object>> reviews = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			String simpleProc = "{call getBookDetails(?)}";
			CallableStatement cs = connection.prepareCall(simpleProc);
			cs.setString(1, bookID);
			cs.execute();
			ResultSet resultSetForBookDetails = cs.getResultSet();
			if (resultSetForBookDetails.next()) {
				bookDetails = new Book(resultSetForBookDetails.getString("bookID"),
						resultSetForBookDetails.getString("ISBN"), resultSetForBookDetails.getString("title"),
						resultSetForBookDetails.getString("author"), resultSetForBookDetails.getString("publisher"),
						resultSetForBookDetails.getString("publication_date"),
						resultSetForBookDetails.getString("description"),
						resultSetForBookDetails.getString("genre_name"), resultSetForBookDetails.getString("img"),
						resultSetForBookDetails.getInt("sold"), resultSetForBookDetails.getInt("inventory"),
						resultSetForBookDetails.getDouble("price"), 1, resultSetForBookDetails.getDouble("rating"));
				System.out.println(resultSetForBookDetails.getString("ISBN"));
				System.out.println(resultSetForBookDetails.getString("genre_name"));
				String sqlStr = "SELECT * FROM review WHERE bookID=?;";
				PreparedStatement ps = connection.prepareStatement(sqlStr);
				ps.setString(1, bookID);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					Map<String, Object> review = Map.of("review_id", rs.getString("review_id"), "review_text",
							rs.getString("review_text"), "ratingByEachCust", rs.getDouble("rating"), "custID",
							rs.getString("custID"));
					reviews.add(review);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error :" + e);
		}
		request.setAttribute("bookDetails", bookDetails);
		request.setAttribute("reviews", reviews);

		request.getRequestDispatcher("/publicAndCustomer/bookDetailsPage.jsp").forward(request, response);
	}

	protected void addToCart(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userID = request.getParameter("userID");
		String bookID = request.getParameter("bookID");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
		if (userID == null) {
			request.getRequestDispatcher("/publicAndCustomer/login.jsp").forward(request, response);
		}else {
	 	
	 	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
		String action = request.getParameter("action");

		if (action != null && action.equals("addToCart")) {
			addToCart(request, response);

		}
	}
}
