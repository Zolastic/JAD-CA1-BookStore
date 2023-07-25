<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Generate Sales Report</title>
<%@include file="../tailwind-css.jsp"%>
<%@ page import="java.util.*, model.Book"%>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
</head>
<body>
	<%
	List<Book> allBooks = (List<Book>) request.getAttribute("allBooks");
	%>
	<%@include file="./navbar.jsp"%>
	<div class="container bg-slate-600 py-20 min-h-screen">
		<div class="flex justify-end">
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
		<div class="relative overflow-x-auto shadow-md sm:rounded-lg m-5 mx-20">
			<table
				class="w-full text-sm text-left text-gray-500 dark:text-gray-400" >
				<thead
					class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
					<tr>
						<th scope="col" class="px-6 py-3">Book Title</th>
						<th scope="col" class="px-6 py-3">ISBN Number</th>
						<th scope="col" class="px-6 py-3">Author</th>
						<th scope="col" class="px-6 py-3">Price</th>
						<th scope="col" class="px-6 py-3"><span class="sr-only">View
								Customer List</span></th>
					</tr>
				</thead>
				<tbody>
					<%
					for (Book book : allBooks) {
					%>
					<tr
						class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600">
						<td
							class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white">
							<%=book.getTitle()%>
						</td>
						<td class="px-6 py-4"><%=book.getISBN()%></td>
						<td class="px-6 py-4"><%=book.getAuthor()%></td>
						<td class="px-6 py-4">$<%=book.getPrice()%>
						</td>
						<td class="px-6 py-4 text-right"><a href="#"
							class="font-medium text-blue-600 dark:text-blue-500 hover:underline">
								View Purchased Customer List </a></td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>
		</div>
	</div>
</body>
</html>
