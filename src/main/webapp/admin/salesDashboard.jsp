<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*, model.OverallSalesReport"%>
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
	<div class="container mx-auto bg-slate-600 pt-20 ">
		<div class="flex justify-center items-center">
			<h1 class="text-3xl font-bold my-6 text-white">Sales Inquiry and Reporting</h1>
		</div>
		<div class="flex justify-between items-center">
			<!-- Top 5 sales books with graph -->
			<div id="TopBooksChartContainer" style="height: 300px; width: 35%;" class="m-5 ml-10"></div>

			<!-- Overall sales past 12 months with graph -->
			<div id="overallSalesChartContainer"
				style="height: 350px; width: 45%;" class="m-5 mx-5 mr-10"></div>
		</div>

		<!-- Sales by date, period, and month -->
		<h2 class="text-2xl font-bold my-4">Sales by Date, Period, and
			Month</h2>
		<!-- Place your HTML code for sales inquiry here -->

		<!-- Top 10 customers -->
		<h2 class="text-2xl font-bold my-4">Top 10 Customers</h2>
		<!-- Place your HTML code for top 10 customers here -->
	</div>
	<!-- Other HTML and JSP code above -->
	<script src="https://cdn.canvasjs.com/canvasjs.min.js"></script>
	<script>
    // Extracting data from the JSP attribute and converting it to a JavaScript object
    var dataPointsForSalesReport = [
        <%for (OverallSalesReport report : past12MonthsSalesData) {%>
            { label: formatDate('<%=report.getTransactionDate()%>'), y: <%=String.format("%.2f", report.getTotalEarningWithoutGST())%> },
        <%}%>
    ];

    // Function to format date e.g., '2023, February'
    function formatDate(dateString) {
        // Extracting year and month from the transactionDate (assumed to be in YYYYMM format)
        var year = dateString.substring(0, 4);
        var month = dateString.substring(4);

        // Convert month number to month name
        var monthNames = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
        var monthName = monthNames[parseInt(month) - 1];

        // Returning formatted date string e.g., '2023, February'
        return year + ', ' + monthName;
    }

    // Rendering the chart
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