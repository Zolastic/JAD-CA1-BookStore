<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA1
  --%>

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
	<!-- Payment successful page -->
	<%@ include file="modal.jsp"%>
	<%
	String validatedUserID = (String) request.getAttribute("validatedUserID");
	if (validatedUserID != null) {
	%>
	<div class="bg-slate-200 flex items-center justify-center h-screen ">
		<div
			class="bg-white rounded-lg shadow-lg p-8 max-w-md mx-auto text-center">
			<div class="flex items-center justify-center ">
				<i class="fas fa-check-circle text-green-500 text-3xl mr-2"></i>
				<h1 class="text-2xl font-bold">Payment Success!</h1>
			</div>
			<div class="flex items-center justify-center flex-col mt-4">
				<button
					class="bg-slate-500 text-white px-4 py-2 m-2 rounded hover:bg-slate-700 transform hover:scale-110"
					onclick="window.location.href = '/CA1-assignment/CartPage?userIDAvailable=true';">
					Go to Cart</button>
				<button
					class="bg-gray-100 text-black px-4 py-2 mx-2 border border-gray-300 rounded hover:bg-gray-200 transform hover:scale-110"
					onclick="window.location.href = '/CA1-assignment/TransactionHistoryPage?userIDAvailable=true';">
					Go to Transaction History</button>
			</div>

		</div>
	</div>
	<%
	} else {
	%>
	<script>
		showModal("Error loading page");
		var closeButton = document.getElementById("close");
		closeButton.addEventListener("click", function() {
			if (
	<%=validatedUserID%>
		== null) {
				window.location.href = "registrationPage.jsp";
			}
		});
	</script>
	<%
	}
	%>
</body>
</html>