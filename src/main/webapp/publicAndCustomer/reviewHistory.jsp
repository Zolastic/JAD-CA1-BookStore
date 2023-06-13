<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA1
  --%>
  
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.ReviewHistoryClass"%>
<%@ page import="model.Book"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Review History</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
	rel="stylesheet">
<%@include file="../../tailwind-css.jsp"%>
</head>
<body>
	<%
	List<ReviewHistoryClass> reviewHistory = (List<ReviewHistoryClass>) request.getAttribute("reviewHistory");
	String validatedUserID = (String) request.getAttribute("validatedUserID");
	if (validatedUserID != null && reviewHistory != null) {
	%>
	<%@ include file="navBar/headerNavCustomer.jsp"%>
	<div class="flex items-center justify-between">
		<h1 class="text-3xl font-bold m-5">Review History</h1>
		<span class="cursor-pointer text-gray-500 hover:text-gray-700"
			onclick="goBack()"> <i
			class="fas fa-times fa-2x m-5 pt-2 mr-10"></i>
		</span>
	</div>

	<%
	if (reviewHistory.size() == 0) {
	%>
	<div
		class="fixed inset-0 flex flex-col items-center justify-center mt-10">
		<p>No Review History</p>
		<button
			class="bg-slate-500 text-white px-4 py-2 rounded hover:bg-slate-700 transform hover:scale-110"
			onclick="goBack()">Back</button>
	</div>
	<%
	} else {
	for (ReviewHistoryClass review : reviewHistory) {
	%>
	<div>
		<div
			class="shadow-lg m-5 px-5 py-8 border border-gray-200 rounded-2xl mb-10">
			<div class="px-10">
				<div class="flex items-center space-x-1">
					<%
					double ratingCust = (double) review.getRating();
					int filledStarsCust = (int) ratingCust;
					boolean hasHalfStarCust = (ratingCust - filledStarsCust) >= 0.5;
					int emptyStarsCust = 5 - filledStarsCust - (hasHalfStarCust ? 1 : 0);
					%>
					<%
					for (int i = 0; i < filledStarsCust; i++) {
					%>
					<i class="fas fa-star text-yellow-500"></i>
					<%
					}
					if (hasHalfStarCust) {
					%>
					<i class="fas fa-star-half-alt text-yellow-500"></i>
					<%
					}
					for (int i = 0; i < emptyStarsCust; i++) {
					%>
					<i class="far fa-star text-yellow-500"></i>
					<%
					}
					%>
					<p class="text-sm text-gray-500">
						(<%=String.format("%.1f", review.getRating())%>/5.0)
					</p>
				</div>
				<p class="text-xs text-gray-500"><%=review.getRatingDate()%></p>
				<p class="text-gray-600"><%=review.getReview_text()%></p>
			</div>
			<%
			Book book = review.getBook();
			%>
			<div class="w-full mt-2">
				<div class="p-4 rounded shadow mx-10">
					<div class="flex items-center justify-between">
					<div class="flex items-center">
						<div class="flex-shrink-0 w-16 h-16 mr-4">
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
						<div class="flex-shrink">
							<h2 class="text-lg font-bold"><%=book.getTitle()%></h2>
							<p class="text-gray-600">
								Author:
								<%=book.getAuthor()%></p>
							<p class="text-gray-600">
								Price: $<%=book.getPrice()%></p>
						</div>
						</div>
						<div class="justify-end">
							<p class="bg-rose-900 text-white text-sm p-1 rounded-lg"><%=book.getGenreName()%></p>
						</div>
					</div>

				</div>
			</div>
		</div>
		<%
		}
		}
		} else {
		%>
		<script>
			if (
		<%=validatedUserID%>
			== null) {
				window.location.href = "registrationPage.jsp";
			} else {
				window.location.href = "/CA1-assignment/ProfilePage?userID="
						+
		<%=validatedUserID%>
			;
			}
		</script>
		<%
		}
		%>
		<script>
			function goBack() {
				window.history.back();
			}
		</script>
</body>
</html>