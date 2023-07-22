package admin;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dao.TransactionHistoryDAO;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class DeleteOrder
 */
@WebServlet("/admin/DeleteOrder")
public class DeleteOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TransactionHistoryDAO transactionHistoryDAO = new TransactionHistoryDAO();
	
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
		String transactionHistoryID = request.getParameter("transactionHistoryID");
		String userID = request.getParameter("userID");
		try (Connection connection = DBConnection.getConnection()) {
			int statusCode = transactionHistoryDAO.deleteTransactionHistory(connection, transactionHistoryID);
			DispatchUtil.dispatch(request, response, "ViewUserOrders?userID=" + userID);
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "ViewUserOrders?userID=" + userID);
		}
	}

}
