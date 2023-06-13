<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA1
  --%>
  
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="model.Genre"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Category Menu</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
	rel="stylesheet">
<%@ include file="../../tailwind-css.jsp"%>
</head>
<body>
	<%
	List<Genre> allGenre = (List<Genre>) request.getAttribute("allGenre");
	boolean err = false;
	String validatedUserID = (String) request.getAttribute("validatedUserID");
	if (allGenre == null) {
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
	<%@ include file="navBar/headerNavCustomer.jsp"%>
	<%
	}

	if (!err) {
	%>
	<div class="mx-10 mb-60">

		<h1 class="text-4xl text-center font-bold italic my-10">Select A
			Category</h1>


		<div class="flex flex-wrap justify-center w-full">
			<%
			int count = 0;
			for (Genre category : allGenre) {
				String categoryName = category.getName();
				String imageSrc = category.getImg();
				String placeholderIcon = "fa-question-circle";
			%>
			<div class="w-1/5 h-40 mb-5">
				<div
					class="flex items-center justify-center h-full border border-slate-700 border-2 shadow-lg rounded-3xl m-4 p-0 transform hover:scale-110">
					<form action="/CA1-assignment/CategoryFilteredPage">
						<button type="submit">
							<div
								class="flex flex-col items-center justify-center bg-white rounded-lg p-4 h-30 w-30">
								<%
								if (imageSrc != null) {
								%>
								<img src="<%=imageSrc%>" alt="<%=categoryName%>"
									class="h-full w-full object-cover rounded-lg" />
								<%
								} else {
								%>
								<i class="fas <%=placeholderIcon%> text-4xl"></i>
								<%
								}
								%>
								<p class="text-center mt-2"><%=categoryName%></p>
							</div>
						</button>
						<input type="hidden" name="genreID" value="<%=category.getId()%>" />
						<input type="hidden" name="genreName" value="<%=category.getName() %>" />
						<%if (validatedUserID!=null){
							%>
						<input type="hidden" name="userIDAvailable" value="true" />
						<% }%>
					</form>
				</div>
			</div>

			<%
			count++;
			if (count % 6 == 0 && count < allGenre.size()) {
			%>
		</div>
		<div class="flex flex-wrap justify-center">
			<%
			}
			}
			%>
		</div>
	</div>
	<%
	}
	%>
</body>
</html>
