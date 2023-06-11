<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: Homepage</title>
<%@include file="../tailwind-css.jsp"%>
</head>
<body>
	<%@include file="./navbar.jsp"%>
	<div class="flex justify-center items-center mt-28">
		<h1 class="font-bold text-3xl">Admin Centre</h1>
	</div>

	<main class="flex justify-center items-center mt-10">
		<a href="<%=request.getContextPath()%>/admin/ViewBooks"
			class="relative inline-flex items-center justify-center p-0.5 mb-2 mr-2 overflow-hidden text-sm font-medium text-gray-900 rounded-lg group bg-gradient-to-br from-purple-500 to-pink-500 group-hover:from-purple-500 group-hover:to-pink-500 hover:text-white focus:ring-4 focus:outline-none focus:ring-purple-200">
			<span
			class="relative px-5 py-2.5 transition-all ease-in duration-75 bg-white rounded-md group-hover:bg-opacity-0 text-black">
				View Books</span>
		</a> <a href="<%=request.getContextPath()%>/admin/AddBook"
			class="relative inline-flex items-center justify-center p-0.5 mb-2 mr-2 overflow-hidden text-sm font-medium text-gray-900 rounded-lg group bg-gradient-to-br from-purple-500 to-pink-500 group-hover:from-purple-500 group-hover:to-pink-500 hover:text-white focus:ring-4 focus:outline-none focus:ring-purple-200">
			<span
			class="relative px-5 py-2.5 transition-all ease-in duration-75 bg-white rounded-md group-hover:bg-opacity-0 text-black">
				Add Books </span>
		</a>
	</main>

</body>
</html>