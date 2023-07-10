<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: Edit User Profile</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/admin/css/feedbackMessages.css">

</head>
<body class="my-8 mx-48">
	<%@ page import="java.util.*, model.*"%>
	<%@include file="./navbar.jsp"%>
	<%
	User user = (User) request.getAttribute("user");
	%>
	<h1 class="text-2xl font-bold tracking-wide mt-28 mb-8 p-0">Edit User (<%= user.getName() %>)</h1>
	<form class="mt-28"
		action="<%=request.getContextPath()%>/admin/EditUserProfile" method="post">

		<input type="text" name="userID" id="userID"
			value="<%=user.getUserID()%>" class="hidden" placeholder=" " required />

		<!-- name -->
			<div class="relative z-0 w-full mt-5 mb-8 group">
				<input type="text" name="name" id="name" value="<%=user.getName()%>"
					class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
					placeholder=" " required /> <label for="name"
					class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Name</label>
			</div>

			<!-- email -->
			<div class="relative z-0 w-full mb-8 group">
				<input type="email" name="email" id="email"
					value="<%=user.getEmail()%>"
					class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
					placeholder=" " required /> <label for="email"
					class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Email</label>
			</div>
		
		<div class="-mt-1 mb-2">
			<%
			String statusCode = request.getParameter("statusCode");
			if (statusCode != null) {
				if (statusCode.equals("200")) {
			%>
			<h1 class="successMessage">User successfully updated!</h1>
			<%
			} else {
			%>
			<h1 class="errorMessage">Uh-oh! Error</h1>
			<%
			}
			}
			%>
		</div>

		<button type="submit"
			class="text-amber-800 bg-pink-100 hover:bg-pink-200 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center">Save
			Changes!</button>
	</form>
</body>
</html>