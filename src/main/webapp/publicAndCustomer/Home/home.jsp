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
	<%@ page import="java.io.*, java.net.*, java.util.*, java.sql.*"%>
	<%@ page import="utils.DBConnection"%>
	<%
	List<Object[]> popularBooks = new ArrayList<>();

	try {
		Connection connection = DBConnection.getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM book ORDER BY sold ASC LIMIT 6;");

		while (resultSet.next()) {
			Object[] book = {};
			popularBooks.add(book);
		}
		resultSet.close();
		statement.close();
	} catch (SQLException e) {
		System.err.println("Error :" + e);
	}
	%>
	<%
	String userID = (String)session.getAttribute("userID");
	if(userID==null){
	%>
	<%@include file="../navBar/headerNavPublic.html"%>
	<% 
	}
	else{
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
					<p class="text-slate-500 text-3xl italic">Searching for a book? Browse & Buy Now!</p>
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
	
	<div class="flex items-center justify-center mt-5">
		<div class="flex items-center mx-10 p-10 px-10 h-500 w-full">
			<h1 class="text-4xl font-semibold italic">POPULAR</h1>
		</div>
	</div>

</body>
</html>