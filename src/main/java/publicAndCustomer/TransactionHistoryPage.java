package publicAndCustomer;

import java.io.IOException;
import java.sql.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import model.TransactionHistory;
import dao.VerifyUserDAO;
import dao.TransactionHistoryDAO;
import utils.DBConnection;

/**
 * Servlet implementation class TransactionHistoryPage
 */

/**
 * Author(s): Soh Jian Min (P2238856) Description: JAD CA1
 */

@WebServlet("/TransactionHistoryPage")
public class TransactionHistoryPage extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private VerifyUserDAO verifyUserDAO = new VerifyUserDAO();
	private TransactionHistoryDAO transactionHistoryDAO = new TransactionHistoryDAO();
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TransactionHistoryPage() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userIDAvailable = request.getParameter("userIDAvailable");
		String scrollPosition = request.getParameter("scrollPosition");
		List<TransactionHistory> transactionHistories = new ArrayList<>();
		String userID = null;
		if (userIDAvailable != null && userIDAvailable.equals("true")) {
			userID = (String) request.getSession().getAttribute("userID");
		}
		try (Connection connection = DBConnection.getConnection()) {
			// validate user
			userID = verifyUserDAO.validateUserID(connection, userID);
			if (userID == null) {
				RequestDispatcher dispatcher = request.getRequestDispatcher("publicAndCustomer/registrationPage.jsp");
				dispatcher.forward(request, response);
				return;
			}
			transactionHistories = transactionHistoryDAO.getTransactionHistories(connection, userID);
		} catch (Exception e) {
			System.err.println("Error: " + e);
		}
		request.setAttribute("transactionHistories", transactionHistories);
		request.setAttribute("validatedUserID", userID);
		String dispatcherURL = "publicAndCustomer/transactionHistory.jsp";
		if (scrollPosition != null) {
			dispatcherURL += "?scrollPosition=" + scrollPosition;
		}
		RequestDispatcher dispatcher = request.getRequestDispatcher(dispatcherURL);
		dispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
