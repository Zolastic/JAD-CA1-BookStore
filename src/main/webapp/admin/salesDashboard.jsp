<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*, model.OverallSalesReport"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Sales Dashboard</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
<%@ page
	import="java.util.*, model.BookReport, model.TopCustomerSalesReport, model.OverallSalesReport"%>
<%@ page import="com.google.gson.Gson"%>
<%@ page import="com.google.gson.JsonObject"%>
</head>
<body>
	<%
	List<BookReport> topSalesBooks = (List<BookReport>) request.getAttribute("topSalesBooks");
	List<TopCustomerSalesReport> topCustomers = (List<TopCustomerSalesReport>) request.getAttribute("topCustomers");
	List<OverallSalesReport> past12MonthsSalesData = (List<OverallSalesReport>) request
			.getAttribute("past12MonthsSalesData");
	Collections.sort(past12MonthsSalesData, Comparator.comparing(OverallSalesReport::getTransactionDate));
	List<Map<Object, Object>> pieChartData = new ArrayList<>();

	// Loop through topSalesBooks to add data to the pieChartData list
	for (BookReport bookReport : topSalesBooks) {
		Map<Object, Object> dataPoint = new HashMap<>();
		dataPoint.put("label", (bookReport.getBookDetails().getTitle()));
		dataPoint.put("y", bookReport.getTotalEarningWithoutGST());
		pieChartData.add(dataPoint);
	}

	// Convert the pieChartData list to a JSON string
	String dataPointsForTopBooks = new Gson().toJson(pieChartData);
	%>
	<%@include file="./navbar.jsp"%>

	<div class="container mx-auto bg-slate-600 py-20 ">
		<div class="flex justify-end space-x-4 p-2 mr-5">
			<a href="<%=request.getContextPath()%>/admin/SalesDashboardServlet"
				class="flex items-center text-black bg-white hover:bg-gray-300 px-4 py-2 rounded-lg">
				<i class="fas fa-chart-line mr-2"></i> Sales Dashboard
			</a> <a href="<%=request.getContextPath()%>/admin/generateSalesReport.jsp"
				class="flex items-center text-black bg-white hover:bg-gray-300 px-4 py-2 rounded-lg">
				<i class="fas fa-file-alt mr-2"></i> Generate Sales Report
			</a> <a href="#"
				class="flex items-center text-black bg-white hover:bg-gray-300 px-4 py-2 rounded-lg">
				<i class="fas fa-filter mr-2"></i> Filter Customer List By Book
			</a>
		</div>
		<div class="flex justify-between items-center">
			<div id="TopBooksChartContainer" style="height: 290px; width: 40%;"
				class="m-5 ml-10"></div>

			<div id="overallSalesChartContainer"
				style="height: 330px; width: 50%;" class="m-5 mx-5 mr-10"></div>
		</div>

		<!-- Top 10 Customers Table -->
		<div class="relative overflow-x-auto mx-10 bg-white p-5">
			<h2 class="text-2xl font-bold my-4">Top 10 Customers</h2>
			<table
				class="w-full text-sm text-left text-gray-500 dark:text-gray-400">
				<thead
					class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
					<tr>
						<th scope="col" class="px-6 py-3">Rank</th>
						<th scope="col" class="px-6 py-3">User</th>
						<th scope="col" class="px-6 py-3">Total Books Bought</th>
						<th scope="col" class="px-6 py-3">Total Order Made</th>
						<th scope="col" class="px-6 py-3">Total Spend With GST</th>
						<th scope="col" class="px-6 py-3">Total Spend W/O GST</th>
						<th scope="col" class="px-6 py-3">Total GST Paid</th>
					</tr>
				</thead>
				<tbody>
					<%
					int rank = 1;
					for (TopCustomerSalesReport customerReport : topCustomers) {
					%>
					<tr
						class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
						<td class="px-6 py-4"><%=rank%></td>
						<td scope="row"
							class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap dark:text-white">
							<div class="h-8 w-8">
								<%
								if (customerReport.getUserDetails().getImage() != null) {
								%>
								<img
									src="data:image/png;base64, <%=customerReport.getUserDetails().getImage()%>"
									class="rounded-full object-contain">
								<%
								} else {
								%>
								<i class="fas fa-user-circle text-gray-400 text-3xl"></i>
								<%
								}
								%>
							</div>
							<div class="pl-3">
								<div class="text-base font-semibold"><%=customerReport.getUserDetails().getName()%></div>
								<div class="font-normal text-gray-500"><%=customerReport.getUserDetails().getEmail()%></div>
							</div>
						</td>
						<td class="px-6 py-4"><%=customerReport.getTotalBooksBought()%></td>
						<td class="px-6 py-4"><%=customerReport.getTotalOrderMade()%></td>
						<td class="px-6 py-4">$<%=String.format("%.2f", customerReport.getTotalSpendwithGST())%></td>
						<td class="px-6 py-4">$<%=String.format("%.2f", customerReport.getTotalSpendwithoutGST())%></td>
						<td class="px-6 py-4">$<%=String.format("%.2f", customerReport.getGst())%></td>
					</tr>
					<%
					rank++;
					}
					%>
				</tbody>
			</table>
		</div>
		<script src="https://cdn.canvasjs.com/canvasjs.min.js"></script>
		<script>
		 function isActiveTab(tabName) {
		        var currentURL = '<%=request.getRequestURI()%>';
		        return currentURL.includes(tabName);
		    }
		 
    var dataPointsForSalesReport = [
        <%for (OverallSalesReport report : past12MonthsSalesData) {%>
            { label: formatDate('<%=report.getTransactionDate()%>'), y: <%=String.format("%.2f", report.getTotalEarningWithoutGST())%> },
        <%}%>
    ];

    function formatDate(dateString) {
        var year = dateString.substring(0, 4);
        var month = dateString.substring(4);

        var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
        var monthName = monthNames[parseInt(month) - 1];

        return year + ', ' + monthName;
    }

    window.onload = function() {
        var chartForSalesReport = new CanvasJS.Chart("overallSalesChartContainer", {
        	animationEnabled: true,
        	exportFileName: "Overall Sales in the Past 12 Month",
            exportEnabled: true,
            title: {
                text: "Overall Sales Last 12 Months"
            },
            axisX: {
                title: "Transaction Date (Year, Month)",
                interval: 1,
                labelAngle: -45,
            },
            axisY: {
                title: "Total Earnings without GST (in SGD)",
                includeZero: true,
                valueFormatString: "$#,##0.00"
            },
            data: [{
                type: "column",
                dataPoints: dataPointsForSalesReport
            }]
        });

        var chartForTopBooks = new CanvasJS.Chart("TopBooksChartContainer", {
            theme: "light2",
            animationEnabled: true,
            exportFileName: "Top 5 Sales Books",
            exportEnabled: true,
            title: {
                text: "Top 5 Sales Book's Total Earnings W/O GST"
            },
            data: [{
                type: "pie",
                legendText: "{label}",
                yValueFormatString: "$#,##0.00",
                toolTipContent: "{label}: <strong>{y}</strong>",
                indexLabel: "{label} {y}",
                dataPoints: <%=dataPointsForTopBooks%>
            }]
        });
        
        chartForSalesReport.render();
        chartForTopBooks.render();
    }
</script>
</body>
</html>