<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA1
  --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.stripe.Stripe"%>
<%@ page import="com.stripe.model.PaymentIntent"%>
<%@ page import="com.stripe.param.PaymentIntentCreateParams"%>
<%@ page import="com.stripe.exception.StripeException"%>
<%@ page import="model.Book"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<script src="https://js.stripe.com/v3/"></script>
<title>Checkout Page</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
	rel="stylesheet">
<%@ include file="../../tailwind-css.jsp"%>
</head>
<body>
	<%@ include file="modal.jsp"%>
	<%@ include file="navBar/headerNavCustomer.jsp"%>
	<div class="px-10 pt-10">
		<%
		String validatedUserID = (String) request.getAttribute("validatedUserID");
		List<Book> checkoutItems = (List<Book>) request.getAttribute("checkoutItems");
		double subtotal = 0.0;
		if (checkoutItems != null && validatedUserID != null) {
			for (Book item : checkoutItems) {
				subtotal += item.getPrice();
			}

			subtotal = Math.round(subtotal * 100.0) / 100.0; // Round to 2 decimal places
		%>
		<h1 class="text-3xl font-bold mb-5">Checkout Details</h1>
		<!-- Show all the books user selected to checkout -->
		<div class="grid gap-4">
			<%
			for (Book item : checkoutItems) {
			%>
			<div class="flex items-center justify-between p-4 rounded shadow">
				<div class="flex items-center">
					<div class="flex-shrink-0 w-16 h-16 mr-4">
						<%
						if (item.getImg() != null) {
						%>
						<img class="h-full object-contain" src="data:image/png;base64, <%=item.getImg()%>">
						<%
						} else {
						%>
						<i class="fas fa-image fa-3x"></i>
						<%
						}
						%>
					</div>
					<div class="flex-shrink">
						<h2 class="text-lg font-bold"><%=item.getTitle()%></h2>
						<p class="text-gray-600">
							Author:
							<%=item.getAuthor()%></p>
						<p class="text-gray-600">
							Price: $<%=item.getPrice()%></p>
					</div>
				</div>
				<div>
					<p>
						Quantity:
						<%=item.getQuantity()%></p>
				</div>

			</div>
			<%
			}
			%>
		</div>
		<!-- Form action for checkout -->
		<div class="mt-2">
			<form id="payment-form" action="/CA1-assignment/CheckoutPage"
				method="post">
				<!-- Input for user to key in address -->
				<div class="p-2 rounded shadow my-8">
					<h2 class="text-lg font-bold ">Address Details</h2>
					<div class="border border-b border-gray-300 my-2"></div>
					<div class="my-5">
						<label>Full Address:</label> <input type="text" name="address"
							class="w-full border border-gray-300 rounded px-4 py-2" required>
					</div>
				</div>
				<!-- Card elements -->
				<div class="p-2 pb-10 rounded shadow ">
					<h2 class="text-lg font-bold my-3">Card details</h2>
					<div class="border border-b border-gray-300 mt-2"></div>
					<div class="mt-8" id="card-element"></div>
				</div>
				<!-- Show the subtotal -->
				<input type="hidden" name="subtotal" value="<%=subtotal%>">
				<input type="hidden" name="action" value="payment">
				<div
					class="bg-white flex justify-between rounded shadow px-5 py-5 h-30 mt-10">
					<div>
						<h1 class="py-5 font-bold text-2xl">
							SubTotal:<%=subtotal%></h1>
					</div>
					<div>
						<button type="submit"
							class="px-4 py-2 bg-slate-600 hover:bg-slate-600 transform hover:scale-110 text-white rounded my-5">Pay
							Now</button>
					</div>
				</div>
			</form>
		</div>
	</div>

	<script>
		var stripe = Stripe('pk_test_51NHoftHrRFi94qYrhAbUZpgunskXTCLKbp23xS1ScnQve4rjrofdIKLKzPRckw8wk6WhZNKQbjitJLB3HgAVBPuN00s12twaBL');
		var elements = stripe.elements();
		<!-- Create card element -->
		var cardElement = elements.create('card', {
			hidePostalCode : true
		});

		cardElement.mount('#card-element');

		var form = document.getElementById('payment-form');
		form.addEventListener('submit', function(event) {
			event.preventDefault();
			<!-- Create payment method -->
			stripe.createPaymentMethod({
				type : 'card',
				card : cardElement
			}).then(function(result) {
				if (result.error) {
					console.error(result.error);
				} else {
					<!-- Create hidden input for paymentMethodId -->
					var paymentMethodId = result.paymentMethod.id;
					var paymentMethodInput = document.createElement('input');
					paymentMethodInput.setAttribute('type', 'hidden');
					paymentMethodInput.setAttribute('name', 'paymentMethodId');
					paymentMethodInput.setAttribute('value', paymentMethodId);
					form.appendChild(paymentMethodInput);

					form.submit();
				}
			});
		});
		<!-- Checkout cookies expires when exit page -->
		window
				.addEventListener(
						'beforeunload',
						function() {
							document.cookie = "checkoutItems=; expires=Thu, 13 Nov 2003 00:00:00 UTC; path=/;";
						});
	</script>
	<%
	} else {
	%>
	<script>
	showModal("Error loading page");
	var closeButton = document.getElementById("close");
	closeButton.addEventListener("click", function() {
		if (<%=validatedUserID%> != null) {
			window.location.href = "/CA1-assignment/CartPage?userIDAvailable=true";
		} else {
			window.location.href = "/CA1-assignment/CartPage";
		}
	});
	</script>

	<%
	}
	%>
</body>
</html>