package admin;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;

/**
 * Servlet implementation class AddBookServlet
 */
@WebServlet("/AddBookServlet")
public class AddBookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddBookServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		try {
			// Step1: Load JDBC Driver
			Class.forName("com.mysql.jdbc.Driver");
			// Step 2: Define Connection URL
			String connURL = "jdbc:mysql://localhost:3306/JAD?user=root&password=root&serverTimezone=UTC";
			// Step 3: Establish connection to URL
			Connection conn = DriverManager.getConnection(connURL);
			// Step 4: Create Statement object
			// Step 5: Execute SQL Command
			// SELECT * FROM member WHERE name='u111           '  AND password='p111'
			// SELECT * FROM member WHERE name=' ???           '  AND password=' ??? '
			//                                                                   u111' or '1=1                   p111' or '1=1
			// SELECT * FROM member WHERE name=' u111' or '1=1'   AND password=' ??? '
			//                      
			String sqlStr = "SELECT genre_id as genreId, genre_name as genreName FROM genre;";
			PreparedStatement ps = conn.prepareStatement(sqlStr);
			

			// Step 7: Close connection
			conn.close();

			System.out.println("Woots");

		} catch (Exception e) {
			System.err.println("Error :" + e);
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
