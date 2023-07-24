<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Inkwell: Profile</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/publicAndCustomer/css/profilePage.css">
<script src="https://kit.fontawesome.com/8c8a7e5c88.js"
	crossorigin="anonymous"></script>
</head>
<body>
	<%@ page import="java.util.*, model.*"%>
	<%
	User user = (User) request.getAttribute("user");
	String image = user.getImage() == null
			? request.getContextPath() + "/publicAndCustomer/img/defaultUserPFP.png"
			: "data:image/png;base64, " + user.getImage();

	String statusCode = request.getParameter("statusCode");
	%>
	<%@include file="navBar/headerNavCustomer.jsp"%>
	<div class="flex flex-col justify-center items-center pb-10 my-8 mx-48">
		<img class="w-24 h-24 mb-3 rounded-full shadow-lg" src="<%=image%>"
			alt="Profile Picture" />
		<h5 class="mb-1 text-xl font-medium text-gray-900"><%=user.getName()%></h5>
		<span class="text-sm text-gray-500"><%=user.getEmail()%></span>
		<div class="flex mt-4 space-x-3 md:mt-6">
			<a
				href="<%=request.getContextPath()%>/EditProfile?userID=<%=user.getUserID()%>"
				class="editProfileButton inline-flex items-center px-4 py-2 text-sm font-medium text-center rounded-lg focus:ring-4 focus:outline-none focus:ring-blue-300">Edit
				Profile</a> <a
				href="<%=request.getContextPath()%>/ChangePassword?userID=<%=user.getUserID()%>"
				class="editProfileButton inline-flex items-center px-4 py-2 text-sm font-medium text-center rounded-lg focus:ring-4 focus:outline-none focus:ring-blue-300">Change
				Password</a>
				
				<a
				href="<%=request.getContextPath()%>/ModifyAddressPage?userIDAvailable=true&from=profile"
				class="editProfileButton inline-flex items-center px-4 py-2 text-sm font-medium text-center rounded-lg focus:ring-4 focus:outline-none focus:ring-blue-300">Modify Address</a>
				
				 <a href="ReviewHistory?userIDAvailable=true"
				class="editProfileButton inline-flex items-center px-4 py-2 text-sm font-medium text-center rounded-lg focus:ring-4 focus:outline-none focus:ring-blue-300">Review
				History</a> <a href="TransactionHistoryPage?userIDAvailable=true"
				class="editProfileButton inline-flex items-center px-4 py-2 text-sm font-medium text-center rounded-lg focus:ring-4 focus:outline-none focus:ring-blue-300">Transaction
				History</a>

		</div>
		<div class="flex mt-4 space-x-3 md:mt-6">
			<a id="toggleButton"
				class="deleteAccountButton inline-flex items-center px-4 py-2 text-sm font-medium text-center rounded-lg focus:ring-4 focus:outline-none focus:ring-gray-200 hover:cursor-pointer">Delete
				Account</a>
		</div>
	</div>
	</div>

	<div class="my-3">
		<h1 id="changePasswordErrorMsg" class="tracking-wide hidden"></h1>
		<%
		if (statusCode != null) {
			if (statusCode.equals("500")) {
		%>
		<h1 class="editProfileErrorMsg tracking-wide">Uh-oh! Error</h1>
		<%
		}
		}
		%>
	</div>

	<!-- Modal for confirm delete -->
	<div id="modal"
		class="fixed inset-0 flex items-center justify-center z-50 hidden">
		<div class="bg-white p-8 rounded shadow-lg rounded-lg">
			<h2 class="text-2xl m-0 p-0">Are you sure you want</h2>
			<h2 class="text-2xl m-0 p-0">
				to <span class="font-semibold text-pink-200">Delete</span> your
				account?
			</h2>
			<div class="flex justify-between mt-5">
				<form id="deleteForm" method="post"
					action="<%=request.getContextPath()%>/DeleteAccount">
					<input type="hidden" id="userID" name="userID"
						value="<%=user.getUserID()%>">
					<button type="submit" class="">
						<span
							class="deleteAccountButton inline-flex items-center px-4 py-2 text-sm font-medium text-center rounded-lg focus:ring-4 focus:outline-none focus:ring-gray-200 hover:cursor-pointer">
							Delete Account </span>
					</button>
				</form>
				<a id="closeButton" class="hover:cursor-pointer"> <span
					class="editProfileButton inline-flex items-center px-4 py-2 text-sm font-medium text-center rounded-lg focus:ring-4 focus:outline-none focus:ring-blue-300">
						Cancel</span>
				</a>
			</div>
		</div>
	</div>
</body>

<script>
	const deleteButton = document.getElementById('toggleButton');
	
	deleteButton.addEventListener("click", () => {
		document.getElementById("modal").classList.toggle("hidden");
	});
	
	document.getElementById("closeButton").addEventListener("click",
			function() {
				document.getElementById("modal").classList.add("hidden");
			});
</script>
</html>