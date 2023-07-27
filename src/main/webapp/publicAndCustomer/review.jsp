<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA2
  --%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Book"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Review</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
	rel="stylesheet">
<%@ include file="../../tailwind-css.jsp"%>
</head>
<body>
	<%
	boolean error = false;
	String custID = (String) request.getAttribute("validatedUserID");
	String transactionHistoryItemID = (String) request.getAttribute("transactionHistoryItemID");
	Book bookDetails = null;
	String scrollPosition = (String) request.getAttribute("scrollPosition");
	try{
		bookDetails = (Book) request.getAttribute("bookDetails");
	}catch (ClassCastException e) {
		error = true;
	}
	if (bookDetails != null && custID != null && scrollPosition != null && !error) {
	%>
	<%@ include file="navBar/headerNavCustomer.jsp"%>
	<div
		class="p-10 my-10 mx-64 border border-gray-500 rounded-2xl shadow-lg">
		<div class="flex items-center justify-end">
			<span class="cursor-pointer text-gray-500 hover:text-gray-700"
				onclick="goBack()"> <i class="fas fa-times fa"></i>
			</span>
		</div>
		<h1 class="text-2xl font-bold mb-4">Book Details</h1>
		<div class="border border-gray-300 mb-2"></div>
		<div class="flex items-center mb-4">
			<!-- Show Book Details -->
			<div class="flex-shrink-0 items-center w-16 h-16 mr-4">
				<%
				if (bookDetails.getImg() != null) {
				%>
				<img class="h-full object-contain"
					src="data:image/png;base64, <%=bookDetails.getImg()%>">
				<%
				} else {
				%>
				<i class="fas fa-image fa-3x mt-2"></i>
				<%
				}
				%>
			</div>
			<div>
				<h2 class="text-xl font-bold"><%=bookDetails.getTitle()%></h2>
				<p class="text-gray-600"><%=bookDetails.getAuthor()%></p>
			</div>
		</div>
		<!-- Allow User Rate with slide -->
		<h1 class="text-2xl font-bold mb-4">Rate the Book</h1>
		<div class="border border-gray-300 mb-5"></div>
		<!-- Form for Review -->
		<form action="Review" method="post">
			<input type="hidden" name="action" value="submitReview"> <input
				type="hidden" name="bookID" value="<%=bookDetails.getBookID()%>">
			<input type="hidden" name="custID" value="<%=custID%>"> <input
				type="hidden" name="transactionHistoryItemID"
				value="<%=transactionHistoryItemID%>"> <input type="hidden"
				name="scrollPosition" value="<%=scrollPosition%>">

			<div class="flex items-center">
				<i class="far fa-sad-tear text-yellow-400 fa-2x"></i>
				<div class="w-72 mx-2">
					<input type="range" name="rating" min="0.0" max="5.0" step="0.1"
						value="0.0"
						class="w-full bg-gradient-to-r from-gray-100 to-gray-300 rounded-lg appearance-none h-4"
						onchange="updateRangeValue(this)">

				</div>
				<i class="far fa-grin-hearts text-yellow-400 fa-2x"></i>
				<p id="rangeValue"
					class="ml-2 bg-rose-900 p-1 text-white rounded-lg">0.0</p>
			</div>

			<!-- Review Description Text Area -->
			<div class="mb-4 mt-5">
				<label for="review_text" class="block mb-2">Review
					Description:</label>
				<textarea name="review_text" id="reviewTextarea" rows="4"
					maxlength="500" required class="w-full bg-gray-100 rounded-lg px-2"></textarea>
				<div class="flex justify-end">
					<p class="wordCount">0/500 Characters</p>
				</div>
			</div>

			<!-- Submit review button -->
			<div class="flex justify-center">
				<button type="submit"
					class="bg-slate-500 hover:bg-slate-600 text-white font-bold py-2 px-4 rounded">Submit
					Review</button>
			</div>
		</form>
	</div>
	<script>
		function updateRangeValue(rangeInput) {
			document.getElementById("rangeValue").textContent = rangeInput.value;
		}
		const reviewTextarea = document.getElementById('reviewTextarea');
		const wordCountElement = document.querySelector('.wordCount');

		reviewTextarea.addEventListener('input', function() {
			const characterCount = reviewTextarea.value.length;
			wordCountElement.textContent = characterCount + "/500 Characters";
		});

		function goBack() {
			window.history.back();
		}
	</script>
	<%
	} else {
	%>
	<script>
		if (
	<%=custID%>
		== null) {
			window.location.href = "registrationPage.jsp";
		} else {
			window.location.href = "<%=request.getContextPath()%>/TransactionHistoryPage?userIDAvailable=true";
		}
	</script>
	<%
	}
	%>

</body>
</html>
