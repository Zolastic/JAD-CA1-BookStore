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
	String custID = (String) request.getAttribute("validatedUserID");
	String transactionHistoryItemID = (String) request.getAttribute("transactionHistoryItemID");
	Book bookDetails = (Book) request.getAttribute("bookDetails");
	String scrollPosition = (String) request.getAttribute("scrollPosition");
	if (bookDetails != null && custID != null && scrollPosition != null) {
	%>
	<%@ include file="navBar/headerNavCustomer.jsp"%>
	<div class="container p-2 px-10 py-8">
		<h1 class="text-2xl font-bold mb-4">Book Details</h1>
		<div class="border border-gray-300 mb-2"></div>
		<div class="flex items-center mb-4">
			<div class="flex-shrink-0 items-center w-16 h-16 mr-4">
				<%
				if (bookDetails.getImg() != null) {
				%>
				<img class="h-full object-contain" src="<%=bookDetails.getImg()%>">
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

		<h1 class="text-2xl font-bold mb-4">Rate the Book</h1>
		<div class="border border-gray-300 mb-5"></div>
		<form action="review" method="post">
			<input type="hidden" name="action" value="submitReview"> <input
				type="hidden" name="bookID" value="<%=bookDetails.getBookID()%>">
			<input type="hidden" name="custID" value="<%=custID%>"> <input
				type="hidden" name="transactionHistoryItemID"
				value="<%=transactionHistoryItemID%>"> <input type="hidden"
				name="scrollPosition" value="<%=scrollPosition%>">

			<div class="flex items-center">
				<i class="far fa-sad-tear text-gray-600"></i>
				<div class="w-72 mx-2">
					<input type="range" name="rating" min="0.0" max="5.0" step="0.1"
						value="0.0"
						class="w-full bg-gray-300 rounded-lg appearance-none h-4"
						onchange="updateRangeValue(this)">
				</div>
				<i class="far fa-grin-hearts text-gray-600"></i>
				<p id="rangeValue" class="ml-2">0.0</p>
			</div>



			<div class="mb-4 mt-5">
				<label for="review_text" class="block mb-2">Review Description:</label>
				<textarea name="review_text" id="review_text" rows="4"
					maxlength="500" required class="w-full bg-gray-100 rounded-lg"></textarea>
					<div class="flex justify-end">
				<p class="word-count">
					<span id="wordCount">0</span>/500 words
				</p>
				</div>
			</div>
<div class="flex justify-center">
			<button type="submit"
				class="bg-slate-500 hover:bg-slate-600 text-white font-bold py-2 px-4 rounded">Submit
				Review</button></div>
		</form>
	</div>
	<script>
		function updateRangeValue(rangeInput) {
			document.getElementById("rangeValue").textContent = rangeInput.value;
		}
		reviewTextarea.addEventListener('input', function() {
			const wordCount = reviewTextarea.value.split(/\s+/).length;
			wordCountElement.textContent = wordCount;
		});
	</script>
	<%
	} else {
	%>
	<script>
		if (
	<%=custID%>
		== null) {
			window.location.href = "http://localhost:8080/CA1-assignment/publicAndCustomer/registrationPage.jsp";
		} else {
			window.location.href = "http://localhost:8080/CA1-assignment/CA1-assignment/transactionHistory?userIDAvailable=true";
		}
	</script>
	<%
	}
	%>

</body>
</html>