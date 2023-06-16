<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA1
  --%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="model.Book"%>
<%@ page import="java.util.*"%>
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
	<%@ include file="customerModal.jsp"%>
	<%
	String validatedUserID = (String) request.getAttribute("validatedUserID");
	List<Book> cartItems = (List<Book>) request.getAttribute("cartItems");
	Double subtotal = 0.0;
	String scrollPosition = (String) request.getAttribute("scrollPosition");
	boolean allSelected = true;

	String cartID = (String) request.getAttribute("cartID");
	String err = request.getParameter("error");
	if (err != null) {
		if (err.equals("true")) {
			out.print("<script>showModal('Internal Server Error')</script>");
		}
	}
	if (cartID != null && validatedUserID != null) {
	%>
	<script>
	
	function selectCartItem(formID, scrollURL) {
		let scrollPosition = window.scrollY;
        document.getElementById(scrollURL).value = scrollPosition;
		  document.getElementById(formID).submit();
	}
    
    function selectAllCartItem() {
    	var checkbox = document.getElementById("select-all");
    	 var newSelectionInput = document.getElementById("newSelection");
    	if (checkbox.checked) {
    	    newSelectionInput.value = "1";
    	  } else {
    	    newSelectionInput.value = "0";
    	  }
        let scrollPosition = window.scrollY;
        document.getElementById('scrollPositionForSelectAll').value = scrollPosition;
        document.getElementById('selectAllCartItemForm').submit();
    }
    
    function deleteCartItem(formID, scrollURL) {
        let scrollPosition = window.scrollY;
        document.getElementById(scrollURL).value = scrollPosition;
        formID.submit();
    }
    
    function submitCheckoutForm() {
        let selectedCartItems = [];
        <%for (Book item : cartItems) {
	if (item.getSelected() == 1) {%>
                selectedCartItems.push({
                    bookID: "<%=item.getBookID()%>",
                    quantity: <%=item.getQuantity()%>
                });
            <%}
}%>
        
        if (selectedCartItems.length > 0) {
            let selectedCartItemsString = JSON.stringify(selectedCartItems);
            document.getElementById('selectedCartItems').value = selectedCartItemsString;
            document.getElementById('checkoutForm').submit();
        } else {
        	showModal("Please select more than one book to proceed with checkout.");
        }
        
        event.preventDefault();
    }



    
    function updateQuantity(change, bookID) {
        const currentOrInputQuantity = document.getElementById("quantityInput_" + bookID);
        const updatedQuantity = document.getElementById("updatedQuantity_" + bookID);
        const newQuantity = parseInt(currentOrInputQuantity.value) + change;
        const inventory = parseInt(document.getElementById("inventory_" + bookID).value);

        if (newQuantity > 0 && newQuantity <= inventory) {
            updatedQuantity.value = newQuantity;
            let scrollPosition = window.scrollY;
            document.getElementById('scrollPositionForQty_' + bookID).value = scrollPosition;
            document.getElementById('quantityForm_' + bookID).submit();
        } else {
        	if(newQuantity <= 0){
        		updatedQuantity.value = 1;
                let scrollPosition = window.scrollY;
                document.getElementById('scrollPositionForQty_' + bookID).value = scrollPosition;
                document.getElementById('quantityForm_' + bookID).submit();
        	}else{
        		updatedQuantity.value = inventory;
                let scrollPosition = window.scrollY;
                document.getElementById('scrollPositionForQty_' + bookID).value = scrollPosition;
                document.getElementById('quantityForm_' + bookID).submit();
        	}
        }

        event.preventDefault();
    }

    
    
    window.addEventListener('load', () => {
        if (!isNaN(<%=scrollPosition%>)) {
            window.scrollTo(0, <%=scrollPosition%>);
        }
    });
    
    </script>

	<!-- Show Cart Details only if user is logged in -->
	<%@ include file="navBar/headerNavCustomer.jsp"%>
	<div class="mx-10 mb-60">
		<h1 class="text-3xl font-bold italic my-6">
			<i class="fas fa-shopping-cart text-blue-950 mr-2"></i>Your Cart
		</h1>
		<div class="flex flex-col mb-5">
			<!-- if cart is empty -->
			<%
			if (cartItems == null || cartItems.isEmpty()) {
			%>
			<div class="fixed inset-0 flex items-center justify-center mt-10">
				<p>Cart is empty</p>
			</div>
			<%
			} else {
			for (Book item : cartItems) {
				if (item.getSelected() != 1) {
					allSelected = false;
				}
				String urlToBookDetails = "/CA1-assignment/BookDetailsPage?bookID=" + item.getBookID();
			%>
			<div
				class="flex items-center border border-gray-300 rounded-lg my-2 p-5 shadow-lg">

				<form id="selectCartItemForm_<%=item.getBookID()%>"
					action="/CA1-assignment/CartPage" method="post">
					<div
						onclick="selectCartItem('selectCartItemForm_<%=item.getBookID()%>', 'scrollPositionForSelect_<%=item.getBookID()%>')">
						<input type="checkbox" class="selectCheckbox mr-2 w-4 h-4"
							<%=(item.getSelected() == 1) ? "checked" : ""%> />
					</div>
					<input type="hidden" class="bookIDInput" name="bookID"
						value="<%=item.getBookID()%>" /> <input type="hidden"
						name="action" value="selectCartItem" /> <input type="hidden"
						name="cartID" value="<%=cartID%>" /> <input type="hidden"
						name="newSelection" value="<%=(item.getSelected() == 1) ? 0 : 1%>">
					<input type="hidden" name="scrollPositionForSelect"
						id="scrollPositionForSelect_<%=item.getBookID()%>" />
				</form>

				<a href="<%=urlToBookDetails%>">
					<div class="flex items-center h-32 m-6">
						<%
						if (item.getImg() != null) {
						%>
						<img class="h-32 w-32 object-contain"
							src="data:image/png;base64, <%=item.getImg()%>">
						<%
						} else {
						%>
						<i class="fas fa-image fa-3x"></i>
						<%
						}
						%>
					</div>
				</a>

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
					<!-- Form action to update cart items quantity -->
					<form id="quantityForm_<%=item.getBookID()%>"
						action="/CA1-assignment/CartPage" method="post">
						<button id="minusBtn"
							class="text-gray-500 hover:text-black focus:outline-none"
							onclick="updateQuantity(-1, <%=item.getBookID()%>)">
							<i class="fas fa-minus transform hover:scale-110"></i>
						</button>
						<input id="quantityInput_<%=item.getBookID()%>"
							class="w-12 text-center" type="number" name="quantity"
							value="<%=item.getQuantity()%>"
							onchange="updateQuantity(0, <%=item.getBookID()%>)">
						<button id="plusBtn"
							class="text-gray-500 hover:text-black focus:outline-none"
							onclick="updateQuantity(1, <%=item.getBookID()%>)">
							<i class="fas fa-plus transform hover:scale-110"></i>
						</button>
						<input type="hidden" name="scrollPositionForQty"
							id="scrollPositionForQty_<%=item.getBookID()%>" value="">
						<input type="hidden" id="updatedQuantity_<%=item.getBookID()%>"
							name="updatedQuantity" value=""> <input type="hidden"
							id="bookID" name="bookID" value="<%=item.getBookID()%>">
						<input type="hidden" name="cartID" value="<%=cartID%>"> <input
							type="hidden" name="action" value="updateQuantity"> <input
							type="hidden" id="inventory_<%=item.getBookID()%>"
							value="<%=item.getInventory()%>">
					</form>
					<!-- Form action to delete cart item -->
					<form id="deleteCartItemForm_<%=item.getBookID()%>"
						action="/CA1-assignment/CartPage" method="post">
						<button id="deleteBtn"
							class="hover:text-red-600 text-red-800 focus:outline-none mx-3"
							onclick="deleteCartItem('deleteCartItemForm_<%=item.getBookID()%>', 'scrollPositionForDelete_<%=item.getBookID()%>')">
							<i class="fas fa-trash-alt transform hover:scale-110"></i>
						</button>
						<input type="hidden" name="scrollPositionForDelete"
							id="scrollPositionForDelete_<%=item.getBookID()%>" value="">
						<input type="hidden" name="bookID" value="<%=item.getBookID()%>">
						<input type="hidden" name="cartID" value="<%=cartID%>"> <input
							type="hidden" name="action" value="deleteCartItem">
					</form>
				</div>
			</div>
			<!-- Calculate subtotal -->
			<%
			subtotal += (item.getSelected() == 1) ? (item.getPrice() * item.getQuantity()) : 0;
			}
			%>
			<!-- Fixed bottom div for select all and subtotal with their form action -->
			<div
				class="fixed bottom-0 left-0 w-full h-50 p-4 px-10 bg-white border border-t border-gray-200 shadow-lg">
				<div class="flex justify-between items-center">
					<form id="selectAllCartItemForm" action="/CA1-assignment/CartPage"
						method="post">
						<input type="checkbox" id="select-all"
							onchange="selectAllCartItem()" class="mr-2 w-4 h-4"
							<%=allSelected ? "checked" : ""%>> <label
							for="select-all">Select All</label> <input type="hidden"
							name="scrollPositionForSelectAll" id="scrollPositionForSelectAll"
							value=""> <input type="hidden" name="action"
							value="selectAllCartItems"> <input type="hidden"
							name="cartID" value="<%=cartID%>"> <input type="hidden"
							name="newSelection" id="newSelection" value="">
					</form>
					<div>

						<p class="text-lg font-bold my-2">
							Subtotal: $<%=String.format("%.2f", subtotal)%>
						</p>
						<div class="flex justify-end my-2">
							<form id="checkoutForm" action="/CA1-assignment/CartPage"
								method="post">
								<button
									class="px-4 p-2 bg-slate-600 hover:bg-slate-800 hover:scale-110 text-white rounded hover:bg-blue-600"
									onclick="submitCheckoutForm()">Checkout</button>
								<input type="hidden" id="selectedCartItems"
									name="selectedCartItems" value=""> <input type="hidden"
									name="action" value="checkout">
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
	<%
	} else {
	%>
	<!-- If user is not logged in or have no cart -->
	<script>
	var closeButton = document.getElementById("close");
	showModal("Error loading page");
	closeButton.addEventListener("click", function() {
		window.location.href = "/CA1-assignment/home.jsp";
	});
	</script>
	<%
	}
	%>
</body>
</html>
