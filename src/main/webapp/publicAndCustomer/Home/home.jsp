<%--
  - Author(s): Soh Jian Min
  - Date:27/4/2023
  - Copyright Notice:-
  - @(#)
  - Description: JAD Practical 5
  --%>
<%--
TO DO:
- GET ALL BOOKS DISPLAYED
- CHECK IF USERID VALID with select to db
 --%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="model.Book"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Home</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">

<%@include file="../../tailwind-css.jsp"%>
</head>
<body>
	<%@ page import="java.io.*,java.net.*,java.util.*,java.sql.*"%>
	<%@ page import="utils.DBConnection"%>
	<%
	ArrayList<model.Book> popularBooks = new ArrayList<>();
		try {
			Connection connection = DBConnection.getConnection();
			Statement statement = connection.createStatement();

			ResultSet resultSet = statement.executeQuery(
			"SELECT book.book_id, book.img,book.title,book.price, book.description,book.publication_date,book.ISBN, book.inventory, genre.genre_name, book.sold, CAST(AVG(review.rating) AS DECIMAL(2,1)) AS average_rating, author.authorName,publisher.publisherName FROM book JOIN genre ON genre.genre_id = book.genre_id LEFT JOIN review ON review.bookID = book.book_id JOIN author ON book.authorID = author.authorID JOIN publisher ON book.publisherID = publisher.publisherID GROUP BY book.book_id, book.img, book.title, book.price, genre.genre_name, book.sold, book.inventory, author.authorName, publisher.publisherName ORDER BY book.sold DESC LIMIT 6;");

			while (resultSet.next()) {
		String bookID = resultSet.getString("book_id");
		String iSBN = resultSet.getString("ISBN");
		String title = resultSet.getString("title");
		String author = resultSet.getString("authorName");
		String publisher = resultSet.getString("publisherName");
		String publication_date = resultSet.getString("publication_date");
		String description = resultSet.getString("description");
		String genre_name = resultSet.getString("genre_name");
		String img = resultSet.getString("img");
		int sold = resultSet.getInt("sold");
		int inventory = resultSet.getInt("inventory");
		double price = resultSet.getDouble("price");
		double rating = resultSet.getDouble("average_rating");
		model.Book popularBook = new model.Book(bookID, iSBN, title, author, publisher, publication_date, description, genre_name,
		img, sold, inventory, price, rating);
		popularBooks.add(popularBook);
			}
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			System.err.println("Error :" + e);
		}
	%>
	<%
	String userID = (String) session.getAttribute("userID");
		if (userID == null) {
	%>
	<%@include file="../navBar/headerNavPublic.html"%>
	<%
	} else {
	%>
	<%@include file="../navBar/headerNavCustomer.html"%>
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
					<button
						class="bg-slate-600 text-white hover:bg-slate-700 px-4 py-2 m-3 rounded mt-5">Explore
						All Books</button>
				</div>
			</div>
			<div>
				<i class="fas fa-book-open text-red-300 text-8xl"></i>
			</div>
		</div>
	</div>

	<div class="flex items-center justify-center mt-20">
		<div class="flex items-center mx-10 px-20 h-500 w-full">
			<h1 class="text-4xl font-semibold italic">POPULAR</h1>
		</div>
	</div>

	<div class="flex items-center justify-center">
		<div class="flex flex-wrap mx-10 px-10 w-full">
			<%
			int bookCount = 0;
				for (model.Book book : popularBooks) {
					double rating = book.getRating();
					int filledStars = (int) rating;
					boolean hasHalfStar = (rating - filledStars) >= 0.5;
					int emptyStars = 5 - filledStars - (hasHalfStar ? 1 : 0);
					if (bookCount % 3 == 0) {
			%><div class="flex justify-between w-full mt-4">
				<%
				}
				%>
				<div class="m-4 p-6 bg-white border border-gray-300 rounded-lg w-80">
					<img class="h-48 w-48 mx-auto" src="<%=book.getImg()%>"
						alt="Book Image">
					<p class="text-xl font-semibold text-gray-800 text-center mt-4"><%=book.getTitle()%></p>
					<p class="text-gray-600 text-center mt-2">
						Sold:
						<%=book.getSold()%></p>
					<p class="text-gray-600 text-center">
						Inventory:
						<%=book.getInventory()%></p>
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


</body>
</html>