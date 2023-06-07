<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: View Books</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/admin/css/viewBooks.css">
<link rel="stylesheet"
	href="https://use.fontawesome.com/releases/v5.13.0/css/all.css"
	integrity="sha384-Bfad6CLCknfcloXFOyFnlgtENryhrpZCe29RTifKEixXQZ38WheV+i/6YWSzkz3V"
	crossorigin="anonymous" />
<script src="https://kit.fontawesome.com/8c8a7e5c88.js"
	crossorigin="anonymous"></script>
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
		<div class="flex py-3 mb-5 border border-y-amber-900">
			<img alt=""
				src="<%=request.getContextPath()%>/admin/img/harryPotter.jpg"
				class="viewBooksImg rounded-lg mx-10">
			<div class="flex flex-col ml-10">
				<h1 class="text-3xl font-bold"><%=book.getTitle()%></h1>
				<p class="mt-5"><%=book.getDescription()%></p>
			</div>
			<div class="flex-grow"></div>
			<div class="flex">
				<a href="#"><i class="fa-solid fa-pencil fa-lg mx-3 hover:cursor-pointer" style="color: #926b6a;"></i></a>
				<a href="#"><i class="fa-solid fa-trash fa-lg mx-3 hover:cursor-pointer" style="color: #926b6a;"></i></a>
			</div>
		</div>
		<%
		}
		%>
	</div>

</body>
</html>