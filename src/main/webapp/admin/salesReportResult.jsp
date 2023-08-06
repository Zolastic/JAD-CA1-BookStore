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
<script src="<%=request.getContextPath()%>/print.js"></script>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>
	<%@ include file="modal.jsp"%>
	<%
	boolean error = false;
	OverallSalesReport overallSales = null;
	List<BookReport> bookReport = null;
	try {
		overallSales = (OverallSalesReport) request.getAttribute("overallSales");
		bookReport = (List<BookReport>) request.getAttribute("bookReport");
	} catch (ClassCastException e) {
		error = true;
	}

	if (overallSales != null && bookReport != null&& !error) {
	%>
	<%@include file="./navbar.jsp"%>
	<div class="h-full w-full bg-slate-600 py-20">
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
				</a> <a
					href="<%=request.getContextPath()%>/admin/FilterCustomersByBookMain"
					class="flex items-center text-black bg-white hover:bg-gray-300 px-4 py-2 rounded-lg">
					<i class="fas fa-filter mr-2"></i> Filter Customer List By Book
				</a>
			</div>
		</div>
		<div class="flex justify-end m-4 mr-10">
			<button onclick="printReport()"
				class="flex items-center text-black bg-white hover:bg-gray-300 px-4 py-2 rounded-lg">
				<i class="fas fa-print mr-2"></i> Print Report
			</button>
		</div>
		<div class="bg-white mx-auto m-5 h-screen overflow-y-auto"
			style="transform: scale(0.7); transform-origin: top;">
			<div id="printdiv">
				<div class="flex justify-end">
					<div class="flex-end p-4">
						<%-- Display the overall sales report here --%>
						<div style="font-size: 11px;">
							<h2 class="font-semibold mb-2">Overall Sales Report</h2>
							<p>
								Total Earnings (with GST): $<%=String.format("%.2f", overallSales.getTotalEarningWithGST())%>
							</p>
							<p>
								Total Earnings (without GST): $<%=String.format("%.2f", overallSales.getTotalEarningWithoutGST())%>
							</p>
							<p>
								Total GST Received: $<%=String.format("%.2f", overallSales.getGst())%>
							</p>

							<p>
								Total Transaction Orders:
								<%=overallSales.getTotalTransactionOrders()%></p>
							<p>
								Total Books Sold:
								<%=overallSales.getTotalBooksSold()%></p>
							<p>
								Transaction Date:
								<%=formatDate(overallSales.getTransactionDate())%></p>
						</div>
					</div>
				</div>
				<hr class="my-2 border-gray-400">
				<div class="flex justify-center mb-5">
					<table class="table-auto text-xs table-fixed w-full">
						<thead class="text-gray-700 bg-gray-100">
							<tr>
								<th class="px-4 w-1/6">ISBN</th>
								<th class="px-4 w-1/6">Title</th>
								<th class="px-4 w-1/6">Average Listing Price</th>
								<th class="px-4 w-1/6">Qty Sold</th>
								<th class="px-4 w-1/6">Total Earnings (with GST)</th>
								<th class="px-4 w-1/6">Total Earnings (without GST)</th>
								<th class="px-4 w-1/6">Average GST Percent</th>
							</tr>
						</thead>
						<tbody>
							<%
							for (BookReport report : bookReport) {
							%>
							<tr class="text-center">
								<td class="border px-4 py-2 whitespace-nowrap"><%=report.getBookDetails().getISBN()%></td>
								<td class="border px-4 py-2 whitespace-nowrap"><%=report.getBookDetails().getTitle()%></td>
								<td class="border px-4 py-2 whitespace-nowrap"><%=report.getBookDetails().getPrice()%></td>
								<td class="border px-4 py-2 whitespace-nowrap"><%=report.getQtySold()%></td>
								<td class="border px-4 py-2 whitespace-nowrap">$<%=report.getTotalEarningWithGST()%></td>
								<td class="border px-4 py-2 whitespace-nowrap">$<%=report.getTotalEarningWithoutGST()%></td>
								<td class="border px-4 py-2 whitespace-nowrap"><%=report.getGstPercent()%>%</td>
							</tr>
							<%
							}
							%>

						</tbody>
					</table>
				</div>
			</div>
		</div>
		<%!public String formatDate(String transactionDate) {
		String formattedDate = "Invalid date format";

		if (transactionDate.matches("^\\d{6}$")) {
			String year = transactionDate.substring(0, 4);
			String month = transactionDate.substring(4, 6);
			formattedDate = getMonthName(Integer.parseInt(month)) + " " + year;
		} else if (transactionDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
			String year = transactionDate.substring(0, 4);
			String month = transactionDate.substring(5, 7);
			String day = transactionDate.substring(8, 10);
			formattedDate = day + " " + getMonthName(Integer.parseInt(month)) + " " + year;
		} else if (transactionDate.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
			String[] dateParts = transactionDate.split("-");
			String year = dateParts[0];
			String month = dateParts[1];
			String day = dateParts[2];
			formattedDate = day + " " + getMonthName(Integer.parseInt(month)) + " " + year;
		}

		return formattedDate;
	}

	public String getMonthName(int month) {
		String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September",
				"October", "November", "December"};
		if (month >= 1 && month <= 12) {
			return monthNames[month - 1];
		} else {
			return "Invalid Month";
		}
	}%>
		<%
		} else {
		%>
		<script>
			var closeButton = document.getElementById("close");
			showModal("Error loading page");
			closeButton.addEventListener("click", function() {
				window.location.href = "<%=request.getContextPath()%>/admin/index.jsp";
			});
		</script>
		<%
		}
		%>
	
</body>
</html>
