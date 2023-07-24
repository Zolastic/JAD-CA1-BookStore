<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: User Details</title>
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
	User user = (User) request.getAttribute("user");
	// TransactionHistoryWithItems transactionHistoryWithItems = (TransactionHistoryWithItems) request.getAttribute("transactionHistoryWithItems");
	%>

	<div class="flex rounded-lg shadow-lg bg-gray-50 pt-3 pb-96 mt-28">
		<%
		if ((user.getImage()) == null) {
		%>
		<img alt=""
			src="<%=request.getContextPath()%>/admin/img/defaultUserPFP.png"
			class="viewImg rounded-full mx-10 w-48 h-48 object-contain">
		<%
		} else {
		%>
		<img alt="" src="data:image/png;base64, <%=user.getImage()%>"
			class="viewImg rounded-full mx-10 w-48 h-48 object-contain">
		<%
		}
		%>
		<div class="flex flex-col ml-10">
			<h1 class="text-3xl font-bold tracking-wide"><%=user.getName()%>
			</h1>
			<p class="mt-5 text-lg font-semibold"><%=user.getEmail()%></p>
		</div>
		<div class="flex-grow"></div>
		<div class="flex">
			<a
				href="<%=request.getContextPath()%>/admin/ViewUserOrders?userID=<%=user.getUserID()%>"><i
				class="viewIcons fa-solid fa-box fa-lg mx-3 hover:cursor-pointer"></i></a>
			<a
				href="<%=request.getContextPath()%>/admin/EditUserProfile?userID=<%=user.getUserID()%>"><i
				class="viewIcons fa-solid fa-pencil fa-lg mx-3 hover:cursor-pointer"></i></a>
			<a
				href="<%=request.getContextPath()%>/admin/EditUserPassword?userID=<%=user.getUserID()%>"><i
				class="viewIcons fa-solid fa-key fa-lg mx-3 hover:cursor-pointer"></i></a>
			<a class="m-0 p-0 toggleButton" data-user-id="<%=user.getUserID()%>"
				data-user-name="<%=user.getName()%>"> <i
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
				to Delete <span id="userTitle" class="m-0 p-0 text-2xl font-bold"></span>
			</h2>
			<div class="flex mt-5">
				<form id="deleteForm" method="post"
					action="<%=request.getContextPath()%>/admin/DeleteUser">
					<input type="hidden" id="userID" name="userID" value="">
					<button type="submit"
						class="relative inline-flex items-center justify-center p-0.5 mb-2 mr-2 overflow-hidden text-sm font-medium text-gray-900 rounded-lg group bg-gradient-to-br from-purple-500 to-pink-500 group-hover:from-purple-500 group-hover:to-pink-500 hover:text-white focus:ring-4 focus:outline-none focus:ring-purple-200">
						<span
							class="relative px-5 py-2.5 transition-all ease-in duration-75 bg-white rounded-md group-hover:bg-opacity-0 text-black hover:cursor-pointer">
							Delete User </span>
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
			items[i].addEventListener("click", function() {
				const userID = this.getAttribute("data-user-id");
				const userName = this.getAttribute("data-user-name");

				document.getElementById("userTitle").textContent = userName;
				document.getElementById("userID").value = userID;

				document.getElementById("modal").classList.toggle("hidden");
			});
		}
		document.getElementById("closeButton").addEventListener("click",
				function() {
					document.getElementById("modal").classList.add("hidden");
				});
	</script>
</body>
</html>