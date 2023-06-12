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
<body class="my-8 mx-48">
	<%@ page import="java.util.*, model.*"%>
	<%
		User user = (User) request.getAttribute("user");
		String image = user.getImage() == null ? request.getContextPath() + "/publicAndCustomer/img/defaultUserPFP.png" : "data:image/png;base64, " + user.getImage();
	%>

		<div class="flex flex-col justify-center items-center pb-10">
			<img class="w-24 h-24 mb-3 rounded-full shadow-lg"
				src="<%= image %>" alt="Profile Picture" />
			<h5 class="mb-1 text-xl font-medium text-gray-900"><%= user.getName() %></h5>
			<span class="text-sm text-gray-500"><%= user.getEmail() %></span>
			<div class="flex mt-4 space-x-3 md:mt-6">
				<a href="<%= request.getContextPath() %>/EditProfile?userID=<%= user.getUserID() %>"
					class="editProfileButton inline-flex items-center px-4 py-2 text-sm font-medium text-center rounded-lg focus:ring-4 focus:outline-none focus:ring-blue-300">Edit Profile</a> 
					<a href="#"
					class="editProfileButton inline-flex items-center px-4 py-2 text-sm font-medium text-center rounded-lg focus:ring-4 focus:outline-none focus:ring-blue-300">Change Password</a>
					<a href="#"
					class="editProfileButton inline-flex items-center px-4 py-2 text-sm font-medium text-center rounded-lg focus:ring-4 focus:outline-none focus:ring-blue-300">Review History</a> 
					<a href="#"
					class="editProfileButton inline-flex items-center px-4 py-2 text-sm font-medium text-center rounded-lg focus:ring-4 focus:outline-none focus:ring-blue-300">Transaction History</a>  

			</div>
			<div class="flex mt-4 space-x-3 md:mt-6">
				<a href="#"
					class="deleteAccountButton inline-flex items-center px-4 py-2 text-sm font-medium text-center rounded-lg focus:ring-4 focus:outline-none focus:ring-gray-200">Delete Account</a> 
			</div>
		</div>
	</div>
</body>
</html>