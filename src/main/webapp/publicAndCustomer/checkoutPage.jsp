<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA2
  --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="com.stripe.Stripe"%>
<%@ page import="com.stripe.model.PaymentIntent"%>
<%@ page import="com.stripe.param.PaymentIntentCreateParams"%>
<%@ page import="com.stripe.exception.StripeException"%>
<%@ page import="model.Book"%>
<%@ page import="model.Address"%>
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
	<%@ include file="customerModal.jsp"%>
	<%@ include file="navBar/headerNavCustomer.jsp"%>
	<div class="px-10 pt-10">
		<%
		boolean error = false;
		String validatedUserID = (String) request.getAttribute("validatedUserID");
		List<Book> checkoutItems = null;
		List<Address> addresses = null;
		try {
			checkoutItems = (List<Book>) request.getAttribute("checkoutItems");
			addresses = (List<Address>) request.getAttribute("addresses");
		} catch (ClassCastException e) {
			error = true;
		}
		double subtotal = 0.0;
		if (checkoutItems != null && validatedUserID != null && checkoutItems.size() != 0 && !error) {
			for (Book item : checkoutItems) {
				subtotal += (item.getPrice() * item.getQuantity());
			}

			subtotal = Math.round(subtotal * 100.0) / 100.0; // Round to 2 decimal places
			double gstPercent=8;
			double gst = Math.round((subtotal / 100) * gstPercent * 100.0) / 100.0;
			double totalAmt = Math.round((subtotal + gst) * 100.0) / 100.0;
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
						<img class="h-full object-contain"
							src="<%=item.getImg()%>">
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
							Price: $<%=String.format("%.2f", item.getPrice())%></p>
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
				<!-- Input for user to select address -->
				<div class="p-2 rounded shadow my-8">
					<div class="w-full flex justify-between">
						<h2 class="text-lg font-bold">Address Details</h2>
						<button type="button"
							class="ml-2 px-4 py-2 bg-gray-300 rounded text-gray-800"
							id="modAddr">Modify Address</button>
					</div>
					<div class="border border-b border-gray-300 my-2"></div>
					<div class="flex my-5">
						<label class="mr-2">Select Address:</label> <select name="addr"
							class="w-full border border-gray-300 rounded px-4 py-2" required>
							<option value="">Choose an address</option>
							<%
							for (Address addr : addresses) {
								String fullAddr=addr.getCountryName()+" "+addr.getPostal_code()+", Block "+addr.getBlock_number()+", "+addr.getStreet_address()+", #"+addr.getUnit_number();
								 String value = addr.getAddr_id() + "~" + fullAddr;
							%>
							<option value="<%=value%>">
								<%=fullAddr%>
							</option>
							
							<%
							}
							%>
						</select>

					</div>
				</div>


				<!-- Card elements -->
				<div class="p-2 pb-10 rounded shadow ">
					<h2 class="text-lg font-bold my-3">Card details</h2>
					<div class="border border-b border-gray-300 mt-2"></div>
					<div class="mt-8" id="card-element"></div>
				</div>
				<!-- Show the subtotal -->
				<input type="hidden" name="totalAmount" value="<%=totalAmt%>">
				<input type="hidden" name="gstPercent" value="<%=gstPercent%>">
				<input type="hidden" name="action" value="payment">
				<div
					class="bg-white flex justify-between rounded shadow px-5 py-5 h-30 mt-10">
					<div>
						<p class="text-md font-semibold my-2">
							Subtotal: $<%=String.format("%.2f", subtotal)%>
						</p>
						<p class="text-md font-semibold my-2">
							GST(<%=gstPercent%>%): $<%=String.format("%.2f", gst)%>
						</p>
						<p class="text-lg font-bold my-2">
							Total Amount: $<%=String.format("%.2f", totalAmt)%>
						</p>
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
		var modAddrButton = document.getElementById("modAddr");
		modAddrButton.addEventListener("click", function() {
			if (<%=validatedUserID%> != null) {
				window.location.href = "/CA1-assignment/ModifyAddressPage?userIDAvailable=true&from=checkout";
			} else {
				window.location.href = "/CA1-assignment/ModifyAddressPage?from=checkout";
			}
		});
	</script>
	<%
	} else {
	%>
	<script>
		if (<%=validatedUserID%>== null) {
			window.location.href = "registrationPage.jsp";
		} else {
			var closeButton = document.getElementById("close");
			showModal("Error loading page");
			closeButton.addEventListener("click", function() {
			window.location.href = "/CA1-assignment/CartPage?userIDAvailable=true";
		});
	}
	</script>
	<%
	}
	%>
</body>
</html>