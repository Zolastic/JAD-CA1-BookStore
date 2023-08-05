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
import model.TransactionHistory;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class EditOrderServlet
 */
@WebServlet("/admin/EditOrder")
public class EditOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TransactionHistoryDAO transactionHistoryDAO = new TransactionHistoryDAO();
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String transactionHistoryID = request.getParameter("transactionHistoryID");
			loadData(request, connection, transactionHistoryID);
			DispatchUtil.dispatch(request, response, "editOrder.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			DispatchUtil.dispatch(request, response, "index.jsp");
		}
	}

	private void loadData(HttpServletRequest request, Connection connection, String transactionHistoryID) throws SQLException {
		TransactionHistory transactionHistory = transactionHistoryDAO.getTransactionHistoryByID(connection, transactionHistoryID);
		request.setAttribute("order", transactionHistory);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String fullAddress = request.getParameter("fullAddress");
		String transactionHistoryID = request.getParameter("transactionHistoryID");
		
		if (fullAddress.equals("") || transactionHistoryID.equals("")) {
			DispatchUtil.dispatch(request, response, "editOrder.jsp?statusCode=" + 400);
			return;
		}
		
		try (Connection connection = DBConnection.getConnection()) {
			int statusCode = transactionHistoryDAO.updateTransactionHistoryAddress(connection, fullAddress, transactionHistoryID);
			loadData(request, connection, transactionHistoryID);
			DispatchUtil.dispatch(request, response, "editOrder.jsp?statusCode=" + statusCode);
			
		} catch (SQLException e) {
			DispatchUtil.dispatch(request, response, "editOrder.jsp?statusCode=" + 500);
		}
	}

}
