<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA2
  --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="model.Book"%>
<%@ page import="java.text.DecimalFormat"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Book Details</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css">
<%@ include file="../../tailwind-css.jsp"%>
</head>
<body>
	<%@ include file="customerModal.jsp"%>
	<script>
    function decrementQuantity(bookID, currentQuantity) {
        if (currentQuantity > 1) {
            var newQuantity = currentQuantity - 1;
            window.location.href = "?bookID=" + bookID + "&quantity=" + newQuantity;
        }
    }

    function incrementQuantity(bookID, currentQuantity, inventory) {
        if (currentQuantity < inventory) {
            var newQuantity = currentQuantity + 1;
            window.location.href = "?bookID=" + bookID + "&quantity=" + newQuantity;
        }
    }

    function updateQuantity(bookID, newQuantity, inventory) {
    	  if (newQuantity < 1 ) {
    	    showModal("Invalid input!");
    	  } else if (newQuantity > inventory) {
    	    showModal("Invalid input! There is only " + inventory + " left in stock!");
    	  } else {
    	    window.location.href = "?bookID=" + bookID + "&quantity=" + newQuantity;
    	    return;
    	  }

    	  var closeButton = document.getElementById("close");
    	  closeButton.addEventListener("click", function() {
    	    if (newQuantity < 1 ) {
    	      window.location.href = "?bookID=" + bookID + "&quantity=" + 1;
    	    } else if (newQuantity > inventory) {
    	      window.location.href = "?bookID=" + bookID + "&quantity=" + inventory;
    	    }
    	  });
    	}
</script>
	<%
	Book bookDetails = null;
	String bookID = null;
	boolean err = false;
	List<Map<String, Object>> reviews = null;
	String validatedUserID = (String) request.getAttribute("validatedUserID");
	String addToCartAction = request.getParameter("addToCart");
	try{
		bookDetails=(Book) request.getAttribute("bookDetails");
		reviews=(List<Map<String, Object>>) request.getAttribute("reviews");
	}catch (ClassCastException e) {
		err = true;
	}
	
	if (addToCartAction != null) {
		if (addToCartAction.equals("success")) {
	%>
	<script>
	showModal("Item added to cart successfully");
	    </script>
	<%
	} else {
	%>
	<script>
	showModal("Error adding to cart");
	    </script>
	<%
	}
	}

	if (bookDetails == null || err) {
	err = true;
	%>
	<!-- Show Error -->
	<div class="fixed inset-0 flex items-center justify-center">
		<div class="bg-yellow-100 p-5 rounded-lg">
			<i class="fas fa-exclamation-triangle text-yellow-700 mr-2"></i>
			Error, No books found
		</div>
	</div>
	<%
	} else {
	bookID = bookDetails.getBookID();
	String sQty = request.getParameter("quantity");
	if (sQty != null) {
		int iQty = Integer.parseInt(sQty);
		if (iQty >= 1 && iQty <= (bookDetails.getInventory())) {
			bookDetails.setQuantity(iQty);
		}

	}

	}

	if (validatedUserID == null) {
	%>
	<%@ include file="navBar/headerNavPublic.jsp"%>
	<%
	} else {
	%>
	<%@ include file="navBar/headerNavCustomer.jsp"%>
	<%
	}

	if (!err) {
	double rating = bookDetails.getRating();
	int filledStars = (int) rating;
	boolean hasHalfStar = (rating - filledStars) >= 0.5;
	int emptyStars = 5 - filledStars - (hasHalfStar ? 1 : 0);
	%>
	<!-- Show Book Details -->
	<div class="container mx-auto mt-8">
		<div class="flex">
			<div class="flex items-center justify-center w-2/3 px-10 py-2">
				<div class="flex items-center justify-center h-80">
					<%
					if (bookDetails.getImg() != null) {
					%>
					<div class="flex items-center h-64 w-64">
						<img class="object-contain" src="<%=bookDetails.getImg()%>">
					</div>
					<%
					} else {
					%>
					<i class="fas fa-image fa-10x"></i>
					<%
					}
					%>
				</div>
			</div>
			<div class="w-1/3 py-2 px-2">
				<h1 class="text-7xl italic mb-4"><%=bookDetails.getTitle()%></h1>
				<div class="flex items-center">
					<p class="text-bold text-lg mb-4 mr-2 mt-2 pt-2">
						ISBN:
						<%=bookDetails.getISBN()%>
					</p>
					<p
						class="text-white text-sm bg-rose-900 text-center rounded-lg px-2">
						<%=bookDetails.getGenreName()%>
					</p>
				</div>


				<div class="flex items-center my-2">
					<p class="text-lg text-bold mr-2"><%=bookDetails.getRating()%>
					</p>
					<%
					for (int i = 0; i < filledStars; i++) {
					%>
					<i class="fas fa-star text-yellow-500"></i>
					<%
					}
					if (hasHalfStar) {
					%>
					<i class="fas fa-star-half-alt text-yellow-500"></i>
					<%
					}
					for (int i = 0; i < emptyStars; i++) {
					%>
					<i class="far fa-star text-yellow-500"></i>
					<%
					}
					%>
					<p class="text-lg text-bold mx-2">
						|
						<%=bookDetails.getSold()%>
						Sold
					</p>
				</div>


				<p class="text-lg mb-4">
					Author:
					<%=bookDetails.getAuthor()%></p>

				<p class="text-lg mb-2">
					Published By
					<%=bookDetails.getPublisher()%>, At
					<%=bookDetails.getPublication_date()%></p>
				<p class="text-lg mb-4">
					Price: $<%=bookDetails.getPrice()%></p>

				<%
				if (bookDetails.getInventory() <= 10) {
				%>

				<p class="mb-4 text-red-500">
					<%=bookDetails.getInventory()%>
					Left!
				</p>
				<%
				} else {
				%>
				<p class="mb-1 text-green-600">
					<%=bookDetails.getInventory()%>
					Left!
				</p>
				<%
				}
				%>

				<div class="flex items-center mb-4">
					<h2 class="text-1xl mr-3">Quantity:</h2>
					<div
						class="bg-white border border-gray-500 flex items-center justify-center w-10 h-10">
						<a href="#"
							onclick="decrementQuantity('<%=bookID%>', <%=bookDetails.getQuantity()%>)"><i
							class="fas fa-minus"></i></a>
					</div>
					<div
						class="bg-white border border-gray-500 border-x-0 flex items-center w-10 h-10 justify-center">
						<input type="number" name="quantity" class="w-10 text-center"
							value="<%=bookDetails.getQuantity()%>"
							onchange="updateQuantity('<%=bookID%>', this.value, <%=bookDetails.getInventory()%>)">
					</div>
					<div
						class="bg-white border border-gray-500 flex items-center justify-center w-10 h-10">
						<a href="#"
							onclick="incrementQuantity('<%=bookID%>', <%=bookDetails.getQuantity()%>, <%=bookDetails.getInventory()%>)"><i
							class="fas fa-plus"></i></a>
					</div>
				</div>

				<div class="mr-4">
					<!-- Form action for update quantity -->
					<form action="<%=request.getContextPath()%>/AddToCart" method="post">
						<input type="hidden" name="bookID" value="<%=bookID%>"> <input
							type="hidden" name="quantity"
							value="<%=bookDetails.getQuantity()%>"> <input
							type="hidden" name="validatedUserID" value="<%=validatedUserID%>">
						<%
						if (bookDetails.getInventory() != 0) {
						%>
						<button type="submit"
							class="bg-slate-500 hover:bg-slate-700 transform hover:scale-110 text-white px-4 py-2 rounded w-full">
							<i class="fas fa-shopping-cart text-white-500 mr-2"></i> Add to
							Cart <i class="fas fa-shopping-cart text-white-500 ml-2"></i>
						</button>
						<%
						} else {
						%>
						<button class="bg-gray-500 text-white px-4 py-2 rounded w-full"
							disabled>
							<i class="fas fa-shopping-cart text-white-500 mr-2"></i> Add to
							Cart <i class="fas fa-shopping-cart text-white-500 ml-2"></i>
						</button>

						<%
						}
						%>
					</form>

				</div>

			</div>
		</div>
		<div
			class="flex items-center justify-between mt-10 mx-5 bg-gray-100 p-5">
			<h1 class="text-4xl text-bold ">Description</h1>
		</div>
		<!-- Book Description -->
		<div class="flex items-center mx-5 border border-gray-100 p-5 ">
			<p class="mb-4"><%=bookDetails.getDescription()%></p>
		</div>
		<!-- Show All Reviews of the book -->
		<div
			class="flex items-center justify-between mt-10 mx-5 bg-gray-100 p-5">
			<h1 class="text-4xl text-bold ">Reviews</h1>
			<div class="flex items-center">
				<%
				for (int i = 0; i < filledStars; i++) {
				%>
				<i class="fas fa-star fa-2x text-yellow-500"></i>
				<%
				}
				if (hasHalfStar) {
				%>
				<i class="fas fa-star-half-alt fa-2x text-yellow-500"></i>
				<%
				}
				for (int i = 0; i < emptyStars; i++) {
				%>
				<i class="far fa-star fa-2x text-yellow-500"></i>
				<%
				}
				%>
				<p class="text-4xl text-bold ml-5 mr-2"><%=String.format("%.1f", bookDetails.getRating())%>/5.0
				</p>
				<p class="m1-3 text-gray-500">
					(Total
					<%=reviews.size()%>
					Reviews)
				</p>
			</div>
		</div>
		<div
			class="flex items-center mx-5 border border-gray-100 px-5 pt-5 pb-5 mb-20">
			<%
			if (reviews.size() == 0) {
			%>
			<p>No reviews yet.</p>
			<%
			} else {
			%>
			<div class="mt-4 space-y-4 w-full">
				<%
				for (Map<String, Object> review : reviews) {
					String userImg = (String) review.get("userImg");
				%>
				<div class="flex items-start space-x-4">
					<div class="h-8 w-8">
						<%
						if (userImg != null) {
						%>
						<img src="<%=userImg%>" class="rounded-full object-contain">
						<%
						} else {
						%>
						<i class="fas fa-user-circle text-gray-400 text-3xl"></i>
						<%
						}
						%>
					</div>
					<div>
						<h1 class="font-semibold text-xl"><%=review.get("userName")%></h1>
						<div class="flex items-center space-x-1">
							<%
							double ratingCust = (double) review.get("ratingByEachCust");
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
								(<%=String.format("%.1f", review.get("ratingByEachCust"))%>
								/5.0)
							</p>
						</div>
						<p class="text-xs text-gray-500"><%=review.get("ratingDate")%></p>
						<p class="text-gray-600"><%=review.get("review_text")%></p>

					</div>
				</div>
				<div class="my-2 border border-gray-100 w-full"></div>
				<%
				}
				%>
			</div>
			<%
			}
			%>
		</div>
	</div>
	<%
	}
	%>

</body>
</html>