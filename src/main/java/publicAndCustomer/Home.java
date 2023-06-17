package publicAndCustomer;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import utils.DBConnection;
import model.Book;
import dao.BookDAO;
import dao.VerifyUserDAO;
/**
 * Servlet implementation class Home
 */
@WebServlet("/Home")
public class Home extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	private BookDAO bookDAO = new BookDAO();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Home() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		setData(request);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/home.jsp");
		dispatcher.forward(request, response);
	}

	// function to be run everytime home.jsp is run
	public void setData(HttpServletRequest request) {
		List<Book> popularBooks = new ArrayList<>();
		String validatedUserID = null;
		String userID=(String) request.getSession().getAttribute("userID");

		try (Connection connection = DBConnection.getConnection()) {
			popularBooks = bookDAO.popularBooks(connection);
			if(userID!=null) {
				validatedUserID = verifyUserDAO.validateUserID(connection, userID);
			}
			connection.close();
		} catch (SQLException e) {
			System.err.println("Error: " + e);
		}

		request.setAttribute("popularBooks", popularBooks);
		request.setAttribute("validatedUserID", validatedUserID);
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
