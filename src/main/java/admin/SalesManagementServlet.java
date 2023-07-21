package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BookReport;
import model.TopCustomerSalesReport;
import utils.DBConnection;
import model.OverallSalesReport;
import dao.SalesReportDAO;

/**
 * Servlet implementation class SalesManagementServlet
 */
@WebServlet("/admin/SalesManagementServlet")
public class SalesManagementServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SalesReportDAO salesReportDAO = new SalesReportDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		List<TopCustomerSalesReport> topCustomers = new ArrayList<>();
		List<BookReport> topSalesBooks = new ArrayList<>();
		List<OverallSalesReport> past12MonthsSalesData = new ArrayList<>();
		try (Connection connection = DBConnection.getConnection()) {
			topCustomers = salesReportDAO.topTenCustomers(connection);
			topSalesBooks = salesReportDAO.top5Books(connection);
			request.setAttribute("topCustomers", topCustomers);
			request.setAttribute("topSalesBooks", topSalesBooks);
			Calendar calendar = Calendar.getInstance();
			int currentYear = calendar.get(Calendar.YEAR);
			int currentMonth = calendar.get(Calendar.MONTH) + 1;
			for (int i = 0; i < 12; i++) {
				int year = currentYear;
				int month = currentMonth - i;
				if (month <= 0) {
					year -= 1;
					month += 12;
				}
				String transactionYearMonth = String.format("%04d%02d", year, month);
				OverallSalesReport salesData = salesReportDAO.overallSalesByMonth(connection, transactionYearMonth);
				if (salesData != null) {
					past12MonthsSalesData.add(salesData);
				}
			}
			request.setAttribute("topCustomers", topCustomers);
			request.setAttribute("topSalesBooks", topSalesBooks);
			request.setAttribute("past12MonthsSalesData", past12MonthsSalesData);
			RequestDispatcher dispatcher = request.getRequestDispatcher("salesManagement.jsp");
			dispatcher.forward(request, response);
			connection.close();
		} catch (SQLException e) {
			System.err.println("Error: " + e);
			RequestDispatcher dispatcher = request.getRequestDispatcher("salesManagement.jsp");
			dispatcher.forward(request, response);
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
