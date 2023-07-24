<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: Order Details</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/admin/css/objectDetails.css">
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/admin/css/viewManagementSystem.css">
<script src="https://kit.fontawesome.com/8c8a7e5c88.js"
	crossorigin="anonymous"></script>
</head>
<body class="my-8 mx-48">
	<%@ page import="java.util.*, model.*"%>
	<%@include file="./navbar.jsp"%>
	<%
	TransactionHistory order = (TransactionHistory) request.getAttribute("order");
	List<TransactionHistoryItemBook> items = (List<TransactionHistoryItemBook>) request.getAttribute("items");
	%>

	<div class="flex rounded-lg shadow-lg bg-gray-50 pt-3 pb-96 mt-28">
		<div class="flex flex-col ml-10">
			<h1 class="text-3xl font-bold tracking-wide">
				Order No.: <span class="text-[#926b6a]"><%=order.getTransactionHistoryID()%></span>
			</h1>

			<table class="mt-4">
				<thead>
					<tr>
						<th></th>
						<th></th>
					</tr>
				</thead>
				<tr>
					<td class="mt-5 text-lg"><span class="text-[#926b6a] mr-2">Transaction
							Time:</span></td>
					<td><%=order.getTransactionDate()%></td>
				</tr>
				<tr>
					<td class="mt-5 text-lg"><span class="text-[#926b6a] mr-2">Total
							Amount:</span></td>
					<td>$<%=order.getTotalAmount()%></td>
				</tr>
				<tr>
					<td class="mt-5 text-lg"><span class="text-[#926b6a] mr-2">To:</span>
					</td>
					<td><%=order.getFullAddress()%></td>
				</tr>
			</table>

			<h3 class="mt-4 -mb-1">Books Ordered:</h3>
			<%
			if (items.size() > 0) {

				for (TransactionHistoryItemBook item : items) {
			%>
			<div class="flex space-x-8">
				<div class="w-[100px]">
					<a
						href="<%=request.getContextPath()%>/admin/BookDetails?bookID=<%=item.getBook().getBookID()%>"
						class="objectDetailsLinks mt-1 text-sm truncate block"><span
						class=""><%=item.getBook().getTitle()%></span> </a>
				</div>
				<div>
					<p>
						x<%=item.getQuantity()%></p>
				</div>
			</div>
			<%
			}
			} else {
			%>
			<p class="mt-2 text-sm">There are no books in this order!</p>
			<%
			}
			%>

		</div>
		<div class="flex-grow"></div>
		<div class="flex">
			<a
				href="<%=request.getContextPath()%>/admin/EditOrder?transactionHistoryID=<%=order.getTransactionHistoryID()%>"><i
				class="viewIcons fa-solid fa-pencil fa-lg mx-3 hover:cursor-pointer"></i></a>
			<a class="m-0 p-0 toggleButton"
				data-order-id="<%=order.getTransactionHistoryID()%>"
				data-order-name="<%=order.getTransactionHistoryID()%>"
				data-order-userID="<%=order.getCustomerID()%>"> <i
				class="viewIcons fa-solid fa-trash fa-lg mx-3 hover:cursor-pointer"></i>
			</a>
		</div>
	</div>

	<!-- Modal for confirm delete -->
	<div id="modal"
		class="fixed inset-0 flex items-center justify-center z-50 hidden">
		<div class="bg-white p-8 rounded shadow-lg rounded-lg">
			<h2 class="text-2xl m-0 p-0">Are you sure you want</h2>
			<h2 class="text-2xl m-0 p-0">
				to Delete Order: <span id="orderTitle"
					class="m-0 p-0 text-2xl font-bold"></span>
			</h2>
			<div class="flex mt-5">
				<form id="deleteForm" method="post"
					action="<%=request.getContextPath()%>/admin/DeleteOrder">
					<input type="hidden" id="transactionHistoryID"
						name="transactionHistoryID" value="" /> <input type="hidden"
						id="userID" name="userID" value="" />
					<button type="submit"
						class="relative inline-flex items-center justify-center p-0.5 mb-2 mr-2 overflow-hidden text-sm font-medium text-gray-900 rounded-lg group bg-gradient-to-br from-purple-500 to-pink-500 group-hover:from-purple-500 group-hover:to-pink-500 hover:text-white focus:ring-4 focus:outline-none focus:ring-purple-200">
						<span
							class="relative px-5 py-2.5 transition-all ease-in duration-75 bg-white rounded-md group-hover:bg-opacity-0 text-black hover:cursor-pointer">
							Delete Order </span>
					</button>
				</form>
				<a id="closeButton"
					class="relative inline-flex items-center justify-center p-0.5 mb-2 mr-2 overflow-hidden text-sm font-medium text-gray-900 rounded-lg group bg-gray-500 hover:bg-gray-700 hover:text-white focus:ring-4 focus:outline-none focus:ring-purple-200">
					<span
					class="relative px-5 py-2.5 transition-all ease-in duration-75 bg-white rounded-md group-hover:bg-opacity-0 text-black hover:cursor-pointer">
						Cancel</span>
				</a>
			</div>
		</div>
	</div>

	<script>
		const items = document.getElementsByClassName('toggleButton');
		for (let i = 0; i < items.length; i++) {
			items[i]
					.addEventListener(
							"click",
							function() {
								const transactionHistoryID = this
										.getAttribute("data-order-id");
								const orderName = this
										.getAttribute("data-order-name");
								const userID = this
										.getAttribute("data-order-userID");

								document.getElementById("orderTitle").textContent = orderName;
								document.getElementById("transactionHistoryID").value = transactionHistoryID;
								document.getElementById("userID").value = userID;

								document.getElementById("modal").classList
										.toggle("hidden");
							});
		}
		document.getElementById("closeButton").addEventListener("click",
				function() {
					document.getElementById("modal").classList.add("hidden");
				});
	</script>
</body>
</html>