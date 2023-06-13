<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<title>Payment Success</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
	rel="stylesheet">
<%@ include file="../../tailwind-css.jsp"%>
</head>
<body>

	<%@ include file="navBar/headerNavCustomer.jsp"%>
	<%
	String validatedUserID = (String) request.getAttribute("validatedUserID");
	if (validatedUserID != null) {
	%>
	<div
		class="bg-slate-200 flex items-center justify-center h-screen ">
		<div
			class="bg-white rounded-lg shadow-lg p-8 max-w-md mx-auto text-center">
			<div class="flex items-center justify-center mb-6">
				<i class="fas fa-check-circle text-green-500 text-3xl mr-2"></i>
				<h1 class="text-2xl font-bold">Payment Success!</h1>
			</div>
			<button
				class="bg-slate-500 text-white px-4 py-2 rounded hover:bg-slate-700 transform hover:scale-110"
				onclick="window.location.href = '/CA1-assignment/CartPage?userIDAvailable=true';">Go
				to Cart</button>
		</div>
	</div>
	<%
	} else {
	%>
	<script>
		alert("Error loading page");
		if (<%=validatedUserID%> == null) {
			window.location.href = "registrationPage.jsp";
		}
	</script>
	<%
	}
	%>
</body>
</html>