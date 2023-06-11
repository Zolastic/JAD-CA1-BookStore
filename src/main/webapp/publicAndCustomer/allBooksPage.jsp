<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="model.Book"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>All Books Page</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
	rel="stylesheet">
<%@ include file="../../tailwind-css.jsp"%>
</head>
<body>
	<%
	List<Book> allBooks = (List<Book>) request.getAttribute("allBooks");
	String searchExecuted = (String) request.getAttribute("searchExecuted");
	boolean err = false;
	String validatedUserID = (String) request.getAttribute("validatedUserID");
	if (allBooks == null) {
		err = true;
	%>
	<div class="fixed inset-0 flex items-center justify-center">
		<div class="bg-yellow-200 px-4 py-2 rounded-lg">
			<i class="fas fa-exclamation-triangle mr-2"></i> Error Loading Page
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
	<%@ include file="navBar/headerNavCustomer.html"%>
	<%
	}

	if (!err) {
	List<Book> booksOnCurrentPage = null;
	int booksPerPage = 10;
	int totalBooks = allBooks.size();
	int totalPages = (int) Math.ceil((double) totalBooks / booksPerPage);
	int currentPage = 1;
	if (request.getParameter("page") != null) {
		currentPage = Integer.parseInt(request.getParameter("page"));
	}
	int startIndex = (currentPage - 1) * booksPerPage;
	int endIndex = Math.min(startIndex + booksPerPage, totalBooks);
	if (searchExecuted == null) {
		booksOnCurrentPage = allBooks.subList(startIndex, endIndex);
	} else {
		booksOnCurrentPage = allBooks;
	}
	%>
	<div class="mx-20 mb-60">
		<div class="flex items-center justify-center m-5">
			<form action="/CA1-assignment/allBooksPage" method="GET">
				<input type="hidden" name="action" value="searchBookByTitle">
				<input type="text" name="searchInput"
					placeholder="Search by Book Title in Category"
					class="p-2 rounded-l-lg border border-gray-600 w-72">
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
		<div class="flex flex-wrap justify-center w-full">
			<%
			for (Book book : booksOnCurrentPage) {
				double rating = book.getRating();
				int filledStars = (int) rating;
				boolean hasHalfStar = (rating - filledStars) >= 0.5;
				int emptyStars = 5 - filledStars - (hasHalfStar ? 1 : 0);
				String urlToBookDetails;
				if (validatedUserID != null) {
					urlToBookDetails = "/CA1-assignment/bookDetailsPage?bookID=" + book.getBookID() + "&userIDAvailable=true";
				} else {
					urlToBookDetails = "/CA1-assignment/bookDetailsPage?bookID=" + book.getBookID();
				}
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
		<%
		if (searchExecuted == null) {
		%>
		<div class="flex items-center justify-center mt-10">
			<div class="flex space-x-4">
				<a
					href="<%=currentPage > 1
				? ("/CA1-assignment/allBooksPage?page=" + (currentPage - 1)
						+ (validatedUserID != null ? "&userIDAvailable=true" : ""))
				: "#"%>"
					class="bg-gray-200 text-gray-600 px-4 py-2 rounded <%=currentPage > 1 ? "" : "cursor-not-allowed opacity-50"%>">
					<i class="fas fa-chevron-left"></i>
				</a>
				<%
				for (int i = 1; i <= totalPages; i++) {
					String pageLink = "/CA1-assignment/allBooksPage?page=" + i;
					if (validatedUserID != null) {
						pageLink += "&userIDAvailable=true";
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
				? ("/CA1-assignment/allBooksPage?page=" + (currentPage + 1)
						+ (validatedUserID != null ? "&userIDAvailable=true" : ""))
				: "#"%>"
					class="bg-gray-200 text-gray-600 px-4 py-2 rounded <%=currentPage < totalPages ? "" : "cursor-not-allowed opacity-50"%>">
					<i class="fas fa-chevron-right"></i>
				</a>
			</div>
		</div>
		<%
		}
		%>



		<%
		} else {
		%>
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