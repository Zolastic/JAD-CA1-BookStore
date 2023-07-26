<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Generate Sales Report</title>
<%@include file="../tailwind-css.jsp"%>
<%@ page
	import="java.util.*, model.BookReport, model.OverallSalesReport"%>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>
	<%
	OverallSalesReport overallSales = (OverallSalesReport) request.getAttribute("overallSales");
	;
	List<BookReport> bookReport = (List<BookReport>) request.getAttribute("bookReport");
	%>
	<%@include file="./navbar.jsp"%>
	<div class="container bg-slate-600 py-20">
		<div class="flex justify-between">
			<div class="flex items-center space-x-2 px-4 mb-4">
				<a
					href="<%=request.getContextPath()%>/admin/generateSalesReportOptions.jsp"
					class="text-white hover:text-gray-300 focus:outline-none text-xl">
					<i class="fas fa-arrow-left text-2xl"></i> <span class="ml-2">Back</span>
				</a>
			</div>
			<div class="flex space-x-4 p-2 mr-5">
				<a href="<%=request.getContextPath()%>/admin/SalesDashboardServlet"
					class="flex items-center text-black bg-white hover:bg-gray-300 px-4 py-2 rounded-lg">
					<i class="fas fa-chart-line mr-2"></i> Sales Dashboard
				</a> <a
					href="<%=request.getContextPath()%>/admin/generateSalesReportOptions.jsp"
					class="flex items-center text-black bg-white hover:bg-gray-300 px-4 py-2 rounded-lg">
					<i class="fas fa-file-alt mr-2"></i> Generate Sales Report
				</a> <a href="#"
					class="flex items-center text-black bg-white hover:bg-gray-300 px-4 py-2 rounded-lg">
					<i class="fas fa-filter mr-2"></i> Filter Customer List By Book
				</a>
			</div>
		</div>
		<div class="bg-white mx-auto m-5 h-screen overflow-y-auto"
			style="width: 411px;"></div>
	</div>
</body>
</html>
