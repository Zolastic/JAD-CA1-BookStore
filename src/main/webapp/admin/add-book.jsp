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
	List<Object[]> authors = new ArrayList<>();
	List<Object[]> publishers = new ArrayList<>();

	try {
		Connection connection = DBConnection.getConnection();
		Statement genreStatement = connection.createStatement();
		Statement authorStatement = connection.createStatement();
		Statement publisherStatement = connection.createStatement();

		ResultSet genreResultSet = genreStatement
		.executeQuery("SELECT genre_id as genreId, genre_name as genreName FROM genre;");
		ResultSet authorResultSet = authorStatement.executeQuery("SELECT * FROM author;");
		ResultSet publisherResultSet = publisherStatement.executeQuery("SELECT * FROM publisher;");

		while (genreResultSet.next()) {
			int genreId = genreResultSet.getInt("genreId");
			String genreName = genreResultSet.getString("genreName");
			Object[] genre = { genreId, genreName };
			genres.add(genre);
		}

		while (authorResultSet.next()) {
			int authorId = authorResultSet.getInt("authorID");
			String authorName = authorResultSet.getString("authorName");
			Object[] author = { authorId, authorName };
			authors.add(author);
		}

		while (publisherResultSet.next()) {
			int publisherID = publisherResultSet.getInt("publisherID");
			String publisherName = publisherResultSet.getString("publisherName");
			Object[] publisher = { publisherID, publisherName };
			publishers.add(publisher);
		}

		genreResultSet.close();
		genreStatement.close();

		authorResultSet.close();
		authorStatement.close();

		publisherResultSet.close();
		publisherStatement.close();
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
			<select
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-blue-600 peer"
				name="author" required>
				<%
				for (Object[] author : authors) {
					int authorId = (int) author[0];
					String authorName = (String) author[1];
				%>
				<option value="<%=authorId%>"><%=authorName%></option>
				<%
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
				for (Object[] publisher : publishers) {
					int publisherId = (int) publisher[0];
					String publisherName = (String) publisher[1];
				%>
				<option value="<%=publisherId%>"><%=publisherName%></option>
				<%
				}
				%>
			</select> <label for="publisher"
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