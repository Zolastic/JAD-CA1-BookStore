<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: Publisher Details</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/admin/css/objectDetails.css">
<script src="https://kit.fontawesome.com/8c8a7e5c88.js"
	crossorigin="anonymous"></script>
</head>
<body class="my-8 mx-48">
	<%@ page import="java.util.*, model.*"%>
	<%@include file="./navbar.jsp"%>
	<%
	Publisher publisher = (Publisher) request.getAttribute("publisher");
	List<Book> books = (List<Book>) request.getAttribute("books");
	%>

	<div class="flex rounded-lg shadow-lg bg-gray-50 pt-3 pb-96 mt-28">
		<div class="flex flex-col ml-10">
			<h1 class="text-3xl font-bold tracking-wide"><%=publisher.getName()%>
			</h1>
			<h3 class="mt-5 -mb-1">
				Books
				<%=publisher.getName()%>
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
			<p class="mt-2 text-sm">This publisher has no books in the store</p>
			<%
			}
			%>

		</div>
		<div class="flex-grow"></div>
		<div class="flex">
			<a
				href="<%=request.getContextPath()%>/admin/EditPublisher?publisherID=<%=publisher.getId()%>"><i
				class="objectDetailsIcons fa-solid fa-pencil fa-lg mx-3 hover:cursor-pointer"></i></a>
			<a class="m-0 p-0 toggleButton"
				data-publisher-id="<%=publisher.getId()%>"
				data-publisher-name="<%=publisher.getName()%>"> <i
				class="objectDetailsIcons fa-solid fa-trash fa-lg mx-3 hover:cursor-pointer"></i>
			</a>
		</div>
	</div>

	<!-- Modal for confirm delete -->
	<div id="modal"
		class="fixed inset-0 flex items-center justify-center z-50 hidden">
		<div class="bg-white p-8 rounded shadow-lg rounded-lg">
			<h2 class="text-2xl m-0 p-0">Are you sure you want</h2>
			<h2 class="text-2xl m-0 p-0">
				to Delete <span id="publisherTitle"
					class="m-0 p-0 text-2xl font-bold"></span>
			</h2>
			<div class="flex mt-5">
				<form id="deleteForm" method="post"
					action="<%=request.getContextPath()%>/admin/DeletePublisher">
					<input type="hidden" id="publisherID" name="publisherID" value="">
					<button type="submit"
						class="relative inline-flex items-center justify-center p-0.5 mb-2 mr-2 overflow-hidden text-sm font-medium text-gray-900 rounded-lg group bg-gradient-to-br from-purple-500 to-pink-500 group-hover:from-purple-500 group-hover:to-pink-500 hover:text-white focus:ring-4 focus:outline-none focus:ring-purple-200">
						<span
							class="relative px-5 py-2.5 transition-all ease-in duration-75 bg-white rounded-md group-hover:bg-opacity-0 text-black hover:cursor-pointer">
							Delete Publisher </span>
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
								const publisherID = this
										.getAttribute("data-publisher-id");
								const publisherName = this
										.getAttribute("data-publisher-name");

								document.getElementById("publisherTitle").textContent = publisherName;
								document.getElementById("publisherID").value = publisherID;

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