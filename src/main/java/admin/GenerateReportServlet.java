package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.BookReport;
import model.OverallSalesReport;
import utils.DBConnection;
import dao.SalesReportDAO;

/**
 * Servlet implementation class GenerateReportServlet
 */
@WebServlet("/admin/GenerateReportServlet")
public class GenerateReportServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private SalesReportDAO salesReportDAO = new SalesReportDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String by = request.getParameter("by");
		String selectedDate = request.getParameter("selectedDate");
		String selectedMonth = request.getParameter("selectedMonth");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		if (by == null || by.isEmpty()) {
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("generateSalesReportOptions.jsp?generateError=invalidInput");
			dispatcher.forward(request, response);
			return;
		}

		DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("yyyyMM");
		OverallSalesReport overallSales = null;
		ArrayList<BookReport> bookReport = new ArrayList<>();
		try {
			if (by.equals("day")) {
				if (selectedDate == null || selectedDate.isEmpty()) {
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("generateSalesReportOptions.jsp?generateError=invalidInput");
					dispatcher.forward(request, response);
					return;
				}
				LocalDate date = LocalDate.parse(selectedDate, dateFormat);
				String formattedDate = date.format(dateFormat);

				try (Connection connection = DBConnection.getConnection()) {
					overallSales = salesReportDAO.overallSalesByDay(connection, formattedDate);
					bookReport = salesReportDAO.bookReportsByDay(connection, formattedDate);
				} catch (SQLException e) {
					System.err.println("Error: " + e);
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("salesDashboard.jsp?generateError=retrieveErr");
					dispatcher.forward(request, response);
				}

			} else if (by.equals("month")) {
				if (selectedMonth == null || selectedMonth.isEmpty()) {
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("generateSalesReportOptions.jsp?generateError=invalidInput");
					dispatcher.forward(request, response);
					return;
				}

				String selectedMonthWithDay = selectedMonth + "-01";

				LocalDate month = LocalDate.parse(selectedMonthWithDay, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

				String formattedMonth = month.format(DateTimeFormatter.ofPattern("yyyyMM"));

				try (Connection connection = DBConnection.getConnection()) {
					overallSales = salesReportDAO.overallSalesByMonth(connection, formattedMonth);
					bookReport = salesReportDAO.bookReportsByMonth(connection, formattedMonth);
				} catch (SQLException e) {
					System.err.println("Error: " + e);
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("salesDashboard.jsp?generateError=retrieveErr");
					dispatcher.forward(request, response);
				}
			} else if (by.equals("period")) {
				if (startDate == null || startDate.isEmpty() || endDate == null || endDate.isEmpty()) {
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("generateSalesReportOptions.jsp?generateError=invalidInput");
					dispatcher.forward(request, response);
					return;
				}
				LocalDate start = LocalDate.parse(startDate, dateFormat);
				LocalDate end = LocalDate.parse(endDate, dateFormat);

				String formattedStartDate = start.format(dateFormat);
				String formattedEndDate = end.format(dateFormat);

				try (Connection connection = DBConnection.getConnection()) {
					overallSales = salesReportDAO.overallSalesByPeriod(connection, startDate,
							endDate);
					bookReport = salesReportDAO.bookReportsByPeriod(connection, formattedStartDate, formattedEndDate);
				} catch (SQLException e) {
					System.err.println("Error: " + e);
					RequestDispatcher dispatcher = request
							.getRequestDispatcher("salesDashboard.jsp?generateError=retrieveErr");
					dispatcher.forward(request, response);
				}

			} else {
				RequestDispatcher dispatcher = request
						.getRequestDispatcher("generateSalesReportOptions.jsp?generateError=invalidInput");
				dispatcher.forward(request, response);
				return;
			}

			request.setAttribute("overallSales", overallSales);
			request.setAttribute("bookReport", bookReport);
			RequestDispatcher dispatcher = request.getRequestDispatcher("salesReportResult.jsp");
			dispatcher.forward(request, response);

		} catch (Exception e) {
			System.err.println("Error: " + e);
			RequestDispatcher dispatcher = request
					.getRequestDispatcher("generateSalesReportOptions.jsp?generateError=unexpectedErr");
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
