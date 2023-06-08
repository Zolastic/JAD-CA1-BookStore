<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: Edit Book</title>
<%@include file="../tailwind-css.jsp"%>

</head>
<body class="my-8 mx-48">
	<%@ page import="java.util.*, model.*"%>
	<%
	Book book = (Book) request.getAttribute("book");
	%>

	<form class="mt-3"
		action="<%=request.getContextPath()%>/EditBook" method="post">
		
		<input type="text" name="bookID" id="bookID"
				value="<%=book.getBookID()%>"
				class="hidden"
				placeholder=" " required/>
		<!-- title -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="text" name="title" id="title"
				value="<%=book.getTitle()%>"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				placeholder=" " required /> <label for="title"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Title</label>
		</div>

		<!-- ISBN -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="text" name="isbn" id="isbn" value="<%=book.getISBN()%>"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				placeholder=" " required /> <label for="isbn"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">ISBN</label>
		</div>

		<!-- description -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="text" name="description" id="description"
				value="<%=book.getDescription()%>"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				placeholder=" " required /> <label for="description"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Description</label>
		</div>

		<!-- price -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="number" name="price" id="price" step="0.01"
				value="<%=book.getPrice()%>"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				placeholder=" " required /> <label for="price"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Price</label>
		</div>

		<!-- author -->
		<div class="relative z-0 w-full mb-8 group">
			<select
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				name="author" required>
				<%
				List<Author> authors = (List<Author>) request.getAttribute("authors");
				for (Author author : authors) {
					if (author.getName().equals(book.getAuthor())) {
				%>
				<option value="<%=author.getId()%>" selected><%=author.getName()%></option>
				<%
				} else {
				%>
				<option value="<%=author.getId()%>"><%=author.getName()%></option>
				<%
				}
				}
				%>
			</select> <label for="author"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Author</label>
		</div>


		<!-- publisher -->
		<div class="relative z-0 w-full mb-8 group">
			<select
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				name="publisher" required>
				<%
				List<Publisher> publishers = (List<Publisher>) request.getAttribute("publishers");
				for (Publisher publisher : publishers) {
					if (publisher.getName().equals(book.getPublisher())) {
				%>
				<option value="<%=publisher.getId()%>" selected><%=publisher.getName()%></option>
				<%
				} else {
				%>
				<option value="<%=publisher.getId()%>"><%=publisher.getName()%></option>
				<%
				}
				}
				%>
			</select> <label for="publisher"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Publisher</label>
		</div>

		<!-- publication date -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="date" name="date" id="date"
				value="<%=book.getPublication_date()%>"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				placeholder=" " required /> <label for="date"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Publication
				Date</label>
		</div>

		<!-- number of books sold -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="number" name="sold" id="sold"
				value="<%=book.getSold()%>"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				placeholder=" " required /> <label for="sold"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">
				Sold</label>
		</div>

		<!-- quantity and genre/ category -->
		<div class="grid md:grid-cols-2 md:gap-6">
			<div class="relative z-0 w-full mb-8 group">
				<input type="number" name="quantity" id="quantity"
					value="<%=book.getInventory()%>"
					class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
					placeholder=" " required /> <label for="quantity"
					class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Quantity</label>
			</div>
			<div class="relative z-0 w-full mb-8 group">
				<select
					class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
					name="genre" required>
					<%
					List<Genre> genres = (List<Genre>) request.getAttribute("genres");
					for (Genre genre : genres) {
						if (genre.getName().equals(book.getGenreName())) {
					%>
					<option value="<%=genre.getId()%>" selected><%=genre.getName()%></option>
					<%
					} else {
					%>
					<option value="<%=genre.getId()%>"><%=genre.getName()%></option>
					<%
					}
					}
					%>
				</select> <label for="genre"
					class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Genre</label>
			</div>
		</div>
		<button type="submit"
			class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center">Save
			Changes!</button>
	</form>
</body>
</html>