package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.BookDAO;
import dao.SalesReportDAO;
import model.CustomerListByBooks;
import model.Book;
import utils.DBConnection;

/**
 * Servlet implementation class FilteredCustomerListServlet
 */
@WebServlet("/admin/FilteredCustomerListServlet")
public class FilteredCustomerListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SalesReportDAO salesReportDAO = new SalesReportDAO();
	private BookDAO bookDAO = new BookDAO();
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String bookID = request.getParameter("bookID");
		if (bookID == null || bookID.isEmpty()) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("filteredCustomerList.jsp");
			dispatcher.forward(request, response);
			return;
		}
		List<CustomerListByBooks> listOfCustomerByBookID = new ArrayList<>();
		Book bookDetails=null;
		try (Connection connection = DBConnection.getConnection()) {
			listOfCustomerByBookID = salesReportDAO.listOfCustomerByBookID(connection, bookID);
			bookDetails = bookDAO.getBookDetailsForBybookID(connection, bookID);
			request.setAttribute("listOfCustomerByBookID", listOfCustomerByBookID);
			request.setAttribute("bookDetails", bookDetails);
			RequestDispatcher dispatcher = request.getRequestDispatcher("filteredCustomerList.jsp");
			dispatcher.forward(request, response);
		} catch (SQLException e) {
			System.err.println("Error: " + e);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("filteredCustomerList.jsp");
			dispatcher.forward(request, response);
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
