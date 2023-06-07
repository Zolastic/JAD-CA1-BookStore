<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
	<%@ page import="java.util.*, model.Book" %>\
	<%
		List<Book> books = (List<Book>)request.getAttribute("books");
	%>
	
	<table>
		<% for (Book book : books) {
		%>
		<tr>
			<td><%= book.getIsbn() %></td>
			<td><%= book.getTitle() %></td>
		<tr>
		<%
		}
		%>
		
	</table>
</body>
</html>