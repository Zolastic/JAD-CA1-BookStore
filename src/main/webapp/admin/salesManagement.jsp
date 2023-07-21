<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Sales Report</title>
<!-- Include Tailwind CSS -->
<%@include file="../tailwind-css.jsp"%>
<%@ page
	import="java.util.*, model.BookReport, model.TopCustomerSalesReport, model.OverallSalesReport"%>
</head>
<body>
	<%
	List<BookReport> topSalesBooks = (List<BookReport>) request.getAttribute("topSalesBooks");
	List<TopCustomerSalesReport> topCustomers = (List<TopCustomerSalesReport>) request.getAttribute("topCustomers");
	List<OverallSalesReport> past12MonthsSalesData = (List<OverallSalesReport>) request.getAttribute("past12MonthsSalesData");
	System.out.println("a:"+topSalesBooks);
	System.out.println("b:"+topCustomers);
	System.out.println("c:"+past12MonthsSalesData);
	%>
	<div class="container mx-auto">
		<h1 class="text-3xl font-bold my-6">Sales Inquiry and Reporting</h1>

		<!-- Sales by date, period, and month -->
		<h2 class="text-2xl font-bold my-4">Sales by Date, Period, and
			Month</h2>
		<!-- Place your HTML code for sales inquiry here -->

		<!-- Top 10 customers -->
		<h2 class="text-2xl font-bold my-4">Top 10 Customers</h2>
		<!-- Place your HTML code for top 10 customers here -->

		<!-- Top 5 sales books with graph -->
		<h2 class="text-2xl font-bold my-4">Top 5 Sales Books</h2>
		<!-- Place your HTML code for top 5 sales books here -->

		<!-- Overall sales past 12 months with graph -->
		<h2 class="text-2xl font-bold my-4">Overall Sales in the Past 12
			Months</h2>
		<!-- Place your HTML code for overall sales graph here -->
	</div>
</body>
</html>
