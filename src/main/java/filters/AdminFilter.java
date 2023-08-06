package filters;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.UserDAO;
import utils.DBConnection;

/**
 * Servlet Filter implementation class AdminFilter
 */
@WebFilter(urlPatterns = {"/admin/*"})
public class AdminFilter extends HttpFilter implements Filter {
	private UserDAO userDAO = new UserDAO();
       
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		String servletPath = request.getServletPath();
		System.out.println("Filter for Admin " + request.getServletPath() + ", " + request.getContextPath());
		
		if (servletPath.endsWith(".css")) {
			chain.doFilter(request, response);
			return;
		}
		
		HttpSession session = request.getSession(false);
		if (session == null) {
			response.sendRedirect(request.getContextPath() + "/publicAndCustomer/registrationPage.jsp");
			return;
		}
		
		String userID = (String) session.getAttribute("userID");
		if (userID == null) {
			response.sendRedirect(request.getContextPath() + "/publicAndCustomer/registrationPage.jsp");
			return;
		}
		
		try (Connection connection = DBConnection.getConnection();) {
			int statusCodeForVerifyRole = userDAO.verifyUserIsAdmin(connection, userID);
			if (statusCodeForVerifyRole != 200) {
				response.sendRedirect(request.getContextPath() + "/home.jsp");
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
