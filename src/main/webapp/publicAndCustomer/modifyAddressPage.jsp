<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA1
  --%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="model.Address"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<title>Modify Address Page</title>
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
		String validatedUserID = (String) request.getAttribute("validatedUserID");
		String pageBack = request.getParameter("from");
		List<Address> addresses = (List<Address>) request.getAttribute("addresses");
		%>
		<h1 class="text-3xl font-bold mb-5">Your Address</h1>
		<%
		if (validatedUserID != null && addresses != null) {
		%>
		<!-- Add New Address button -->
		<div class="flex justify-end mb-5">
			<button type="button"
				class="px-4 py-2 bg-gray-300 rounded text-gray-800"
				id="insertNewAddr">Add New Address</button>
		</div>
		<!-- Show all the addresses -->
		<div class="grid gap-4">
			<%
			for (Address address : addresses) {
			%>
			<div class="p-2 rounded shadow">
				<div class="flex justify-between">
					<div>
						<h2 class="text-lg font-bold"><%=address.getCountryName()%>
							<%=address.getPostal_code()%>, Block
							<%=address.getBlock_number()%>,
							<%=address.getStreet_address()%>,
							<%=address.getUnit_number()%></h2>
						<p class="text-sm">
							Postal Code:
							<%=address.getPostal_code()%></p>
						<p class="text-sm">
							Country:
							<%=address.getCountryName()%></p>
						<p class="text-sm">
							Street Address:
							<%=address.getStreet_address()%></p>
						<p class="text-sm">
							Block Number:
							<%=address.getBlock_number()%></p>
						<p class="text-sm">
							Unit Number<%=address.getUnit_number()%></p>
					</div>
					<div class="flex items-center">
						<form id="payment-form"
							action="/CA1-assignment/EditAddressPage?userIDAvailable=true&from=<%=pageBack%>"
							method="post">
							<button type="submit"
								class="mr-2 px-4 py-2 bg-gray-300 rounded text-gray-800">Edit</button>
							<input type="hidden" name="addr_id"
								value="<%=address.getAddr_id() %>">
						</form>

					</div>
				</div>
			</div>
			<%
			}
			%>
		</div>
		<script>
    		var newAddrBttn = document.getElementById("insertNewAddr");
   			newAddrBttn.addEventListener("click", function() {
        		var url = "/CA1-assignment/AddAddressPage?userIDAvailable=true&from=<%=pageBack%>";
				window.location.href = url;
			});
		</script>

		<%
		} else {
		%>
		<script>
			if (
		<%=validatedUserID%>
			== null) {
				window.location.href = "registrationPage.jsp";
			} else {
				var closeButton = document.getElementById("close");
				showModal("Error loading page");
				closeButton
						.addEventListener(
								"click",
								function() {
									window.location.href = "/CA1-assignment/ModifyAddressPage?userIDAvailable=true&from=<%=pageBack%>";
								});
			}
		</script>
		<%
		}
		%>
	</div>
</body>
</html>
