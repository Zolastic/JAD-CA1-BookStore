<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: View Books</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/admin/css/viewManagementSystem.css">
<script src="https://kit.fontawesome.com/8c8a7e5c88.js"
	crossorigin="anonymous"></script>
<script src="<%=request.getContextPath()%>/print.js"></script>
</head>
<body>
	<%@ page import="java.util.*, model.*"%>
	<%@include file="./navbar.jsp"%>
	<%
	List<Book> books = (List<Book>) request.getAttribute("books");
	String sCurrentPage = request.getParameter("page");

	if (sCurrentPage == null) {
		sCurrentPage = "1";
	}

	int iCurrentPage = Integer.parseInt(sCurrentPage);
	int itemsPerPage = 10;

	int startIndex = (iCurrentPage - 1) * itemsPerPage;
	int endIndex = Math.min(startIndex + itemsPerPage, books.size());

	List<Book> booksPerPage = books.subList(startIndex, endIndex);

	int totalBooks = books.size();
	int totalPages = (int) Math.ceil((double) totalBooks / itemsPerPage);
	totalPages = totalPages == 0 ? 1 : totalPages;

	String userInput = request.getParameter("userInput");

	String pageURL = String.format("%s/admin/ViewBooksWithLowStock?page=", request.getContextPath(),
			userInput == null ? "" : "userInput=" + userInput + "&");
	%>
	<header class="viewHeader mt-16">
		<div class="h-64 flex flex-col justify-center items-center">
			<h1 class="font-bold text-2xl my-2 tracking-wider">Book
				Management System</h1>
			<form action="<%=request.getContextPath()%>/admin/ViewBooks"
				method="get" class="my-2">
				<input id="userInput" name="userInput" type="text"
					value="<%=userInput == null ? "" : userInput%>"
					class="w-[444px] h-10 px-5 py-3 text-lg rounded-full border-2 border-blue-300 focus:border-l-blue-300 outline-none transition text-greyAccent placeholder:text-gray-300"
					placeholder="Search for a book here!" />
			</form>
		</div>
		<div class="flex justify-end items-end pb-3">
			<a class="" href="<%=request.getContextPath()%>/admin/AddBook"><i
				class="viewIcons fa-solid fa-plus fa-2xl mx-3 hover:cursor-pointer"></i></a>
		</div>
	</header>

	<div class="flex space-x-2">
		<div class="my-5 mx-10">
			<button id="dropdownDefaultButton" data-dropdown-toggle="dropdown"
				class="text-amber-800 bg-pink-100 hover:bg-pink-100 focus:ring-4 focus:outline-none focus:ring-pink-100 font-medium rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center"
				type="button">
				Filter By
				<svg class="w-2.5 h-2.5 ml-2.5" aria-hidden="true"
					xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 10 6">
    <path stroke="currentColor" stroke-linecap="round"
						stroke-linejoin="round" stroke-width="2" d="m1 1 4 4 4-4" />
  </svg>
			</button>
			<div id="dropdown"
				class="z-10 hidden bg-white divide-y divide-gray-100 rounded-lg shadow w-44">
				<ul class="py-2 text-sm text-gray-700"
					aria-labelledby="dropdownDefaultButton">
					<li><a href="<%=request.getContextPath()%>/admin/ViewBooks"
						class="block px-4 py-2 hover:bg-pink-100">Nothing</a></li>
					<li><a
						href="<%=request.getContextPath()%>/admin/ViewBestSellingBooks"
						class="block px-4 py-2 hover:bg-pink-100">Best Selling Books</a></li>
					<li><a
						href="<%=request.getContextPath()%>/admin/ViewLeastSellingBooks"
						class="block px-4 py-2 hover:bg-pink-100">Least Selling Books</a>
					</li>
					<li><a
						href="<%=request.getContextPath()%>/admin/ViewBooksWithLowStock"
						class="block px-4 py-2 hover:bg-pink-100">Books Low on Stock</a></li>
				</ul>
			</div>

			<div class="mt-3">
				<button onclick="printReport()"
					class="text-amber-800 bg-pink-100 hover:bg-pink-200 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center">
					<i class="fas fa-print mr-2"></i> Print Report
				</button>
			</div>

		</div>

		<div class="flex flex-col grow">
			<%
			if (books.size() > 0) {
				for (Book book : booksPerPage) {
			%>
			<div class="flex py-3 my-5 mx-10 rounded-lg shadow-lg bg-gray-50">
				<a class="hover:cursor-pointer hover:text-amber-900"
					href="<%=request.getContextPath()%>/admin/BookDetails?bookID=<%=book.getBookID()%>">
					<%
					if ((book.getImg()) == null) {
					%> <img alt=""
					src="<%=request.getContextPath()%>/admin/img/No_Image_Available.jpg"
					class="viewImg rounded-lg mx-10 object-contain"> <%
 } else {
 %> <img alt="" src="<%=book.getImg()%>"
					class="viewImg rounded-lg mx-10 object-contain"> <%
 }
 %>
				</a>
				<div class="flex flex-col ml-10">
					<a class="hover:cursor-pointer hover:text-amber-900"
						href="<%=request.getContextPath()%>/admin/BookDetails?bookID=<%=book.getBookID()%>"><h1
							class="text-3xl font-bold"><%=book.getTitle()%></h1></a>
					<p class="mt-5 text-lg text-[#926b6a]">
						Stock: <span class="text-[#57687c]"><%=book.getInventory()%></span>
					</p>
					<p class="mt-5 text-lg"><%=book.getDescription()%></p>
					<p class="mt-5 text-sm">
						Author:
						<%=book.getAuthor()%></p>
					<%
					if (book.getRating() > 0) {
					%>
					<p class="mt-5 text-sm">
						Rating:
						<%=book.getRating()%></p>
					<%
					} else {
					%>
					<p class="mt-5 text-sm">Rating: no ratings</p>
					<%
					}
					%>
					<p class="mt-5 text-sm">
						Sold:
						<%=book.getSold()%></p>


				</div>
				<div class="flex-grow"></div>
				<div class="flex">
					<a
						href="<%=request.getContextPath()%>/admin/EditBook?bookID=<%=book.getBookID()%>"><i
						class="viewIcons fa-solid fa-pencil fa-lg mx-3 hover:cursor-pointer"></i></a>
					<a class="m-0 p-0 toggleButton"
						data-book-id="<%=book.getBookID()%>"
						data-book-title="<%=book.getTitle()%>"> <i
						class="viewIcons fa-solid fa-trash fa-lg mx-3 hover:cursor-pointer"></i>
					</a>
				</div>
			</div>
			<%
			}
			} else {
			%>
			<div class="flex justify-center items-center mt-5">
				<h1 class="text-xl font-semibold">There is no such book in the
					store!</h1>
			</div>
			<%
			}
			%>

			<!-- pagination -->
			<div class="flex justify-center items-center mb-2">
				<ul class="inline-flex items-center -space-x-px">
					<li><a href="<%=pageURL + (iCurrentPage - 1)%>"
						class="<%=iCurrentPage == 1 ? "paginationDisabled pointer-events-none" : "paginationEnabled"%> block px-3 py-2 ml-0 leading-tight border border-gray-300 rounded-l-lg">&laquo;</a></li>
					<li>
						<%
						for (int i = 1; i <= totalPages; i++) {
						%> <a href="<%=pageURL + i%>"
						class="paginationEnabled px-3 py-2 leading-tight border border-gray-300"><%=i%></a>
						<%
						}
						%>
					</li>
					<li><a href="<%=pageURL + (iCurrentPage + 1)%>"
						class="<%=iCurrentPage == totalPages ? "paginationDisabled pointer-events-none" : "paginationEnabled"%> block px-3 py-2 leading-tight border border-gray-300 rounded-r-lg">&raquo;</a></li>
				</ul>
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
							action="<%=request.getContextPath()%>/admin/DeleteBook">
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
		</div>

		<script>
			const dropdownButton = document
					.getElementById('dropdownDefaultButton');
			const dropdownMenu = document.getElementById('dropdown');

			dropdownButton.addEventListener('click', function() {
				dropdownMenu.classList.toggle('hidden');
			});
		</script>


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

		<div id="printdiv" class="hidden">
			<div class="flex flex-col justify-center items-center">
				<div class="flex justify-between bg-[#b4c7dd] h-20 space-x-2">
					<div class="flex items-center">
						<img src="<%=request.getContextPath()%>/admin/img/ink-bottle.png"
							class="h-8 mr-3" alt="Flowbite Logo"> <span
							class="self-center text-2xl font-semibold whitespace-nowrap tracking-wide text-black">Inkwell
							Report</span>
					</div>
				</div>
				<div class="flex items-center my-3">
					<h1
						class="self-center text-2xl font-semibold whitespace-nowrap tracking-wide text-black">
						Books Low on Stock</h1>
				</div>
				<div class="relative overflow-x-auto">
					<table class="w-full text-sm text-left text-gray-500">
						<thead class="text-xs text-gray-700 uppercase bg-gray-50">
							<tr>
								<th scope="col" class="px-6 py-3 bg-gray-50">Title</th>
								<th scope="col" class="px-6 py-3">ISBN</th>
								<th scope="col" class="px-6 py-3 bg-gray-50">Price</th>
								<th scope="col" class="px-6 py-3">Qty Sold</th>
								<th scope="col" class="px-6 py-3">Ratings</th>
								<th scope="col" class="px-6 py-3">Stock</th>
								<th scope="col" class="px-6 py-3">Author</th>
							</tr>
						</thead>
						<tbody>
							<%
							if (!books.isEmpty()) {
								for (Book book : books) {
							%>
							<tr class="border-b border-gray-200 dark:border-gray-700">
								<th scope="row"
									class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap bg-gray-50">
									<%=book.getTitle()%></th>
								<td class="px-6 py-4"><%=book.getISBN()%></td>
								<td class="px-6 py-4">$<%=book.getPrice()%></td>
								<td class="px-6 py-4"><%=book.getSold()%></td>
								<td class="px-6 py-4"><%=book.getRating()%></td>
								<td class="px-6 py-4"><%=book.getInventory()%></td>
								<td class="px-6 py-4"><%=book.getAuthor()%></td>
							</tr>
							<%
							}
							}
							%>
						</tbody>
					</table>
				</div>

			</div>
		</div>

	</div>

</body>
</html>
