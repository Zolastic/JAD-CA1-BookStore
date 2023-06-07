<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="model.Book"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.io.*,java.net.*,java.util.*,java.sql.*"%>
<%@ page import="utils.DBConnection"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Book Details</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
<%@ include file="../../tailwind-css.jsp"%>
</head>
<body>
	<script>
    function decrementQuantity(bookID, currentQuantity) {
        if (currentQuantity > 1) {
            var newQuantity = currentQuantity - 1;
            window.location.href = "?bookID=" + bookID + "&quantity=" + newQuantity;
        }
    }

    function incrementQuantity(bookID, currentQuantity, inventory) {
        if (currentQuantity < inventory) {
            var newQuantity = currentQuantity + 1;
            window.location.href = "?bookID=" + bookID + "&quantity=" + newQuantity;
        }
    }

    function updateQuantity(bookID, newQuantity, inventory) {
        if (newQuantity < 1 || newQuantity > inventory) {
            alert("Invalid input! Please enter a quantity between 1 and " + inventory);
        } else {
            window.location.href = "?bookID=" + bookID + "&quantity=" + newQuantity;
        }
    }
</script>
	<%
	String bookID = request.getParameter("bookID");
	Book bookDetails = null;
	boolean err = false;

	if (bookID == null) {
		err = true;
	%>
	<div class="fixed inset-0 flex items-center justify-center">
		<div class="bg-yellow-200 px-4 py-2 rounded-lg">
			<i class="fas fa-exclamation-triangle mr-2"></i> Error, No books
			found
		</div>
	</div>
	<%
	} else {
	try {
		Connection connection = DBConnection.getConnection();
		String simpleProc = "{call getBookDetails(?)}";
		CallableStatement cs = connection.prepareCall(simpleProc);
		cs.setString(1, bookID);
		cs.execute();
		ResultSet resultSet = cs.getResultSet();

		if (resultSet != null && resultSet.next()) {
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
			bookDetails = new Book(bookID, iSBN, title, author, publisher, publication_date, description, genre_name, img,
			sold, inventory, price, 1, rating);

			String sQty = request.getParameter("quantity");
			if (sQty != null) {
		int iQty = Integer.parseInt(sQty);
		if (iQty >= 1 && iQty <= (bookDetails.getInventory())) {
			bookDetails.setQuantity(iQty);
		}

			}

		} else {
			err = true;
	%>
	<div class="fixed inset-0 flex items-center justify-center">
		<div class="bg-yellow-200 px-4 py-2 rounded-lg">
			<i class="fas fa-exclamation-triangle mr-2"></i> Error, No books
			found
		</div>
	</div>
	<%
	}
	connection.close();
	} catch (Exception e) {
	System.err.println("Error: " + e);
	}
	}

	String userID = (String) session.getAttribute("userID");
	if (userID == null) {
	%>
	<%@ include file="navBar/headerNavPublic.html"%>
	<%
	} else {
	%>
	<%@ include file="navBar/headerNavCustomer.html"%>
	<%
	}

	if (!err) {
	%>
	<div class="container mx-auto mt-8">
		<div class="flex">
			<div class="flex items-center justify-center w-2/3 px-10 py-2">
				<div class="flex items-center justify-center h-80">
					<%
					if (bookDetails.getImg() != null) {
					%>
					<div class="flex items-center h-full">
						<img class="max-w-full max-h-full" src="<%=bookDetails.getImg()%>">
					</div>
					<%
					} else {
					%>
					<i class="fas fa-image fa-10x"></i>
					<%
					}
					%>
				</div>
			</div>
			<div class="w-1/3 py-2 px-2">
				<h1 class="text-7xl italic mb-4"><%=bookDetails.getTitle()%></h1>
				<p class="text-lg mb-4">
					Author:
					<%=bookDetails.getAuthor()%></p>
				<p class="text-lg mb-4">
					Price: $<%=bookDetails.getPrice()%></p>
				<div class="flex items-center mb-4">
					<h2 class="text-1xl mr-3">Quantity:</h2>
					<div
						class="bg-white border border-gray-500 flex items-center justify-center w-10 h-10">
						<a href="#"
							onclick="decrementQuantity('<%=bookID%>', <%=bookDetails.getQuantity()%>)"><i
							class="fas fa-minus"></i></a>
					</div>
					<div
						class="bg-white border border-gray-500 border-x-0 flex items-center w-10 h-10 justify-center">
						<input type="number" name="quantity" class="w-10 text-center"
							value="<%=bookDetails.getQuantity()%>"
							onchange="updateQuantity('<%=bookID%>', this.value, <%=bookDetails.getInventory()%>)">
					</div>
					<div
						class="bg-white border border-gray-500 flex items-center justify-center w-10 h-10">
						<a href="#"
							onclick="incrementQuantity('<%=bookID%>', <%=bookDetails.getQuantity()%>, <%=bookDetails.getInventory()%>)"><i
							class="fas fa-plus"></i></a>
					</div>
				</div>

				<div class="mr-4">
					<button class="bg-slate-500 text-white px-4 py-2 rounded w-full">Add
						to Cart</button>
				</div>

				<h2 class="text-2xl">Description:</h2>
				<p class="mb-4"><%=bookDetails.getDescription()%></p>
			</div>
		</div>
		<div class="flex items-center justify-between mt-10">
			<h1 class="text-4xl text-bold">Reviews</h1>
			<div class="flex items-center">
				<%
				double rating = bookDetails.getRating();
				int filledStars = (int) rating;
				boolean hasHalfStar = (rating - filledStars) >= 0.5;
				int emptyStars = 5 - filledStars - (hasHalfStar ? 1 : 0);
				%>
				<%
				for (int i = 0; i < filledStars; i++) {
				%>
				<i class="fas fa-star fa-2x text-yellow-500"></i>
				<%
				}
				if (hasHalfStar) {
				%>
				<i class="fas fa-star-half-alt fa-2x text-yellow-500"></i>
				<%
				}
				for (int i = 0; i < emptyStars; i++) {
				%>
				<i class="far fa-star fa-2x text-yellow-500"></i>
				<%
				}
				%>
				<p class="text-4xl text-bold ml-5 mr-2"><%=bookDetails.getRating()%></p>
			</div>
		</div>



	</div>
	<%
	}
	%>
</body>
</html>
