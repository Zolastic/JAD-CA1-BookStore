 package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Book;
import model.OverallSalesReport;
import model.TopCustomerSalesReport;
import model.User;
import model.BookReport;

public class SalesReportDAO {
	public OverallSalesReport overallSalesByDay(Connection connection, String transactionDate) {
		OverallSalesReport overallSalesByDay=null;
		String sqlStr = "SELECT\r\n"
				+ "SUM(th.totalAmount) AS totalEarningWithGST,\r\n"
				+ "SUM((th.totalAmount / (th.gstPercent+100)) * 100) AS totalEarningWithoutGST,\r\n"
				+ "SUM((th.totalAmount / (th.gstPercent+100)) * gstPercent) AS gstPercent,\r\n"
				+ "COUNT(DISTINCT th.transaction_historyID) AS totalTransactionOrders,\r\n"
				+ "SUM(ti.Qty) AS totalBooksSold\r\n"
				+ "FROM transaction_history AS th\r\n"
				+ "JOIN transaction_history_items as ti ON th.transaction_historyID= ti.transaction_historyID\r\n"
				+ "WHERE\r\n"
				+ "    DATE(transactionDate) = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, transactionDate);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				double totalEarningWithGST = resultSet.getDouble("totalEarningWithGST");
				double totalEarningWithoutGST = resultSet.getDouble("totalEarningWithoutGST");
				double gst = resultSet.getDouble("gst");
				int totalTransactionOrders = resultSet.getInt("totalTransactionOrders");
				int totalBooksSold = resultSet.getInt("totalBooksSold");
				resultSet.close();
				overallSalesByDay = new OverallSalesReport(totalEarningWithGST, totalEarningWithoutGST, gst, totalTransactionOrders, totalBooksSold, transactionDate);
			}
		}catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			overallSalesByDay = null;
		}
		return overallSalesByDay;
	}
	
	public BookReport bookReportByDay(Connection connection, String transactionDate) {
		BookReport bookReportByDay=null;
		String sqlStr = "SELECT\r\n"
				+ "    b.book_id ,b.ISBN,b.title,a.authorName,p.publisherName ,b.publication_date ,\r\n"
				+ "    b.description ,g.genre_name ,b.img,b.inventory ,b.price,\r\n"
				+ "    CAST(AVG(IFNULL(r.rating, 0)) AS DECIMAL(2, 1)) AS average_rating,\r\n"
				+ "    COUNT(*) AS Quantity,SUM(thi.Qty) AS qtySold,SUM(th.totalAmount) AS totalEarningWithGST,\r\n"
				+ "    SUM((totalAmount / (gstPercent+100)) * 100) AS totalEarningWithoutGST,\r\n"
				+ "    th.gstPercent\r\n"
				+ "FROM\r\n"
				+ "    book b\r\n"
				+ "JOIN\r\n"
				+ "    genre g ON b.genre_id = g.genre_id\r\n"
				+ "LEFT JOIN\r\n"
				+ "    review r ON r.bookID = b.book_id\r\n"
				+ "JOIN\r\n"
				+ "    author a ON b.authorID = a.authorID\r\n"
				+ "JOIN\r\n"
				+ "    publisher p ON b.publisherID = p.publisherID\r\n"
				+ "JOIN\r\n"
				+ "    transaction_history_items thi ON b.book_id = thi.bookID\r\n"
				+ "JOIN\r\n"
				+ "    transaction_history th ON thi.transaction_historyID = th.transaction_historyID\r\n"
				+ "WHERE\r\n"
				+ "    DATE(th.transactionDate) = ?\r\n"
				+ "GROUP BY\r\n"
				+ "    b.book_id,b.ISBN,b.title,th.gstPercent;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, transactionDate);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				String bookID = resultSet.getString("book_id");
				String iSBN = resultSet.getString("ISBN");
				String title = resultSet.getString("title");
				String author = resultSet.getString("authorName");
				String publisher = resultSet.getString("publisherName");
				String publicationDate = resultSet.getString("publication_date");
				String description = resultSet.getString("description");
				String genreName = resultSet.getString("genre_name");
				String img = resultSet.getString("img");
				int sold = resultSet.getInt("sold");
				int inventory = resultSet.getInt("inventory");
				double price = resultSet.getDouble("price");
				double rating = resultSet.getDouble("average_rating");
				Book bookDetails = new Book(bookID, iSBN, title, author, publisher, publicationDate, description,
						genreName, img, sold, inventory, price, rating);
				int qtySold=resultSet.getInt("qtySold");
				double totalEarningWithGST = resultSet.getDouble("totalEarningWithGST");
				double totalEarningWithoutGST = resultSet.getDouble("totalEarningWithoutGST");
				double gstPercent = resultSet.getDouble("gstPercent");
				bookReportByDay=new BookReport(bookDetails, qtySold, totalEarningWithGST, totalEarningWithoutGST, gstPercent);
				
			}
		}catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			bookReportByDay = null;
		}
		return bookReportByDay;
	}
	
	public OverallSalesReport overallSalesByPeriod(Connection connection, String transactionDateFrom,  String transactionDateTo) {
		OverallSalesReport overallSalesByPeriod=null;
		String sqlStr = "SELECT\r\n"
				+ "SUM(th.totalAmount) AS totalEarningWithGST,\r\n"
				+ "SUM((th.totalAmount / (th.gstPercent+100)) * 100) AS totalEarningWithoutGST,\r\n"
				+ "SUM((th.totalAmount / (th.gstPercent+100)) * gstPercent) AS gstPercent,\r\n"
				+ "COUNT(DISTINCT th.transaction_historyID) AS totalTransactionOrders,\r\n"
				+ "SUM(ti.Qty) AS totalBooksSold\r\n"
				+ "FROM transaction_history AS th\r\n"
				+ "JOIN transaction_history_items as ti ON th.transaction_historyID= ti.transaction_historyID\r\n"
				+ "WHERE\r\n"
				+ "    DATE(th.transactionDate) BETWEEN ? AND ?";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, transactionDateFrom);
			ps.setString(2, transactionDateTo);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				double totalEarningWithGST = resultSet.getDouble("totalEarningWithGST");
				double totalEarningWithoutGST = resultSet.getDouble("totalEarningWithoutGST");
				double gst = resultSet.getDouble("gst");
				int totalTransactionOrders = resultSet.getInt("totalTransactionOrders");
				int totalBooksSold = resultSet.getInt("totalBooksSold");
				resultSet.close();
				overallSalesByPeriod = new OverallSalesReport(totalEarningWithGST, totalEarningWithoutGST, gst, totalTransactionOrders, totalBooksSold, (transactionDateFrom+'-'+transactionDateTo));
			}
		}catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			overallSalesByPeriod = null;
		}
		return overallSalesByPeriod;
	}
	
	public BookReport bookReportByPeriod(Connection connection, String transactionDateFrom, String transactionDateTo) {
		BookReport bookReportByPeriod=null;
		String sqlStr = "SELECT\r\n"
				+ "    b.book_id ,b.ISBN,b.title,a.authorName,p.publisherName ,b.publication_date ,\r\n"
				+ "    b.description ,g.genre_name ,b.img,b.inventory ,b.price,\r\n"
				+ "    CAST(AVG(IFNULL(r.rating, 0)) AS DECIMAL(2, 1)) AS average_rating,\r\n"
				+ "    COUNT(*) AS Quantity,SUM(thi.Qty) AS qtySold,SUM(th.totalAmount) AS totalEarningWithGST,\r\n"
				+ "    SUM((totalAmount / (gstPercent+100)) * 100) AS totalEarningWithoutGST,\r\n"
				+ "    th.gstPercent\r\n"
				+ "FROM\r\n"
				+ "    book b\r\n"
				+ "JOIN\r\n"
				+ "    genre g ON b.genre_id = g.genre_id\r\n"
				+ "LEFT JOIN\r\n"
				+ "    review r ON r.bookID = b.book_id\r\n"
				+ "JOIN\r\n"
				+ "    author a ON b.authorID = a.authorID\r\n"
				+ "JOIN\r\n"
				+ "    publisher p ON b.publisherID = p.publisherID\r\n"
				+ "JOIN\r\n"
				+ "    transaction_history_items thi ON b.book_id = thi.bookID\r\n"
				+ "JOIN\r\n"
				+ "    transaction_history th ON thi.transaction_historyID = th.transaction_historyID\r\n"
				+ "WHERE\r\n"
				+ "DATE(th.transactionDate) BETWEEN ? AND ?\r\n"
				+ "GROUP BY\r\n"
				+ "    b.book_id,b.ISBN,b.title,th.gstPercent;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, transactionDateFrom);
			ps.setString(2, transactionDateTo);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				String bookID = resultSet.getString("book_id");
				String iSBN = resultSet.getString("ISBN");
				String title = resultSet.getString("title");
				String author = resultSet.getString("authorName");
				String publisher = resultSet.getString("publisherName");
				String publicationDate = resultSet.getString("publication_date");
				String description = resultSet.getString("description");
				String genreName = resultSet.getString("genre_name");
				String img = resultSet.getString("img");
				int sold = resultSet.getInt("sold");
				int inventory = resultSet.getInt("inventory");
				double price = resultSet.getDouble("price");
				double rating = resultSet.getDouble("average_rating");
				Book bookDetails = new Book(bookID, iSBN, title, author, publisher, publicationDate, description,
						genreName, img, sold, inventory, price, rating);
				int qtySold=resultSet.getInt("qtySold");
				double totalEarningWithGST = resultSet.getDouble("totalEarningWithGST");
				double totalEarningWithoutGST = resultSet.getDouble("totalEarningWithoutGST");
				double gstPercent = resultSet.getDouble("gstPercent");
				bookReportByPeriod=new BookReport(bookDetails, qtySold, totalEarningWithGST, totalEarningWithoutGST, gstPercent);
				
			}
		}catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			bookReportByPeriod = null;
		}
		return bookReportByPeriod;
	}
	
	public OverallSalesReport overallSalesByMonth(Connection connection, String transactionYearMonth) {
		OverallSalesReport overallSalesByMonth=null;
		String sqlStr = "SELECT\r\n"
				+ "SUM(th.totalAmount) AS totalEarningWithGST,\r\n"
				+ "SUM((th.totalAmount / (th.gstPercent+100)) * 100) AS totalEarningWithoutGST,\r\n"
				+ "SUM((th.totalAmount / (th.gstPercent+100)) * gstPercent) AS gstPercent,\r\n"
				+ "COUNT(DISTINCT th.transaction_historyID) AS totalTransactionOrders,\r\n"
				+ "SUM(ti.Qty) AS totalBooksSold\r\n"
				+ "FROM transaction_history AS th\r\n"
				+ "JOIN transaction_history_items as ti ON th.transaction_historyID= ti.transaction_historyID\r\n"
				+ "EXTRACT(YEAR_MONTH FROM transactionDate) = ?;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, transactionYearMonth);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				double totalEarningWithGST = resultSet.getDouble("totalEarningWithGST");
				double totalEarningWithoutGST = resultSet.getDouble("totalEarningWithoutGST");
				double gst = resultSet.getDouble("gst");
				int totalTransactionOrders = resultSet.getInt("totalTransactionOrders");
				int totalBooksSold = resultSet.getInt("totalBooksSold");
				resultSet.close();
				overallSalesByMonth = new OverallSalesReport(totalEarningWithGST, totalEarningWithoutGST, gst, totalTransactionOrders, totalBooksSold, transactionYearMonth);
			}
		}catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			overallSalesByMonth = null;
		}
		return overallSalesByMonth;
	}
	
	public BookReport bookReportByMonth(Connection connection, String transactionDate) {
		BookReport bookReportByMonth=null;
		String sqlStr = "SELECT\r\n"
				+ "    b.book_id ,b.ISBN,b.title,a.authorName,p.publisherName ,b.publication_date ,\r\n"
				+ "    b.description ,g.genre_name ,b.img,b.inventory ,b.price,\r\n"
				+ "    CAST(AVG(IFNULL(r.rating, 0)) AS DECIMAL(2, 1)) AS average_rating,\r\n"
				+ "    COUNT(*) AS Quantity,SUM(thi.Qty) AS qtySold,SUM(th.totalAmount) AS totalEarningWithGST,\r\n"
				+ "    SUM((totalAmount / (gstPercent+100)) * 100) AS totalEarningWithoutGST,\r\n"
				+ "    th.gstPercent\r\n"
				+ "FROM\r\n"
				+ "    book b\r\n"
				+ "JOIN\r\n"
				+ "    genre g ON b.genre_id = g.genre_id\r\n"
				+ "LEFT JOIN\r\n"
				+ "    review r ON r.bookID = b.book_id\r\n"
				+ "JOIN\r\n"
				+ "    author a ON b.authorID = a.authorID\r\n"
				+ "JOIN\r\n"
				+ "    publisher p ON b.publisherID = p.publisherID\r\n"
				+ "JOIN\r\n"
				+ "    transaction_history_items thi ON b.book_id = thi.bookID\r\n"
				+ "JOIN\r\n"
				+ "    transaction_history th ON thi.transaction_historyID = th.transaction_historyID\r\n"
				+ "WHERE\r\n"
				+ "    EXTRACT(YEAR_MONTH FROM th.transactionDate) = ?\r\n"
				+ "GROUP BY\r\n"
				+ "    b.book_id,b.ISBN,b.title,th.gstPercent;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ps.setString(1, transactionDate);
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				String bookID = resultSet.getString("book_id");
				String iSBN = resultSet.getString("ISBN");
				String title = resultSet.getString("title");
				String author = resultSet.getString("authorName");
				String publisher = resultSet.getString("publisherName");
				String publicationDate = resultSet.getString("publication_date");
				String description = resultSet.getString("description");
				String genreName = resultSet.getString("genre_name");
				String img = resultSet.getString("img");
				int sold = resultSet.getInt("sold");
				int inventory = resultSet.getInt("inventory");
				double price = resultSet.getDouble("price");
				double rating = resultSet.getDouble("average_rating");
				Book bookDetails = new Book(bookID, iSBN, title, author, publisher, publicationDate, description,
						genreName, img, sold, inventory, price, rating);
				int qtySold=resultSet.getInt("qtySold");
				double totalEarningWithGST = resultSet.getDouble("totalEarningWithGST");
				double totalEarningWithoutGST = resultSet.getDouble("totalEarningWithoutGST");
				double gstPercent = resultSet.getDouble("gstPercent");
				bookReportByMonth=new BookReport(bookDetails, qtySold, totalEarningWithGST, totalEarningWithoutGST, gstPercent);
				
			}
		}catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			bookReportByMonth = null;
		}
		return bookReportByMonth;
	}
	
	public ArrayList<TopCustomerSalesReport> topTenCustomers(Connection connection) {
		ArrayList<TopCustomerSalesReport> topTenCustomers=new ArrayList();
		String sqlStr = "SELECT\r\n"
				+ "    u.userID,\r\n"
				+ "    u.name,\r\n"
				+ "    u.email,\r\n"
				+ "    u.role,\r\n"
				+ "    u.img,\r\n"
				+ "    COUNT(DISTINCT th.transaction_historyID) AS totalOrderMade,\r\n"
				+ "    SUM(th.totalAmount) AS totalSpendwithGST,\r\n"
				+ "    SUM((totalAmount / (gstPercent+100)) * 100) AS totalSpendwithoutGST,\r\n"
				+ "    SUM(thi.Qty) AS totalBooksBought,\r\n"
				+ "    SUM((totalAmount / (gstPercent+100)) * gstPercent) AS gst\r\n"
				+ "FROM\r\n"
				+ "    users u\r\n"
				+ "JOIN\r\n"
				+ "    transaction_history th ON u.userID = th.custID\r\n"
				+ "JOIN\r\n"
				+ "    transaction_history_items thi ON th.transaction_historyID = thi.transaction_historyID\r\n"
				+ "WHERE\r\n"
				+ "    u.role='customer'\r\n"
				+ "GROUP BY\r\n"
				+ "    u.userID,\r\n"
				+ "    u.name,\r\n"
				+ "    u.email,\r\n"
				+ "    u.role,\r\n"
				+ "    u.img\r\n"
				+ "ORDER BY\r\n"
				+ "    totalSpendwithGST DESC\r\n"
				+ "LIMIT\r\n"
				+ "    10;";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				String userID = resultSet.getString("userID");
				String name = resultSet.getString("name");
				String email = resultSet.getString("email");
				String role = resultSet.getString("role");
				String img = resultSet.getString("img");
				int totalOrderMade = resultSet.getInt("totalOrderMade");
				double totalSpendwithGST = resultSet.getDouble("totalSpendwithGST");
				double totalSpendwithoutGST = resultSet.getDouble("totalSpendwithoutGST");
				int totalBooksBought = resultSet.getInt("totalBooksBought");
				double gst = resultSet.getDouble("gst");
				User topUser=new User(userID, name, email, role, img);
				TopCustomerSalesReport topCustomer=new TopCustomerSalesReport(topUser,  totalSpendwithGST,  totalSpendwithoutGST, totalBooksBought, totalOrderMade, gst);
				topTenCustomers.add(topCustomer);
			}
		}catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			topTenCustomers = null;
		}
		return topTenCustomers;
	}
	
	public ArrayList<BookReport> top5Books(Connection connection, String transactionDateFrom, String transactionDateTo) {
		ArrayList<BookReport> top5Books=new ArrayList();
		String sqlStr = "SELECT\r\n"
				+ "    b.book_id,\r\n"
				+ "    b.ISBN,\r\n"
				+ "    b.title,\r\n"
				+ "    a.authorName,\r\n"
				+ "    p.publisherName,\r\n"
				+ "    b.publication_date,\r\n"
				+ "    b.description,\r\n"
				+ "    g.genre_name,\r\n"
				+ "    b.img,\r\n"
				+ "    b.inventory,\r\n"
				+ "    b.price,\r\n"
				+ "    CAST(AVG(IFNULL(r.rating, 0)) AS DECIMAL(2, 1)) AS average_rating,\r\n"
				+ "    COUNT(*) AS Quantity,\r\n"
				+ "    SUM(thi.Qty) AS qtySold,\r\n"
				+ "    SUM(th.totalAmount) AS totalEarningWithGST,\r\n"
				+ "    SUM((totalAmount / (gstPercent + 100)) * 100) AS totalEarningWithoutGST,\r\n"
				+ "    th.gstPercent\r\n"
				+ "FROM\r\n"
				+ "    book b\r\n"
				+ "JOIN\r\n"
				+ "    genre g ON b.genre_id = g.genre_id\r\n"
				+ "LEFT JOIN\r\n"
				+ "    review r ON r.bookID = b.book_id\r\n"
				+ "JOIN\r\n"
				+ "    author a ON b.authorID = a.authorID\r\n"
				+ "JOIN\r\n"
				+ "    publisher p ON b.publisherID = p.publisherID\r\n"
				+ "JOIN\r\n"
				+ "    transaction_history_items thi ON b.book_id = thi.bookID\r\n"
				+ "JOIN\r\n"
				+ "    transaction_history th ON thi.transaction_historyID = th.transaction_historyID\r\n"
				+ "GROUP BY\r\n"
				+ "    b.book_id, b.ISBN, b.title, th.gstPercent\r\n"
				+ "ORDER BY\r\n"
				+ "    totalEarningWithGST DESC\r\n"
				+ "LIMIT 5;\r\n";
		try (PreparedStatement ps = connection.prepareStatement(sqlStr)) {
			ResultSet resultSet = ps.executeQuery();
			if (resultSet.next()) {
				String bookID = resultSet.getString("book_id");
				String iSBN = resultSet.getString("ISBN");
				String title = resultSet.getString("title");
				String author = resultSet.getString("authorName");
				String publisher = resultSet.getString("publisherName");
				String publicationDate = resultSet.getString("publication_date");
				String description = resultSet.getString("description");
				String genreName = resultSet.getString("genre_name");
				String img = resultSet.getString("img");
				int sold = resultSet.getInt("sold");
				int inventory = resultSet.getInt("inventory");
				double price = resultSet.getDouble("price");
				double rating = resultSet.getDouble("average_rating");
				Book bookDetails = new Book(bookID, iSBN, title, author, publisher, publicationDate, description,
						genreName, img, sold, inventory, price, rating);
				int qtySold=resultSet.getInt("qtySold");
				double totalEarningWithGST = resultSet.getDouble("totalEarningWithGST");
				double totalEarningWithoutGST = resultSet.getDouble("totalEarningWithoutGST");
				double gstPercent = resultSet.getDouble("gstPercent");
				BookReport bookReport=new BookReport(bookDetails, qtySold, totalEarningWithGST, totalEarningWithoutGST, gstPercent);
				top5Books.add(bookReport);
				
			}
		}catch (SQLException e) {
			System.err.println("Error: " + e.getMessage());
			top5Books = null;
		}
		return top5Books;
	}
	
}
