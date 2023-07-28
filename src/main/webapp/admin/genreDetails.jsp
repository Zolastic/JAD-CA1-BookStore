<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: Genre Details</title>
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
	Genre genre = (Genre) request.getAttribute("genre");
	List<Book> books = (List<Book>) request.getAttribute("books");
	%>

	<div class="flex rounded-lg shadow-lg bg-gray-50 pt-3 pb-96 mt-28">
		<%
		if ((genre.getImg()) == null) {
		%>
		<img alt=""
			src="<%=request.getContextPath()%>/admin/img/No_Image_Available.jpg"
			class="viewImg rounded-lg mx-10 object-contain">
		<%
		} else {
		%>
		<img alt="" src="<%=genre.getImg()%>"
			class="viewImg rounded-lg mx-10 object-contain">
		<%
		}
		%>

		<div class="flex flex-col ml-10">
			<h1 class="text-3xl font-bold tracking-wide"><%=genre.getName()%>
			</h1>
			<h3 class="mt-5 -mb-1">
				Books
				<%=genre.getName()%>
				has in the store:
			</h3>
			<%
			if (books.size() > 0) {

				for (Book book : books) {
			%>
			<a
				href="<%=request.getContextPath()%>/admin/BookDetails?bookID=<%=book.getBookID()%>"
				class="objectDetailsLinks mt-1 text-sm"><span class="mx-2"><%=book.getTitle()%></span>
			</a>
			<%
			}
			} else {
			%>
			<p class="mt-2 text-sm">This genre has no books in the store</p>
			<%
			}
			%>

		</div>
		<div class="flex-grow"></div>
		<div class="flex">
			<a
				href="<%=request.getContextPath()%>/admin/EditGenre?genreID=<%=genre.getId()%>"><i
				class="viewIcons fa-solid fa-pencil fa-lg mx-3 hover:cursor-pointer"></i></a>
			<a class="m-0 p-0 toggleButton" data-genre-id="<%=genre.getId()%>"
				data-genre-name="<%=genre.getName()%>"> <i
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
				to Delete <span id="genreTitle" class="m-0 p-0 text-2xl font-bold"></span>
			</h2>
			<div class="flex mt-5">
				<form id="deleteForm" method="post"
					action="<%=request.getContextPath()%>/admin/DeleteGenre">
					<input type="hidden" id="genreID" name="genreID" value="">
					<button type="submit"
						class="relative inline-flex items-center justify-center p-0.5 mb-2 mr-2 overflow-hidden text-sm font-medium text-gray-900 rounded-lg group bg-gradient-to-br from-purple-500 to-pink-500 group-hover:from-purple-500 group-hover:to-pink-500 hover:text-white focus:ring-4 focus:outline-none focus:ring-purple-200">
						<span
							class="relative px-5 py-2.5 transition-all ease-in duration-75 bg-white rounded-md group-hover:bg-opacity-0 text-black hover:cursor-pointer">
							Delete Genre </span>
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
			items[i]
					.addEventListener(
							"click",
							function() {
								const genreID = this
										.getAttribute("data-genre-id");
								const genreName = this
										.getAttribute("data-genre-name");

								document.getElementById("genreTitle").textContent = genreName;
								document.getElementById("genreID").value = genreID;

								document.getElementById("modal").classList
										.toggle("hidden");
							});
		}
		document.getElementById("closeButton").addEventListener("click",
				function() {
					document.getElementById("modal").classList.add("hidden");
				});
	</script>
</body>
</html>