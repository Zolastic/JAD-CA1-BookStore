<%--
  - Author(s): Soh Jian Min (P2238856)
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA2
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
	<%@ include file="customerModalYesCancel.jsp"%>
	<%@ include file="navBar/headerNavCustomer.jsp"%>
	<div class="px-10 pt-10">
		<%
		boolean error = false;
		String validatedUserID = (String) request.getAttribute("validatedUserID");
		String pageBack = request.getParameter("from");
		String deleteError = request.getParameter("deleteError");
		List<Address> addresses = null;
		try {
			addresses = (List<Address>) request.getAttribute("addresses");
		} catch (ClassCastException e) {
			error = true;
		}
		String urlBack = "";
		if (pageBack != null) {
			if (pageBack.equals("profile")) {
				urlBack = request.getContextPath() + "/ProfilePage?userID=" + validatedUserID;
			} else {
				urlBack = request.getContextPath() + "/CheckoutPage?userIDAvailable=true";
			}
		}

		if (deleteError != null) {
			out.print("<script>showModal('Error Deleting Address, Try Again Later')</script>");

		}
		if (validatedUserID != null && addresses != null && pageBack != null && !error) {
		%>

		<div class="flex items-center justify-between">
			<h1 class="text-3xl font-bold mb-5">Your Address</h1>
			<span class="cursor-pointer text-gray-500 hover:text-gray-700"
				onclick="goBack()"> <i
				class="fas fa-times fa-2x m-5 pt-2 mr-10"></i>
			</span>

		</div>
		<!-- Add New Address button -->
		<div class="flex justify-start mb-5">
			<button type="button"
				class="px-4 py-2 text-white rounded bg-slate-500 hover:bg-slate-600"
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
							Unit Number: <%=address.getUnit_number()%></p>
					</div>
					<div class="flex items-center">
						<form id="edit-form"
							action="<%=request.getContextPath()%>/EditAddressPage"
							method="get">
							<input type="hidden" name="userIDAvailable" value="true">
							<input type="hidden" name="from" value="<%=pageBack%>"> <input
								type="hidden" name="addr_id" value="<%=address.getAddr_id()%>">
							<button type="submit" class="text-gray-800 hover:text-black mx-3">
								<i class="fas fa-edit transform hover:scale-110"></i>
							</button>
						</form>

						<form id="delete-form_<%=address.getAddr_id()%>"
							action="<%=request.getContextPath()%>/DeleteAddress"
							method="post">
							<input type="hidden" name="addr_id"
								value="<%=address.getAddr_id()%>"> <input type="hidden"
								name="from" value="<%=pageBack%>">
						</form>
						<button class="text-red-600 hover:text-red-800 mx-3 mr-4"
							onclick="showModalYesCancel('Are you sure you want to delete this address?', 'delete-form_<%=address.getAddr_id()%>', null)">
							<i class="fas fa-trash-alt transform hover:scale-110"></i>
						</button>
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
                var url = "<%=request.getContextPath()%>/AddAddressPage?userIDAvailable=true&from=<%=pageBack%>";
                window.location.href = url;
            });
        </script>


		<%
		} else {
		%>
		<script>
            if (<%=validatedUserID%> == null) {
                window.location.href = "registrationPage.jsp";
            } else if(<%=pageBack%>==null){
            	var closeButton = document.getElementById("close");
				showModal("Error loading page");
				closeButton.addEventListener("click", function() {
					window.location.href = "<%=request.getContextPath()%>/";
				});
            }
            else {
                var closeButton = document.getElementById("close");
                showModal("Error loading page");
                closeButton.addEventListener("click", function() {
                    window.location.href = "<%=request.getContextPath()%>/";
                });
            }
        </script>
		<%
		}
		%>

	</div>
	<script>
        function goBack() {
            var urlBack = '<%=urlBack%>';
			window.location.href = urlBack;
		}
	</script>
</body>
</html>
