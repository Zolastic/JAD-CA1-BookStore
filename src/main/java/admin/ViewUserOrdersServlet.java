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
import dao.UserDAO;
import model.TransactionHistoryWithItems;
import model.User;
import utils.DBConnection;
import utils.DispatchUtil;

/**
 * Servlet implementation class ViewUserOrders
 */
@WebServlet("/admin/ViewUserOrders")
public class ViewUserOrdersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private TransactionHistoryDAO transactionHistoryDAO = new TransactionHistoryDAO(); 
    private UserDAO userDAO = new UserDAO();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try (Connection connection = DBConnection.getConnection()) {
			String userID = request.getParameter("userID");
			loadData(request, connection, userID);
			DispatchUtil.dispatch(request, response, "viewUserOrders.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			// redirect to error page
		}
	}

	private void loadData(HttpServletRequest request, Connection connection, String userID) throws SQLException {
		List<TransactionHistoryWithItems> transactionHistoryWithItems = transactionHistoryDAO.getTransactionHistoriesOfUser(connection, userID);
		User user = userDAO.getUserInfoByID(connection, userID);
		request.setAttribute("transactionHistoryWithItems", transactionHistoryWithItems);
		request.setAttribute("user", user);
	}
	
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
