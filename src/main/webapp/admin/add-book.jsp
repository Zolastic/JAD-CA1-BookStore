<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: Add Book</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet" href="./css/add-book.css">
</head>
<body class="addBookBody">
	<%@ page import="java.io.*, java.net.*, java.util.*, java.sql.*"%>
	<%@ page import="utils.DBConnection"%>
	<%
	List<Object[]> genres = new ArrayList<>();

	try {
		Connection connection = DBConnection.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT genre_id as genreId, genre_name as genreName FROM genre;");

		while (resultSet.next()) {
			int genreId = resultSet.getInt("genreId");
			String genreName = resultSet.getString("genreName");
			Object[] genre = { genreId, genreName };
			genres.add(genre);
		}

		resultSet.close();
		statement.close();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	%>
	<!-- Add Book Form  -->
	<form class="mt-3" action="/CA1-assignment/BooksServlet" method="post">
		<!-- title -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="text" name="title" id="title"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				placeholder=" " required /> <label for="title"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Title</label>
		</div>

		<!-- ISBN -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="text" name="isbn" id="isbn"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				placeholder=" " required /> <label for="isbn"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">ISBN</label>
		</div>

		<!-- description -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="text" name="description" id="description"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				placeholder=" " required /> <label for="description"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Description</label>
		</div>

		<!-- price -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="number" name="price" id="price"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				placeholder=" " required /> <label for="price"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Price</label>
		</div>

		<!-- author -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="text" name="author" id="author"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				placeholder=" " required /> <label for="author"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Author</label>
		</div>

		<!-- publisher -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="text" name="publisher" id="publisher"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				placeholder=" " required /> <label for="publisher"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Publisher</label>
		</div>

		<!-- publication date -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="date" name="date" id="date"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				placeholder=" " required /> <label for="date"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Publication
				Date</label>
		</div>

		<!-- quantity and genre/ category -->
		<div class="grid md:grid-cols-2 md:gap-6">
			<div class="relative z-0 w-full mb-8 group">
				<input type="number" name="quantity" id="quantity"
					class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
					placeholder=" " required /> <label for="quantity"
					class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Quantity</label>
			</div>
			<div class="relative z-0 w-full mb-8 group">
				<select
					class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
					name="genre" required>
					<%
					for (Object[] genre : genres) {
						int genreId = (int) genre[0];
						String genreName = (String) genre[1];
					%>
					<option value="<%=genreId%>"><%=genreName%></option>
					<%
					}
					%>
				</select> <label for="genre"
					class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-blue-600 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Genre</label>
			</div>
		</div>
		<button type="submit"
			class="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center">Add
			Book!</button>
	</form>

	<%
	String errCode = request.getParameter("errCode");
	
	if (errCode != null) {
		out.print("error adding book!");
	}
	%>
</body>
</html>