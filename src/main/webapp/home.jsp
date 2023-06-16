<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA1
  --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="model.Book"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.io.*,java.net.*,java.util.*,java.sql.*"%>
<%@ page import="utils.DBConnection"%>
<%@page import="publicAndCustomer.Home"%>



<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Home</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
	rel="stylesheet">
<%@ include file="../../tailwind-css.jsp"%>
</head>
<body>
	<%
	Home home = new Home();
	home.setData(request);
	List<Book> popularBooks = (List<Book>) request.getAttribute("popularBooks");
	String validatedUserID = (String) request.getAttribute("validatedUserID");
	%>
	<%
	String urlToAllBooks;
	if (validatedUserID == null) {
		urlToAllBooks = "/CA1-assignment/AllBooksPage";
	%>
	<%@include file="publicAndCustomer/navBar/headerNavPublic.html"%>
	<%
	} else {
	urlToAllBooks = "publicAndCustomer/CA1-assignment/AllBooksPage?userIDAvailable=true";
	%>
	<%@include file="publicAndCustomer/navBar/headerNavCustomer.jsp"%>
	<%
	}
	%>
	<div class="flex items-center justify-center mt-5">
		<div
			class="flex items-center bg-gray-200 mx-10 p-10 px-10 h-500 w-full">
			<div class="flex-grow">
				<div class="m-3">
					<p class="text-slate-500 text-3xl italic">Searching for a book?
						Browse & Buy Now!</p>
					<!-- Button to All Books Page -->
					<button
						class="bg-slate-600 text-white hover:bg-slate-700 px-4 py-2 m-3 rounded mt-5 transform hover:scale-110"
						onclick="window.location.href = '<%=urlToAllBooks%>'">Explore
						All Books</button>
				</div>
			</div>
			<div>
				<i class="fas fa-book-open text-red-300 text-8xl"></i>
			</div>
		</div>
	</div>
	
	<!-- Show popular books -->
	<div class="flex items-center justify-center mt-20">
		<div class="flex items-center mx-10 px-20 h-500 w-full">
			<h1 class="text-4xl font-semibold italic mr-2">POPULAR</h1>
			<i class="fas fa-fire fa-2x text-orange-500"></i>
		</div>
	</div>

	<div class="flex items-center justify-center">
		<div class="flex flex-wrap mx-10 px-10 w-full">
			<%
			int bookCount = 0;
			for (Book book : popularBooks) {
				double rating = book.getRating();
				int filledStars = (int) rating;
				boolean hasHalfStar = (rating - filledStars) >= 0.5;
				int emptyStars = 5 - filledStars - (hasHalfStar ? 1 : 0);
				if (bookCount % 3 == 0) {
			%><div class="flex justify-between w-full mt-4">
				<%
				}
				String urlToBookDetails = "/CA1-assignment/BookDetailsPage?bookID=" + book.getBookID();
				%>
				<div
					class="m-4 p-6 bg-white border border-black rounded-lg w-80 shadow-lg transform hover:scale-110 cursor-pointer"
					onclick="window.location.href = '<%=urlToBookDetails%>'">
					<div class="h-48 w-auto flex items-center justify-center mx-auto">
						<%
						if (book.getImg() != null) {
						%>
						<img class="h-48 object-contain" src="data:image/png;base64, <%=book.getImg()%>">
						<%
						} else {
						%>
						<i class="fas fa-image fa-10x"></i>
						<%
						}
						%>
					</div>


					<p class="text-xl font-semibold text-gray-800 text-center mt-4"><%=book.getTitle()%></p>
					<%
					double price = book.getPrice();
					DecimalFormat df = new DecimalFormat("#.00");
					String formattedPrice = df.format(price);
					%>
					<p class="text-red-600 text-center font-bold mt-2">
						$<%=formattedPrice%></p>



					<div class="flex items-center justify-center mt-2">
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
					<div class="flex items-center justify-center mt-2">
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
						<p class="text-gray-600">
							<%=book.getInventory()%>
							Left!
						</p>
						<%
						}
						%>
					</div>
					<div class="flex items-center justify-center mt-2">
						<p
							class="text-white text-sm bg-rose-900 text-center rounded-lg mt-2 p-1">
							<%=book.getGenreName()%>
						</p>
					</div>


				</div>

				<%
				if ((bookCount + 1) % 3 == 0 || bookCount == popularBooks.size() - 1) {
				%>
			</div>
			<%
			}
			bookCount++;
			}
			%>
		</div>

	</div>
	<!-- Button to navigate to all books -->
	<div class="flex items-center justify-center m-10">
		<button
			class="bg-slate-600 text-white text-md hover:bg-slate-700 px-4 py-2 m-3 rounded mt-5 transform hover:scale-110"
			onclick="window.location.href = '<%=urlToAllBooks%>'">
			EXPLORE ALL</button>
	</div>


</body>
</html>