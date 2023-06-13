package utils;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CustomerSessionValidation {
	
	public static boolean validate(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);
		boolean valid = true;
		if (session == null) {
			valid = false;
		}
		
		if (valid) {
			String userIDFromSession = (String) session.getAttribute("userID");
			valid = userIDFromSession != null;
		}
		
		if (!valid) {
			res.sendRedirect(req.getContextPath() + "/publicAndCustomer/registrationPage.jsp");
		}
		
		return valid;
	}
}
