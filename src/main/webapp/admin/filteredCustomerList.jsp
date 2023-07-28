<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%@ include file="../tailwind-css.jsp"%>
<%@ page import="java.util.*, model.CustomerListByBooks, model.Book"%>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
<title>Filtered Customer List</title>
</head>
<body>
	<%@ include file="modal.jsp"%>
	<%
	boolean error = false;
	List<CustomerListByBooks> listOfCustomerByBookID = null;
	Book bookDetails = null;
	try {
		listOfCustomerByBookID = (List<CustomerListByBooks>) request.getAttribute("listOfCustomerByBookID");
		bookDetails = (Book) request.getAttribute("bookDetails");
		System.out.println(bookDetails);
		System.out.println(listOfCustomerByBookID);
	} catch (ClassCastException e) {
		error = true;
	}

	if (listOfCustomerByBookID != null && bookDetails != null && !error) {
	%>
	<%@ include file="./navbar.jsp"%>
	<div class="container bg-slate-600 py-20">
		<div class="flex justify-between">
			<div class="flex items-center space-x-2 px-4 mb-4">
				<a
					href="<%=request.getContextPath()%>/admin/FilterCustomersByBookMain"
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
						<div style="font-size: 11px;">
							<h2 class="font-semibold mb-2">Customer List That Purchased:</h2>
							<p>
								Book's Name:
								<%=bookDetails.getTitle()%>
							</p>
							<p>
								Book's ISBN NO:
								<%=bookDetails.getISBN()%>
							</p>
							<p>
								Total Books Sold:
								<%=bookDetails.getSold()%>
							</p>
							<p>
								Total Customer:
								<%=listOfCustomerByBookID.size()%>
							</p>
						</div>
					</div>
				</div>
				<hr class="my-2 border-gray-400">
				<div class="flex justify-center mb-5">
					<table
						class="w-full text-sm text-left text-gray-500 dark:text-gray-400">
						<thead
							class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
							<tr>
								<th scope="col" class="px-6 py-2">User</th>
								<th scope="col" class="px-6 py-2">Transaction History User
									Bought Book</th>
								<th scope="col" class="px-6 py-2">Quantity Sold</th>
							</tr>
						</thead>
						<tbody>
							<%
							for (CustomerListByBooks customerWithBookPurchased : listOfCustomerByBookID) {
							%>
							<tr class="bg-white border-b hover:bg-gray-50">
								<td scope="row"
									class="flex items-center px-6 py-4 text-gray-900 whitespace-nowrap dark:text-white">
									<div class="h-5 w-5">
										<%
										if (customerWithBookPurchased.getUserDetails().getImage() != null) {
										%>
										<img
											src="<%=customerWithBookPurchased.getUserDetails().getImage()%>"
											class="rounded-full object-contain">
										<%
										} else {
										%>
										<i class="fas fa-user-circle text-gray-400 text-3xl"></i>
										<%
										}
										%>
									</div>
									<div class="pl-5 text-xs">
										<div class="text-base font-semibold">
											<%=customerWithBookPurchased.getUserDetails().getName()%>
										</div>
										<div class="font-normal text-gray-500">
											<%=customerWithBookPurchased.getUserDetails().getEmail()%>
										</div>
									</div>
								</td>
								<td class="px-6 py-4">
									<ul>
										<%
										for (int i = 0; i < customerWithBookPurchased.getTransactionDates().size(); i++) {
										%>
										<li><%="Bought " + customerWithBookPurchased.getQuantityPurchased().get(i) + " Books on "+ customerWithBookPurchased.getTransactionDates().get(i)%>
										</li>
										<%
										}
										%>
									</ul>
								</td>
								<td class="px-6 py-4">
									<%
									int totalQuantityBought = customerWithBookPurchased.getQuantityPurchased().stream().mapToInt(Integer::intValue).sum();
									%>
									<%=totalQuantityBought%>
								</td>
							</tr>
							<%
							}
							%>
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<script>
        function printReport() {
            const printContent = document.getElementById('printdiv').outerHTML;
            const originalContent = document.body.innerHTML;
            document.body.innerHTML = printContent;
            window.print();
            document.body.innerHTML = originalContent;
        }
    </script>
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
