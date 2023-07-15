<%--
  - Author(s): Soh Jian Min (P2238856)
  - Description: JAD CA1
--%>

<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="model.Address"%>
<%@ page import="model.Country"%>
<%@ page import="java.util.*"%>

<!DOCTYPE html>
<html>
<head>
<title>Edit Address Page</title>
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"
	rel="stylesheet">
<%@ include file="../../tailwind-css.jsp"%>
<!-- Include Select2 CSS -->
<link
	href="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/css/select2.min.css"
	rel="stylesheet" />
</head>
<body>
	<%@ include file="customerModal.jsp"%>
	<%@ include file="navBar/headerNavCustomer.jsp"%>

	<%
	String validatedUserID = (String) request.getAttribute("validatedUserID");
	Address addr = (Address) request.getAttribute("addressDetails");
	List<Country> countries = (List<Country>) request.getAttribute("countries");
	String pageBack = request.getParameter("from");
	String err = request.getParameter("error");
	if (err != null) {
		if (err.equals("errEdit")) {
			out.print("<script>showModal('Error Editing Address, Try Again Later')</script>");
		} else if (err.equals("conndbError")) {
			out.print("<script>showModal('Internal Server Error')</script>");
		} else if (err.equals("emptyInput")) {
			out.print("<script>showModal('Empty Input Fields!')</script>");
		}
	}
	String success = request.getParameter("success");
	if (success != null) {
		if (success.equals("true")) {
	%>
	<script>
        var closeButton = document.getElementById("close");
        showModal("Address Updated!");
        closeButton.addEventListener("click", function() {
            window.location.href = "/CA1-assignment/ModifyAddressPage?userIDAvailable=true&from=<%=pageBack%>";
        });
    </script>
	<%
	}
	}
	if (validatedUserID != null && addr != null && countries != null) {
	%>
	<div class="flex items-center justify-center mt-16">
		<form action="/CA1-assignment/EditAddressPage?userIDAvailable=true&from=<%=pageBack%>"
			method="post" id="editAddressForm"
			class="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4">
			<h1 class="text-3xl font-bold my-2 mb-5">Edit Your Address</h1>
			<input type="hidden" name="action" value="addr.getAddr_id()%>">
			<div class="mb-4 flex">
				<label for="unit_number" class="font-bold w-32">Unit Number:</label>
				<input type="text" name="unit_number" id="unit_number"
					value="<%=addr.getUnit_number()%>"
					class="border border-gray-300 p-2 rounded ml-2 w-64" required>
			</div>
			<div class="mb-4 flex">
				<label for="block_number" class="font-bold w-32">Block
					Number:</label> <input type="text" name="block_number" id="block_number"
					value="<%=addr.getBlock_number()%>"
					class="border border-gray-300 p-2 rounded ml-2 w-64" required>
			</div>
			<div class="mb-4 flex">
				<label for="street_address" class="font-bold w-32">Street
					Address:</label> <input type="text" name="street_address"
					id="street_address" value="<%=addr.getStreet_address()%>"
					class="border border-gray-300 p-2 rounded ml-2 w-64" required>
			</div>
			<div class="mb-4 flex">
				<label for="postal_code" class="font-bold w-32">Postal Code:</label>
				<input type="text" name="postal_code" id="postal_code"
					value="<%=addr.getPostal_code()%>"
					class="border border-gray-300 p-2 rounded ml-2 w-64" required>
			</div>
			<div class="mb-4 flex">
				<label for="country" class="font-bold w-32" >Country:</label> <select
					name="country" id="country"
					class="border border-gray-300 p-2 rounded ml-2 w-64"
					style="width: 100%">
					<%
					for (Country country : countries) {
						String countryId = country.getCountryId();
						String countryName = country.getCountryName();
						String selected = "";
						if (addr.getCountryId().equals(countryId)) {
							selected = "selected";
						}
					%>
					<option value="<%=countryId%>,<%=countryName%>" <%=selected%>><%=countryName%></option>
					<%
					}
					%>
				</select>
			</div>

			<div class="flex mb-4 justify-end">
				<button type="submit"
					class="bg-slate-500 hover:bg-slate-700 text-white font-bold py-2 px-4 rounded"
					id="submitBtn">Submit</button>
			</div>
		</form>
	</div>

	<%
	} else {
	%>
	<script>
        showModal("Error loading page");
    </script>
	<%
	}
	%>

	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"></script>
	<!-- Include Select2 JS -->
	<script
		src="https://cdnjs.cloudflare.com/ajax/libs/select2/4.0.13/js/select2.min.js"></script>
	<script>
        // Initialize Select2 on the country dropdown
        $(document).ready(function() {
            $('#country').select2({
                templateSelection: function(data) {
                    return data.text;
                },
                templateResult: function(data) {
                    return data.text;
                }
            });
        });



        // Prevent form submission when there are no changes for the address
        $('#editAddressForm').submit(function(event) {
            var unitNumber = $('#unit_number').val();
            var blockNumber = $('#block_number').val();
            var streetAddress = $('#street_address').val();
            var postalCode = $('#postal_code').val();
            var selectedCountry = $('#country').val();
            var [countryId, countryName] = selectedCountry.split(',');
            var addrId = '<%= addr.getAddr_id() %>';
            var closeButton = document.getElementById("close");
            if (unitNumber === '<%= addr.getUnit_number() %>' &&
                blockNumber === '<%= addr.getBlock_number() %>' &&
                streetAddress === '<%= addr.getStreet_address() %>' &&
                postalCode === '<%= addr.getPostal_code() %>' &&
                countryId === '<%= addr.getCountryId() %>') {
                event.preventDefault();
                $('#country').prop('disabled', true);
                $('#country').addClass('bg-gray-600 cursor-not-allowed');
                showModal('No changes were made to the address.');
        		closeButton.addEventListener("click", function() {
        			$('#country').prop('disabled', false);
                    $('#country').removeClass('bg-black cursor-not-allowed');
                });
            }
        });
    </script>
</body>
</html>
