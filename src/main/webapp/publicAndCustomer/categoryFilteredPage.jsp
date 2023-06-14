<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA1
  --%>
  
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="model.Book"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Category Filtered Page</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
	rel="stylesheet">
<%@ include file="../../tailwind-css.jsp"%>
</head>
<body>
	<%
	List<Book> allGenreBook = (List<Book>) request.getAttribute("allGenreBook");
	String genreName = (String) request.getAttribute("genreName");
	String action = request.getParameter("action");
	String searchInput = request.getParameter("searchInput");
	boolean err = false;
	String validatedUserID = (String) request.getAttribute("validatedUserID");
	if (allGenreBook == null || genreName == null) {
		err = true;
	%>
	<!-- Error Loading Page -->
	<div class="fixed inset-0 flex items-center justify-center">
		<div class="bg-yellow-100 p-5 rounded-lg">
			<i class="fas fa-exclamation-triangle text-yellow-700 mr-2"></i> Error Loading Page
		</div>
	</div>
	<%
	}

	if (validatedUserID == null) {
	%>
	<%@ include file="navBar/headerNavPublic.html"%>
	<%
	} else {
	%>
	<%@ include file="navBar/headerNavCustomer.jsp"%>
	<%
	}

	if (!err) {
	List<Book> booksOnCurrentPage = null;
	int booksPerPage = 10;
	int totalBooks = allGenreBook.size();
	int totalPages = (int) Math.ceil((double) totalBooks / booksPerPage);
	int currentPage = 1;
	if (request.getParameter("page") != null) {
		currentPage = Integer.parseInt(request.getParameter("page"));
	}
	int startIndex = (currentPage - 1) * booksPerPage;
	int endIndex = Math.min(startIndex + booksPerPage, totalBooks);
	booksOnCurrentPage = allGenreBook.subList(startIndex, endIndex);
	%>
	<!-- Allow searching books that is under the genre -->
	<div class="mx-20 mb-60">
		<div class="flex items-center justify-between">
			<h1 class="text-4xl font-bold italic my-10"><%=genreName%></h1>
			<form action="/CA1-assignment/CategoryFilteredPage" method="GET">
				<input type="hidden" name="action" value="searchBookByTitle">
				<input type="hidden" name="genreID"
					value="<%=request.getParameter("genreID")%>"> <input
					type="text" name="searchInput"
					placeholder="Search by Book Title in Category"
					class="p-2 rounded-l-lg border border-gray-600 w-72"> <input
					type="hidden" name="genreName" value="<%=genreName%>">
				<%
				if (validatedUserID != null) {
				%>
				<input type="hidden" name="userIDAvailable" value="true" />
				<%
				}
				%>
				<button type="submit"
					class="p-2 bg-gray-400 text-white rounded-r-lg">
					<i class="fas fa-search"></i>
				</button>
			</form>
		</div>

		<%
		if (booksOnCurrentPage.size() > 0) {
		%>
		<!-- Show MAX 10 books per page -->
		<div class="flex flex-wrap justify-center w-full">
			<%
			for (Book book : booksOnCurrentPage) {
				double rating = book.getRating();
				int filledStars = (int) rating;
				boolean hasHalfStar = (rating - filledStars) >= 0.5;
				int emptyStars = 5 - filledStars - (hasHalfStar ? 1 : 0);
				String urlToBookDetails = "/CA1-assignment/BookDetailsPage?bookID=" + book.getBookID();
			%>
			<div
				class="flex items-center justify-between border border-gray-300 rounded-lg my-4 p-5 shadow-lg w-full  transform hover:scale-110"
				onclick="window.location.href = '<%=urlToBookDetails%>'">


				<div class="flex items-center h-40 m-6">
					<%
					if (book.getImg() != null) {
					%>
					<img class="h-full object-contain" src="<%=book.getImg()%>">
					<%
					} else {
					%>
					<i class="fas fa-image fa-3x"></i>
					<%
					}
					%>
				</div>


				<h2 class="text-lg text-black font-bold"><%=book.getTitle()%></h2>
				<div class="flex items-center">
					<%
					for (int i = 0; i < filledStars; i++) {
					%>
					<i class="fas fa-star text-yellow-500"></i>
					<%
					}
					if (hasHalfStar) {
					%>
					<i class="fas fa-star-half-alt text-yellow-500"></i>
					<%
					}
					for (int i = 0; i < emptyStars; i++) {
					%>
					<i class="far fa-star text-yellow-500"></i>
					<%
					}
					%>
					<p class="text-gray-600 ml-2">
						<%=book.getSold()%>
						Sold
					</p>
				</div>
				<p>
					Author:
					<%=book.getAuthor()%></p>

				<%
				if (book.getInventory() <= 10) {
				%>

				<p class="text-red-500">
					<%=book.getInventory()%>
					Left!
				</p>
				<%
				} else {
				%>
				<p>
					Stock:
					<%=book.getInventory()%>
					Left!
				</p>
				<%
				}
				%>


				<p class="text-lg text-red-600 font-bold mr-6">
					$<%=String.format("%.2f", book.getPrice())%>
				</p>
			</div>


			<%
			}
			%>
		</div>
		<!-- Pagination -->
		<div class="flex items-center justify-center mt-10">
			<div class="flex space-x-4">
				<a
					href="<%=currentPage > 1
		? ("/CA1-assignment/CategoryFilteredPage?page=" + (currentPage - 1) + "&genreName=" + genreName + "&genreID="
				+ request.getParameter("genreID") + (validatedUserID != null ? "&userIDAvailable=true" : "")+(action!=null&&action.equals("searchBookByTitle")?("&action=searchBookByTitle&searchInput="+searchInput):""))
		: "#"%>"
					class="bg-gray-200 text-gray-600 px-4 py-2 rounded <%=currentPage > 1 ? "" : "cursor-not-allowed opacity-50"%>">
					<i class="fas fa-chevron-left"></i>
				</a>
				<%
				for (int i = 1; i <= totalPages; i++) {
					String pageLink = "/CA1-assignment/CategoryFilteredPage?page=" + i + "&genreName=" + genreName + "&genreID="
					+ request.getParameter("genreID");
					if (validatedUserID != null) {
						pageLink += "&userIDAvailable=true";
					}if(action!=null&&action.equals("searchBookByTitle")){
						pageLink+=("&action=searchBookByTitle&searchInput="+searchInput);
					}
				%>
				<a href="<%=pageLink%>"
					class="<%=(i == currentPage ? "bg-slate-500 text-white hover:bg-slate-600"
		: "bg-gray-200 text-gray-600 hover:bg-gray-300")%> px-4 py-2 rounded transform hover:scale-110">
					<%=i%>
				</a>
				<%
				}
				%>
				<a
					href="<%=currentPage < totalPages
		? ("/CA1-assignment/CategoryFilteredPage?page=" + (currentPage + 1) + "&genreName=" + genreName + "&genreID="
				+ request.getParameter("genreID") + (validatedUserID != null ? "&userIDAvailable=true" : "")+(action!=null&&action.equals("searchBookByTitle")?("&action=searchBookByTitle&searchInput="+searchInput):""))
		: "#"%>"
					class="bg-gray-200 text-gray-600 px-4 py-2 rounded <%=currentPage < totalPages ? "" : "cursor-not-allowed opacity-50"%>">
					<i class="fas fa-chevron-right"></i>
				</a>
			</div>
		</div>




		<%
		} else {
		%>
		<!-- If no books in genre -->
		<div class="flex items-center justify-center">
			<div class="flex flex-col items-center">
				<h2 class="text-lg mt-20 pt-10">No results</h2>
			</div>
		</div>
		<%
		}
		%>
	</div>
	<%
	}
	%>
</body>
</html>