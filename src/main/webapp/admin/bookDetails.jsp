<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: Book Details</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/admin/css/bookDetails.css">
<script src="https://kit.fontawesome.com/8c8a7e5c88.js"
	crossorigin="anonymous"></script>
</head>
<body class="my-8 mx-48">
	<%@ page import="java.util.*, model.*"%>
	<%@include file="./navbar.jsp"%>
	<%
	Book book = (Book) request.getAttribute("book");
	%>

	<div class="flex rounded-lg shadow-lg bg-gray-50 pt-3 pb-96 mt-28">
		<%
		if ((book.getImg()) == null) {
		%>
		<img alt=""
			src="<%=request.getContextPath()%>/admin/img/No_Image_Available.jpg"
			class="viewBooksImg rounded-lg mx-10 object-contain">
		<%
		} else {
		%>
		<img alt="" src="data:image/png;base64, <%=book.getImg()%>"
			class="viewBooksImg rounded-lg mx-10 object-contain">
		<%
		}
		%>
		<div class="flex flex-col ml-10">
			<h1 class="text-3xl font-bold tracking-wide"><%=book.getTitle()%>
				(<%=book.getISBN()%>)
			</h1>
			<p class="mt-5 text-lg font-semibold"><%=book.getDescription()%></p>
			<p class="mt-5 text-lg font-semibold">
				Price: $<%=book.getPrice()%></p>

			<h3 class="mt-5 -mb-1">Book Details:</h3>
			<p class="mt-1 text-sm">
				Author: <span class="mx-2"><%=book.getAuthor()%></span>
			</p>
			<p class="mt-1 text-sm">
				Publisher: <span class="mx-2"><%=book.getPublisher()%></span>
			</p>
			<p class="mt-1 text-sm">
				Publication Date: <span class="mx-2"><%=book.getPublication_date()%></span>
			</p>

			<h3 class="mt-5 -mb-1">Book Stats:</h3>
			<%
			if (book.getRating() > 0) {
			%>
			<p class="mt-1 text-sm">
				Rating:
				<%=book.getRating()%></p>
			<%
			} else {
			%>
			<p class="mt-1 text-sm">Rating: no ratings</p>
			<%
			}
			%>
			<p class="mt-1 text-sm">
				Sold:
				<%=book.getSold()%></p>
			<p class="mt-1 text-sm">
				Quantity:
				<%=book.getInventory()%></p>

		</div>
		<div class="flex-grow"></div>
		<div class="flex">
			<a
				href="<%=request.getContextPath()%>/EditBook?bookID=<%=book.getBookID()%>"><i
				class="bookDetialsIcons fa-solid fa-pencil fa-lg mx-3 hover:cursor-pointer"></i></a>
			<a class="m-0 p-0 toggleButton" data-book-id="<%=book.getBookID()%>"
				data-book-title="<%=book.getTitle()%>"> <i
				class="bookDetialsIcons fa-solid fa-trash fa-lg mx-3 hover:cursor-pointer"></i>
			</a>
		</div>
	</div>

	<!-- Modal for confirm delete -->
	<div id="modal"
		class="fixed inset-0 flex items-center justify-center z-50 hidden">
		<div class="bg-white p-8 rounded shadow-lg rounded-lg">
			<h2 class="text-2xl m-0 p-0">Are you sure you want</h2>
			<h2 class="text-2xl m-0 p-0">
				to Delete <span id="bookTitle" class="m-0 p-0 text-2xl font-bold"></span>
			</h2>
			<div class="flex mt-5">
				<form id="deleteForm" method="post"
					action="<%=request.getContextPath()%>/DeleteBook">
					<input type="hidden" id="bookID" name="bookID" value="">
					<button type="submit"
						class="relative inline-flex items-center justify-center p-0.5 mb-2 mr-2 overflow-hidden text-sm font-medium text-gray-900 rounded-lg group bg-gradient-to-br from-purple-500 to-pink-500 group-hover:from-purple-500 group-hover:to-pink-500 hover:text-white focus:ring-4 focus:outline-none focus:ring-purple-200">
						<span
							class="relative px-5 py-2.5 transition-all ease-in duration-75 bg-white rounded-md group-hover:bg-opacity-0 text-black hover:cursor-pointer">
							Delete Book </span>
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
				const bookID = this.getAttribute("data-book-id");
				const bookTitle = this.getAttribute("data-book-title");

				document.getElementById("bookTitle").textContent = bookTitle;
				document.getElementById("bookID").value = bookID;

				document.getElementById("modal").classList.toggle("hidden");
			});
		}
		document.getElementById("closeButton").addEventListener("click",
				function() {
					document.getElementById("modal").classList.add("hidden");
				});
	</script>
	</div>
</body>
</html>