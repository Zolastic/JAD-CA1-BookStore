<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: View User's Orders</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/admin/css/viewManagementSystem.css">
<script src="https://kit.fontawesome.com/8c8a7e5c88.js"
	crossorigin="anonymous"></script>
</head>
<body>
	<%@ page import="java.util.*, model.*"%>
	<%@include file="./navbar.jsp"%>
	<%
	User user = (User) request.getAttribute("user");
	List<TransactionHistoryWithItems> transactionHistoryWithItems = (List<TransactionHistoryWithItems>) request
			.getAttribute("transactionHistoryWithItems");
	String sCurrentPage = request.getParameter("page");

	if (sCurrentPage == null) {
		sCurrentPage = "1";
	}

	int iCurrentPage = Integer.parseInt(sCurrentPage);
	int itemsPerPage = 10;

	int startIndex = (iCurrentPage - 1) * itemsPerPage;
	int endIndex = Math.min(startIndex + itemsPerPage, transactionHistoryWithItems.size());

	List<TransactionHistoryWithItems> ordersPerPage = transactionHistoryWithItems.subList(startIndex, endIndex);

	int totalOrders = transactionHistoryWithItems.size();
	int totalPages = (int) Math.ceil((double) totalOrders / itemsPerPage);
	totalPages = totalPages == 0 ? 1 : totalPages;

	String userInput = request.getParameter("userInput");

	String pageURL = String.format("%s/admin/ViewUserOrders?%spage=", request.getContextPath(), "userID=" + user.getUserID() + "&");
	%>
	<header class="viewHeader mt-16">
		<div class="h-64 flex flex-col justify-center items-center">
			<h1 class="font-bold text-2xl my-2 tracking-wider">User
				Management System</h1>
			<form action="<%=request.getContextPath()%>/admin/ViewUsers"
				method="get" class="my-2">
				<input id="userInput" name="userInput" type="text"
					value="<%=userInput == null ? "" : userInput%>"
					class="w-[444px] h-10 px-5 py-3 text-lg rounded-full border-2 border-blue-300 focus:border-l-blue-300 outline-none transition text-greyAccent placeholder:text-gray-300"
					placeholder="Search for a user here!" />
			</form>
		</div>
		<div class="flex justify-end items-end pb-3">
			<a class="" href="<%=request.getContextPath()%>/admin/addUser.jsp"><i
				class="viewIcons fa-solid fa-plus fa-2xl mx-3 hover:cursor-pointer"></i></a>
		</div>
	</header>

	<div class="flex space-x-2">
		<div class="my-5 mx-10">
			<button id="dropdownDefaultButton" data-dropdown-toggle="dropdown"
				class="text-amber-800 bg-pink-100 hover:bg-pink-100 focus:ring-4 focus:outline-none focus:ring-pink-100 font-medium rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center"
				type="button">
				Filter By
				<svg class="w-2.5 h-2.5 ml-2.5" aria-hidden="true"
					xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 10 6">
    <path stroke="currentColor" stroke-linecap="round"
						stroke-linejoin="round" stroke-width="2" d="m1 1 4 4 4-4" />
  </svg>
			</button>
			<div id="dropdown"
				class="z-10 hidden bg-white divide-y divide-gray-100 rounded-lg shadow w-44">
				<ul class="py-2 text-sm text-gray-700"
					aria-labelledby="dropdownDefaultButton">
					<li><a href="<%=request.getContextPath()%>/admin/ViewUsers"
						class="block px-4 py-2 hover:bg-pink-100">Nothing</a></li>
					<li><a
						href="<%=request.getContextPath()%>/admin/ViewUserByPostalCode"
						class="block px-4 py-2 hover:bg-pink-100">Postal Code</a></li>
				</ul>
			</div>


		</div>

		<div class="flex flex-col grow">
			<div class="py-3 my-5 mx-10">
				<h1 class="text-[#926b6a] text-xl font-semibold"><%= user.getName() %> Orders:</h1>
			</div>
			<%
			if (transactionHistoryWithItems.size() > 0) {
				for (int i = 0; i < ordersPerPage.size(); i++) {
					TransactionHistoryWithItems transactionHistoryWithItem = ordersPerPage.get(i);
			%>
			<div class="flex py-3 my-5 mx-10 rounded-lg shadow-lg bg-gray-50">
				<div class="flex flex-col ml-10">
					<a class="hover:cursor-pointer hover:text-amber-900"
						href="<%=request.getContextPath()%>/admin/OrderDetails?transactionHistoryID=<%=transactionHistoryWithItem.getTransactionHistoryID()%>"><h1
							class="text-3xl font-bold">
							Order
							<%=i + 1%></h1></a>
					<table class="mt-2">
						<thead>
							<tr>
								<th></th>
								<th></th>
							</tr>
						</thead>
						<tr>
							<td class="mt-5 text-lg"><span class="text-[#926b6a] mr-2">Transaction
									Time:</span></td><td><%=transactionHistoryWithItem.getTransactionDate()%></td>
						</tr>
						<tr>
						<td class="mt-5 text-lg">
						<span class="text-[#926b6a] mr-2">Total Amount:</span></td>
						<td>$<%=transactionHistoryWithItem.getTotalAmount()%></td>
						</tr>
						<tr><td class="mt-5 text-lg">
						<span class="text-[#926b6a] mr-2">To:</span>
						</td>
						<td><%=transactionHistoryWithItem.getFullAddr()%></td>
						</tr>
					</table>
				</div>
				<div class="flex-grow"></div>
				<div class="flex">
					<a
						href="<%=request.getContextPath()%>/admin/EditOrder?transactionHistoryID=<%=transactionHistoryWithItem.getTransactionHistoryID()%>"><i
						class="viewIcons fa-solid fa-pencil fa-lg mx-3 hover:cursor-pointer"></i></a>
					<a class="m-0 p-0 toggleButton"
						data-order-id="<%=transactionHistoryWithItem.getTransactionHistoryID()%>"
						data-order-name="<%=transactionHistoryWithItem.getTransactionHistoryID()%>"
						data-order-userID="<%=transactionHistoryWithItem.getCustID()%>"
						>
						<i
						class="viewIcons fa-solid fa-trash fa-lg mx-3 hover:cursor-pointer"></i>
					</a>
				</div>
			</div>
			<%
			}
			} else {
			%>
			<div class="flex justify-center items-center mt-5">
				<h1 class="text-xl font-semibold">There are no orders made by this member!</h1>
			</div>
			<%
			}
			%>

			<!-- pagination -->
			<div class="flex justify-center items-center mb-2">
				<ul class="inline-flex items-center -space-x-px">
					<li><a href="<%=pageURL + (iCurrentPage - 1)%>"
						class="<%=iCurrentPage == 1 ? "paginationDisabled pointer-events-none" : "paginationEnabled"%> block px-3 py-2 ml-0 leading-tight border border-gray-300 rounded-l-lg">&laquo;</a></li>
					<li>
						<%
						for (int i = 1; i <= totalPages; i++) {
						%> <a href="<%=pageURL + i%>"
						class="paginationEnabled px-3 py-2 leading-tight border border-gray-300"><%=i%></a>
						<%
						}
						%>
					</li>
					<li><a href="<%=pageURL + (iCurrentPage + 1)%>"
						class="<%=iCurrentPage == totalPages ? "paginationDisabled pointer-events-none" : "paginationEnabled"%> block px-3 py-2 leading-tight border border-gray-300 rounded-r-lg">&raquo;</a></li>
				</ul>
			</div>


			<!-- Modal for confirm delete -->
			<div id="modal"
				class="fixed inset-0 flex items-center justify-center z-50 hidden">
				<div class="bg-white p-8 rounded shadow-lg rounded-lg">
					<h2 class="text-2xl m-0 p-0">Are you sure you want</h2>
					<h2 class="text-2xl m-0 p-0">
						to Delete Order: <span id="orderTitle" class="m-0 p-0 text-2xl font-bold"></span>
					</h2>
					<div class="flex mt-5">
						<form id="deleteForm" method="post"
							action="<%=request.getContextPath()%>/admin/DeleteOrder">
							<input type="hidden" id="transactionHistoryID"
								name="transactionHistoryID" value="" />
							<input type="hidden" id="userID"
								name="userID" value="" />
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
				const dropdownButton = document
						.getElementById('dropdownDefaultButton');
				const dropdownMenu = document.getElementById('dropdown');

				dropdownButton.addEventListener('click', function() {
					dropdownMenu.classList.toggle('hidden');
				});
			</script>

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
										document
												.getElementById("transactionHistoryID").value = transactionHistoryID;
										document
										.getElementById("userID").value = userID;

										document.getElementById("modal").classList
												.toggle("hidden");
									});
				}
				document.getElementById("closeButton").addEventListener(
						"click",
						function() {
							document.getElementById("modal").classList
									.add("hidden");
						});
			</script>
		</div>
	</div>

</body>
</html>
