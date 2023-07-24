package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.TransactionHistoryDAO;
import dao.TransactionHistoryItemsDAO;
import model.TransactionHistory;
import model.TransactionHistoryItemBook;
import utils.DBConnection;

/**
 * Servlet implementation class orderDetails
 */
@WebServlet("/admin/OrderDetails")
public class OrderDetailsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TransactionHistoryDAO transactionHistoryDAO = new TransactionHistoryDAO();
	private TransactionHistoryItemsDAO transactionHistoryItemsDAO = new TransactionHistoryItemsDAO();
       

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String transactionHistoryID = request.getParameter("transactionHistoryID");
			loadData(request, connection, transactionHistoryID);
			request.getRequestDispatcher("orderDetails.jsp").forward(request, response);
		} catch (SQLException e) {
			e.printStackTrace();
			request.getRequestDispatcher("viewUsers.jsp").forward(request, response);
		}
	}

	private void loadData(HttpServletRequest request, Connection connection, String transactionHistoryID) throws SQLException {
		TransactionHistory transactionHistory = transactionHistoryDAO.getTransactionHistoryByID(connection, transactionHistoryID);
		List<TransactionHistoryItemBook> items = transactionHistoryItemsDAO.getTransactionHistoryItemsByTransactionHistoryID(connection, transactionHistoryID);
		request.setAttribute("order", transactionHistory);
		request.setAttribute("items", items);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
