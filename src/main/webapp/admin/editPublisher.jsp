<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: Edit Publisher</title>
<%@include file="../tailwind-css.jsp"%>

</head>
<body class="my-8 mx-48">
	<%@ page import="java.util.*, model.*"%>
	<%@include file="./navbar.jsp"%>
	<%
	Publisher publisher = (Publisher) request.getAttribute("publisher");
	%>
	<h1 class="text-2xl font-bold tracking-wide mt-28 mb-8 p-0">Edit Author (<%= publisher.getName() %>)</h1>
	<form class="mt-28"
		action="<%=request.getContextPath()%>/admin/EditPublisher" method="post">

		<input type="text" name="publisherID" id="publisherID"
			value="<%=publisher.getId()%>" class="hidden" placeholder=" " required />

		<!-- name -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="text" name="name" id="name"
				value="<%=publisher.getName()%>"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
				placeholder=" " required /> <label for="name"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Name</label>
		</div>

		<button type="submit"
			class="text-amber-800 bg-pink-100 hover:bg-pink-200 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center">Save
			Changes!</button>
	</form>
</body>
</html>