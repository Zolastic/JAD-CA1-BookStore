<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA2
  --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<title>Payment Error</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
	rel="stylesheet">
<%@ include file="../../tailwind-css.jsp"%>
</head>
<body>
	<%@ include file="customerModal.jsp"%>
	<%
	String validatedUserID = (String) request.getAttribute("validatedUserID");
	String error = request.getParameter("error");
	if (validatedUserID != null) {
	%>
	<div class="bg-slate-200 flex items-center justify-center h-screen">
		<div
			class="bg-white rounded-lg shadow-lg p-8 max-w-md mx-auto text-center">
			<div class="flex items-center justify-center mb-6">
				<i class="fas fa-exclamation-triangle text-yellow-500 text-3xl mr-2"></i>
				<%
				if (error != null && error.equals("RefundFailed")) {
				%>
				<!-- If payment have been processed and have database error and refund failed -->
				<h1 class="text-2xl font-bold">Payment unsuccessful, refund
					failed.</h1>
				<p class="mb-4">Please contact +65 6666 6666 for assistance.</p>
				<%
				} else {
				%>
				<!-- Payment unsuccessful -->
				<h1 class="text-2xl font-bold">Payment Unsuccesful!</h1>
				<%
				}
				%>
			</div>
			<button
				class="bg-slate-500 text-white px-4 py-2 rounded hover:bg-slate-700 transform hover:scale-110"
				onclick="window.location.href = '<%=request.getContextPath()%>/CartPage?userIDAvailable=true';">Go
				to Cart</button>
		</div>
	</div>
	<%
	} else {
	%>
	<script>
		showModal("Error loading page");
		var closeButton = document.getElementById("close");
		closeButton.addEventListener("click", function() {
			if (<%=validatedUserID%>== null) {
				window.location.href = "registrationPage.jsp";
			}
		});
	</script>
	<%
	}
	%>
</body>
</html>
