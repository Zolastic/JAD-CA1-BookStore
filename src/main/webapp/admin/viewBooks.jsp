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
				<p class="mt-5 text-lg"><%=book.getDescription()%></p>
				<p class="mt-5 text-sm">
					Author:
					<%=book.getAuthor()%></p>
				<p class="mt-5 text-sm">
					Rating:
					<%=book.getRating()%></p>

			</div>
			<div class="flex-grow"></div>
			<div class="flex">
				<a
					href="<%=request.getContextPath()%>/EditBook?bookID=<%=book.getBookID()%>"><i
					class="viewBooksIcons fa-solid fa-pencil fa-lg mx-3 hover:cursor-pointer"></i></a>
				<a class="m-0 p-0 toggleButton" data-book-id="<%=book.getBookID()%>"
					data-book-title="<%=book.getTitle()%>"> <i
					class="viewBooksIcons fa-solid fa-trash fa-lg mx-3 hover:cursor-pointer"></i>
				</a>
			</div>
		</div>
		<%
		}
		%>
		<!-- Modal for confirm delete -->
		<div id="modal"
			class="fixed inset-0 flex items-center justify-center z-50 hidden">
			<div class="bg-white p-8 rounded shadow-lg rounded-lg">
				<h2 class="text-2xl m-0 p-0">Are you sure you want</h2>
				<h2 class="text-2xl m-0 p-0">
					to Delete
					<span id="bookTitle" class="m-0 p-0 text-2xl font-bold"></span>
				</h2>
				<div class="flex mt-5">
					<form id="deleteForm" method="post" action="<%=request.getContextPath()%>/DeleteBook">
						<input type="hidden" id="bookID" name="bookID" value="">
						<button type="submit" class="relative inline-flex items-center justify-center p-0.5 mb-2 mr-2 overflow-hidden text-sm font-medium text-gray-900 rounded-lg group bg-gradient-to-br from-purple-500 to-pink-500 group-hover:from-purple-500 group-hover:to-pink-500 hover:text-white focus:ring-4 focus:outline-none focus:ring-purple-200">
							<span class="relative px-5 py-2.5 transition-all ease-in duration-75 bg-white rounded-md group-hover:bg-opacity-0 text-black hover:cursor-pointer">
								Delete Book
							</span>
						</button>
					</form>
					<a	id="closeButton"
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
									const bookID = this
											.getAttribute("data-book-id");
									const bookTitle = this
											.getAttribute("data-book-title");
									
									document.getElementById("bookTitle").textContent = bookTitle;
									document.getElementById("bookID").value = bookID;
									
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

</body>
</html>
