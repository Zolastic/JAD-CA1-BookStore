<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA2
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
	<%@ include file="customerModal.jsp"%>
	<%@ include file="customerModalYesCancel.jsp"%>
	<%
	boolean error = false;
	List<ReviewHistoryClass> reviewHistories = null;
	String validatedUserID = (String) request.getAttribute("validatedUserID");
	String scrollPosition = (String) request.getAttribute("scrollPosition");
	String deleteStatus = request.getParameter("delete");
	if (deleteStatus != null) {
		if (deleteStatus.equals("false")) {
			out.print("<script>showModal('Internal Server Error')</script>");
		}
	}
	try{
		reviewHistories = (List<ReviewHistoryClass>) request.getAttribute("reviewHistories");
	}catch (ClassCastException e) {
		error = true;
	}
	if (validatedUserID != null && reviewHistories != null && !error) {
	%>
	<%@ include file="navBar/headerNavCustomer.jsp"%>
	<div class="flex items-center justify-between">
		<h1 class="text-3xl font-bold m-5">Review History</h1>
		<%
		if (reviewHistories.size() != 0) {
		%>
		<span class="cursor-pointer text-gray-500 hover:text-gray-700"
			onclick="goBack()"> <i
			class="fas fa-times fa-2x m-5 pt-2 mr-10"></i>
		</span>
		<%
		}
		%>
	</div>

	<%
	if (reviewHistories.size() == 0) {
	%>
	<!-- If there is no review history yet -->
	<div
		class="fixed inset-0 flex flex-col items-center justify-center mt-10">
		<p>No Review History</p>
		<button
			class="bg-slate-500 mt-3 text-white px-4 py-2 rounded hover:bg-slate-700 transform hover:scale-110"
			onclick="goBack()">Back</button>
	</div>
	<%
	} else {
	for (ReviewHistoryClass reviewHistory : reviewHistories) {
	%>
	<!-- show all review histories-->
	<div
		class="shadow-lg m-5 px-5 py-8 border border-gray-200 rounded-2xl mb-10">
		<div class="px-10">
			<div class="flex justify-between">
				<div class="flex items-center space-x-1">
					<%
					double ratingCust = (double) reviewHistory.getRating();
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
						(<%=String.format("%.1f", reviewHistory.getRating())%>/5.0)
					</p>
				</div>
				<!-- Form action to delete review -->
				<form id="deleteReview_<%=reviewHistory.getReview_id()%>"
					action="<%=request.getContextPath()%>/DeleteReview" method="post">
					<div id="deleteBtn"
						class="hover:text-red-600 text-red-800 focus:outline-none mx-3"
						onclick="showModalYesCancel('Are you sure to delete review?','deleteReview_<%=reviewHistory.getReview_id()%>', 'scrollPositionForDelete_<%=reviewHistory.getReview_id()%>')">
						<i class="fas fa-trash-alt transform hover:scale-110"></i>
					</div>
					<input type="hidden" name="scrollPositionForDelete"
						id="scrollPositionForDelete_<%=reviewHistory.getReview_id()%>"
						value=""> <input type="hidden" name="review_id"
						value="<%=reviewHistory.getReview_id()%>"> <input
						type="hidden" name="transaction_history_itemID"
						value="<%=reviewHistory.getTransaction_history_itemID()%>">
				</form>
			</div>

			<p class="text-xs text-gray-500"><%=reviewHistory.getRatingDate()%></p>
			<p class="text-gray-600"><%=reviewHistory.getReview_text()%></p>
		</div>
		<%
		Book book = reviewHistory.getBook();
		String urlToBookDetails = request.getContextPath()+"/BookDetailsPage?bookID=" + book.getBookID();
		%>
		<div class="w-full mt-2">
			<div class="p-4 rounded shadow mx-10">
				<div class="flex items-center justify-between">
					<div class="flex items-center">
						<a href="<%=urlToBookDetails%>">
							<div class="flex-shrink-0 w-16 h-16 mr-4">
								<%
								if (book.getImg() != null) {
								%>
								<img class="h-full object-contain"
									src="<%=book.getImg()%>">
								<%
								} else {
								%>
								<i class="fas fa-image fa-3x"></i>
								<%
								}
								%>
							</div>
						</a>
						<div class="flex-shrink">
							<a href="<%=urlToBookDetails%>">
								<h2 class="text-lg font-bold"><%=book.getTitle()%></h2>
							</a>
							<p class="text-gray-600">
								Author:
								<%=book.getAuthor()%></p>
							<p class="text-gray-600">
								Price: $<%=String.format("%.2f", book.getPrice())%></p>
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
				window.location.href = "<%=request.getContextPath()%>/ProfilePage?userID="
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
				window.location.href = "<%=request.getContextPath()%>/ProfilePage?userID="+<%=validatedUserID%>;
			}
		    window.addEventListener('load', () => {
		        if (!isNaN(<%=scrollPosition%>)) {
		            window.scrollTo(0, <%=scrollPosition%>);
		        }
		    });
		</script>
</body>
</html>