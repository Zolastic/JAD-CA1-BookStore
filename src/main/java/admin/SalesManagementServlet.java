package admin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Book;
import model.TransactionHistoryWithItems;
import model.User;


/**
 * Servlet implementation class SalesManagementServlet
 */
@WebServlet("/admin/SalesManagementServlet")
public class SalesManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Retrieve data from the database and populate the necessary ArrayLists
        List<User> topCustomers = new ArrayList<>(); // Replace with actual code to fetch top customers
        List<Book> topSalesBooks = new ArrayList<>(); // Replace with actual code to fetch top sales books
        List<TransactionHistoryWithItems> salesData = new ArrayList<>(); // Replace with actual code to fetch sales data

        // Set the retrieved data as attributes to be used in the JSP file
        request.setAttribute("topCustomers", topCustomers);
        request.setAttribute("topSalesBooks", topSalesBooks);
        request.setAttribute("salesData", salesData);

        // Forward the request to the JSP file
        RequestDispatcher dispatcher = request.getRequestDispatcher("admin/salesManagement.jsp");
		dispatcher.forward(request, response);
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
