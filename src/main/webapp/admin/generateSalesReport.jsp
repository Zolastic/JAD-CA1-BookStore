<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Generate Sales Report</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>
	<%@include file="./navbar.jsp"%>
	<div class="container bg-slate-600 h-screen py-20">
		<div class="flex justify-end space-x-4 p-2 mr-5">
			<a href="<%=request.getContextPath()%>/admin/SalesDashboardServlet"
				class="flex items-center text-black bg-white hover:bg-gray-300 px-4 py-2 rounded-lg">
				<i class="fas fa-chart-line mr-2"></i> Sales Dashboard
			</a> <a
				href="<%=request.getContextPath()%>/admin/generateSalesReport.jsp"
				class="flex items-center text-black bg-white hover:bg-gray-300 px-4 py-2 rounded-lg">
				<i class="fas fa-file-alt mr-2"></i> Generate Sales Report
			</a> <a href="#"
				class="flex items-center text-black bg-white hover:bg-gray-300 px-4 py-2 rounded-lg">
				<i class="fas fa-filter mr-2"></i> Filter Customer List By Book
			</a>
		</div>
		<div class="flex justify-center w-full mt-40" id="initialForm">
			<div class="bg-white rounded-lg p-5">
				<label class="block">Generate Report By:</label> <select
					name="reportType" id="reportTypeSelector"
					class="border border-gray-300 rounded px-2 py-1 focus:outline-none focus:border-blue-500">
					<option value="byDate">Date (YYYY-MM-DD)</option>
					<option value="byMonth">Month (YYYYMM)</option>
					<option value="byPeriod">Period (YYYY-MM-DD to YYYY-MM-DD)</option>
				</select> <br>
				<div class="mt-2">
					<button type="button"
						class="bg-gray-300 hover:bg-gray-500 p-2 rounded-lg"
						id="nextButton">Next</button>
				</div>
			</div>
		</div>

		<div id="datePickerContainer" class="w-full mt-40"
			style="display: none;">
			<!-- Date picker content here -->
			<div class="flex justify-center">
				<div class="inline-block bg-white rounded-lg p-5">
					<form action="GenerateReportServlet" method="post">
						<label>Choose a date:</label> <input type="date"
							name="selectedDate" id="datePicker"> <br> <input
							class="bg-gray-300 hover:bg-gray-500 p-2 rounded-lg"
							type="submit" value="Generate Report">
						<button type="button"
							class="bg-gray-300 hover:bg-gray-500 p-2 rounded-lg ml-2"
							id="backToDateSelection1">Back</button>
					</form>
				</div>
			</div>
		</div>


		<div id="monthPickerContainer" class="w-full mt-40"
			style="display: none;">
			<div class="flex justify-center">
				<div class="inline-block bg-white rounded-lg p-5">
					<!-- Month picker content here -->
					<form action="GenerateReportServlet" method="post">
						<label>Choose a month:</label> <input type="month"
							name="selectedMonth" id="monthPicker"> <br> <input
							class="bg-gray-300 hover:bg-gray-500 p-2 rounded-lg"
							type="submit" value="Generate Report">
						<button type="button"
							class="bg-gray-300 hover:bg-gray-500 p-2 rounded-lg ml-2"
							id="backToDateSelection2">Back</button>
					</form>
				</div>
			</div>
		</div>

		<div id="periodPickerContainer" class="w-full mt-40"
			style="display: none;">
			<!-- Period picker content here -->
			<div class="flex justify-center">
				<div class="inline-block bg-white rounded-lg p-5">
					<form action="GenerateReportServlet" method="post">
						<label>Enter start date:</label> <input type="date"
							name="startDate" id="startDatePicker"> <br> <label>Enter
							end date:</label> <input type="date" name="endDate" id="endDatePicker">
						<br> <input
							class="bg-gray-300 hover:bg-gray-500 p-2 rounded-lg"
							type="submit" value="Generate Report">
						<button type="button"
							class="bg-gray-300 hover:bg-gray-500 p-2 rounded-lg ml-2"
							id="backToDateSelection3">Back</button>
					</form>
				</div>
			</div>
		</div>
		<!-- 
		<div class="bg-white mx-auto m-5 h-screen overflow-y-auto"
			style="width: 411px;">
			
		</div>
-->
		<script>
			// Get references to the containers
			const initialForm = document.getElementById("initialForm");
			const datePickerContainer = document.getElementById("datePickerContainer");
			const monthPickerContainer = document.getElementById("monthPickerContainer");
			const periodPickerContainer = document.getElementById("periodPickerContainer");

			// Get reference to the next button
			const nextButton = document.getElementById("nextButton");
			 const backButton1 = document.getElementById("backToDateSelection1");
			 const backButton2 = document.getElementById("backToDateSelection2");
			 const backButton3 = document.getElementById("backToDateSelection3");
			    backButton1.addEventListener("click", () => {
			    	location.reload();
			    });
			    backButton2.addEventListener("click", () => {
			    	location.reload();
			    });
			    backButton3.addEventListener("click", () => {
			    	location.reload();
			    });
			// Add event listener to the next button
			nextButton.addEventListener("click", () => {
				const reportType = document.getElementById("reportTypeSelector").value;

				// Hide the initial form
				initialForm.style.display = "none";

				// Hide all containers by default
				datePickerContainer.style.display = "none";
				monthPickerContainer.style.display = "none";
				periodPickerContainer.style.display = "none";

				// Show the container based on the selected report type
				if (reportType === "byDate") {
					datePickerContainer.style.display = "block";
				} else if (reportType === "byMonth") {
					monthPickerContainer.style.display = "block";
				} else if (reportType === "byPeriod") {
					periodPickerContainer.style.display = "block";
				}
			});
		</script>
	</div>
</body>
</html>
