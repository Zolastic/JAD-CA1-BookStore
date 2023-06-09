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
	<script>
	
	function selectCartItem(formID) {
		  let scrollPosition = window.scrollY;
		  document.getElementById('scrollPositionForSelect').value = scrollPosition;

		  document.getElementById(formID).submit();
		}
    
    function selectAllCartItem() {
        let scrollPosition = window.scrollY;
        document.getElementById('scrollPositionForSelectAll').value = scrollPosition;
        selectAllCartItemForm.submit();
    }
    window.addEventListener('load', () => {
        const scrollPosition = parseInt(document.getElementById('scrollPosition').value);
        if (!isNaN(scrollPosition)) {
            window.scrollTo(0, scrollPosition);
        }

        let quantityInput = document.getElementById('quantityInput');
        let updatedQuantityInput = document.getElementById('updatedQuantityInput');
        let minusBtn = document.getElementById('minusBtn');
        let plusBtn = document.getElementById('plusBtn');
        let quantityForm = document.getElementById('quantityForm');
        let inventory= document.getElementById('inventoryInput');

        minusBtn.addEventListener('click', () => {
            updateQuantity(-1);
        });

        plusBtn.addEventListener('click', () => {
            updateQuantity(1);
        });

        quantityInput.addEventListener('change', () => {
            updateQuantity(0);
        });

        deleteBtn.addEventListener('click', () => {
            deleteCartItem();
        });
        

        function deleteCartItem() {
            let scrollPosition = window.scrollY;
            document.getElementById('scrollPositionForDelete').value = scrollPosition;
            deleteCartItemForm.submit();
        }
        
        function submitCheckoutForm() {
            let selectedBookIDs = [<%for (Book item : cartItems) {
	if (item.getSelected() == 1) {
		out.println("\"" + item.getBookID() + "\",");
	}
}%>];
            let selectedBookIDsString = selectedBookIDs.join(',');
            document.getElementById('selectedBookID').value = selectedBookIDsString;
            document.getElementById('checkoutForm').submit();
        }

        function updateQuantity(change) {
            const currentQuantity = parseInt(quantityInput.value);
            const newQuantity = currentQuantity + change;
            if (change == 0) {
                quantityInput.value = newQuantity;
            }
            if (newQuantity > 0 && newQuantity <= inventory) {
                let scrollPosition = window.scrollY;
                document.getElementById('scrollPositionForQty').value = scrollPosition;
                updatedQuantityInput.value = newQuantity;
                quantityForm.submit();
            }
        }
    });
    </script>
	<%@ include file="navBar/headerNavCustomer.html"%>
	<div class="mx-10 mb-60">
		<h1 class="text-3xl font-bold italic my-6">
			<i class="fas fa-shopping-cart text-blue-950 mr-2"></i>Your Cart
		</h1>
		<div class="flex flex-col mb-5">
			<%
			if (cartItems == null || cartItems.isEmpty()) {
			%>
			<div class="flex items-center justify-center">
				<p>Cart is empty</p>
			</div>
			<%
			} else {
			for (Book item : cartItems) {
				String encodedUserID = URLEncoder.encode(validatedUserID, StandardCharsets.UTF_8.toString());
				String urlToBookDetails = "/CA1-assignment/bookDetailsPage?bookID=" + item.getBookID() + "&userID=" + encodedUserID;
			%>
			<div
				class="flex items-center border border-gray-300 rounded-lg my-2 p-5">
				<form id="selectCartItemForm_<%=item.getBookID()%>"
					action="/CA1-assignment/cartPage" method="post">
					<div onclick="selectCartItem('selectCartItemForm_<%=item.getBookID()%>')">
						<input type="checkbox"
							class="selectCheckbox mr-2 w-4 h-4"
							<%=(item.getSelected() == 1) ? "checked" : ""%> />
					</div>
					<input type="hidden" class="bookIDInput" name="bookID"
						value="<%=item.getBookID()%>" /> <input type="hidden"
						name="scrollPositionForSelect" id="scrollPositionForSelect"
						value="" /> <input type="hidden" class="actionInput"
						name="selectCartItem" value="" /> <input type="hidden"
						class="validatedUserIDInput" name="validatedUserID"
						value="<%=validatedUserID%>" />
				</form>


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
					<form id="quantityForm" action="/CA1-assignment/cartPage"
						method="post">
						<button id="minusBtn"
							class="text-gray-500 hover:text-black focus:outline-none">
							<i class="fas fa-minus transform hover:scale-110"></i>
						</button>
						<input id="quantityInput" class="w-12 text-center" type="number"
							name="quantity" value="<%=item.getQuantity()%>">
						<button id="plusBtn"
							class="text-gray-500 hover:text-black focus:outline-none">
							<i class="fas fa-plus transform hover:scale-110"></i>
						</button>
						<input type="hidden" name="scrollPositionForQty"
							id="scrollPositionForQty" value=""> <input type="hidden"
							id="updatedQuantityInput" name="updatedQuantity" value="">
						<input type="hidden" name="bookID" value="<%=item.getBookID()%>">
						<input type="hidden" name="validatedUserID"
							value="<%=validatedUserID%>"> <input type="hidden"
							name="action" value="updateCartItem"> <input
							type="hidden" id="inventoryInput"
							value="<%=item.getInventory()%>">
					</form>
					<form id="deleteCartItemForm" action="/CA1-assignment/cartPage"
						method="post">
						<button id="deleteBtn"
							class="hover:text-red-600 text-red-800 focus:outline-none mx-3">
							<i class="fas fa-trash-alt transform hover:scale-110"></i>
						</button>
						<input type="hidden" name="scrollPositionForDelete"
							id="scrollPositionForDelete" value=""> <input
							type="hidden" name="bookID" value="<%=item.getBookID()%>">
						<input type="hidden" name="validatedUserID"
							value="<%=validatedUserID%>"> <input type="hidden"
							name="action" value="deleteCartItem">
					</form>
				</div>
			</div>
			<%
			subtotal += (item.getSelected() == 1) ? (item.getPrice() * item.getQuantity()) : 0;
			}
			%>
			<div
				class="fixed bottom-0 left-0 w-full h-50 p-4 px-10 bg-white shadow">
				<div class="flex justify-between items-center">
					<form id="selectAllCartItemForm" action="/CA1-assignment/cartPage" method="post">
						<input type="checkbox" id="select-all"
							onchange="selectAllCartItem()" class="mr-2 w-4 h-4"> <label
							for="select-all">Select All</label> <input type="hidden"
							name="scrollPositionForSelectAll" id="scrollPositionForSelectAll"
							value=""> <input type="hidden" class="actionInput"
							name="selectAllCartItems" value=""> <input type="hidden"
							class="validatedUserIDInput" name="validatedUserID"
							value="<%=validatedUserID%>">
					</form>
					<div>

						<p class="text-lg font-bold my-2">
							Subtotal: $<%=String.format("%.2f", subtotal)%>
						</p>
						<div class="flex justify-end my-2">
							<form id="checkoutForm" action="/CA1-assignment/cartPage"
								method="post">
								<button
									class="px-4 p-2 bg-slate-600 hover:bg-slate-700 hover:scale-110 text-white rounded hover:bg-blue-600"
									onclick="submitCheckoutForm()">Checkout</button>
								<input type="hidden" id="selectedBookID" name="selectedBookID"
									value=""> <input type="hidden" name="checkout" value="">
								<input type="hidden" class="validatedUserIDInput"
									name="validatedUserID" value="<%=validatedUserID%>">
							</form>
						</div>
					</div>
				</div>
			</div>
			<%
			}
			%>
		</div>
	</div>
</body>
</html>
