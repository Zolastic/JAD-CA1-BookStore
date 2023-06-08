<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Book"%>
<%@ page import="java.io.*,java.net.*,java.util.*,java.sql.*"%>
<%@ page import="java.nio.charset.StandardCharsets"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Cart</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
	rel="stylesheet">
<%@ include file="../../tailwind-css.jsp"%>

</head>
<body>
	<%
	String validatedUserID = (String) request.getAttribute("validatedUserID");
	List<Book> cartItems = (List<Book>) request.getAttribute("cartItems");
	Double subtotal = 0.0;
	%>
	<%@ include file="navBar/headerNavCustomer.html"%>
	<div class="mx-10 mb-60">

		<h1 class="text-3xl font-bold italic my-6">
			<i class="fas fa-shopping-cart text-blue-950 mr-2"></i>Your Cart
		</h1>

		<div class="flex flex-col mb-5 ">
			<%
			if (cartItems == null || cartItems.isEmpty()) {
			%>
			<div class="flex items-center justify-center">
				<p>Cart is empty</p>
			</div>
			<%
			} else {
			%>
			<%
			for (Book item : cartItems) {

				String encodedUserID = URLEncoder.encode(validatedUserID, StandardCharsets.UTF_8.toString());
				String urlToBookDetails = "/CA1-assignment/bookDetailsPage?bookID=" + item.getBookID() + "&userID=" + encodedUserID;
			%>
			<div
				class="flex items-center border border-gray-300 rounded-lg my-2 p-5">
				<div>
					<input type="checkbox" id="item<%=item.getBookID()%>"
						class="mr-2 w-4 h-4"
						<%=(item.getSelected() == 1) ? "checked" : ""%>>
				</div>
				<div class="flex items-center w-16 h-16 m-4"
					onclick="window.location.href = '<%=urlToBookDetails%>'">
					<%
					if (item.getImg() != null) {
					%>
					<img class="h-full w-full" src="<%=item.getImg()%>">
					<%
					} else {
					%>
					<i class="fas fa-image fa-3x"></i>
					<%
					}
					%>
				</div>
				<div class="flex-1 mr-4">
					<a href="<%=urlToBookDetails%>">
						<h2 class="text-lg text-black font-bold"><%=item.getTitle()%></h2>
					</a>
					<p class="text-gray-500"><%=item.getAuthor()%></p>
				</div>
				<div class="flex items-center">
					<p class="text-lg font-bold mr-6">
						$<%=String.format("%.2f", item.getPrice())%>
					</p>
					<button class="text-gray-500 hover:text-black focus:outline-none">
						<i class="fas fa-minus transform hover:scale-110"></i>
					</button>
					<input type="number"
						class="w-16 text-center border border-gray-300 mx-2"
						value="<%=item.getQuantity()%>">
					<button class="text-gray-500 hover:text-black focus:outline-none">
						<i class="fas fa-plus transform hover:scale-110"></i>
					</button>
					<button
						class="ml-4 text-rose-900 hover:text-red-600 focus:outline-none">
						<i class="fas fa-trash transform hover:scale-110"></i>
					</button>
				</div>
			</div>

			<%
			subtotal += (item.getSelected() == 1) ? (item.getPrice() * item.getQuantity()) : 0;
			%>
			<%
			}
			%>
			<%
			}
			%>
		</div>

	</div>
	<div
		class="fixed bottom-0 left-0 w-full h-50 p-4 px-10 bg-white shadow">
		<div class="flex justify-between items-center">
			<div>
				<input type="checkbox" id="select-all" class="mr-2 w-4 h-4">
				<label for="select-all">Select All</label>
			</div>
			<div>

				<p class="text-lg font-bold my-2">
					Subtotal: $<%=String.format("%.2f", subtotal)%>
				</p>
				<div class="flex justify-end my-2">
					<button
						class="px-4 p-2 bg-slate-600 hover:bg-slate-700 hover:scale-110 text-white rounded hover:bg-blue-600">Checkout</button>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
