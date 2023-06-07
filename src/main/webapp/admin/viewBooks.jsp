<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: View Books</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet" href="<%= request.getContextPath()%>/admin/css/viewBooks.css">
</head>
<body>
	<%@ page import="java.util.*, model.*"%>
	
	<div class="viewBooksHeader h-64 flex justify-center items-center">
		<h1 class="font-bold text-2xl">Book Management System</h1>
	</div>

	<div class="flex flex-col">
		<%
		List<Book> books = (List<Book>) request.getAttribute("books");
		for (Book book : books) {
		%>
		<div class="flex my-10 border border-y-amber-900">
			<img alt=""
				src="<%=request.getContextPath()%>/admin/img/harryPotter.jpg" class="viewBooksImg rounded-lg mx-10">
			<div class="flex flex-col ml-10">
				<h1 class="text-3xl font-bold"><%=book.getTitle()%></h1>
				<p class="mt-5"><%=book.getDescription()%></p>
			</div>
		</div>
		<%
		}
		%>
	</div>

</body>
</html>