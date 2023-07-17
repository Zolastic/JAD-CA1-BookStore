<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: View Users Order By Postal Code</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/admin/css/viewManagementSystem.css">
<script src="https://kit.fontawesome.com/8c8a7e5c88.js"
	crossorigin="anonymous"></script>
</head>
<body>
	<%@ page import="java.util.*, model.*"%>
	<%@include file="./navbar.jsp"%>
	<%
	List<UserAddress> users = (List<UserAddress>) request.getAttribute("users");
	String sCurrentPage = request.getParameter("page");

	if (sCurrentPage == null) {
		sCurrentPage = "1";
	}

	int iCurrentPage = Integer.parseInt(sCurrentPage);
	int itemsPerPage = 10;

	int startIndex = (iCurrentPage - 1) * itemsPerPage;
	int endIndex = Math.min(startIndex + itemsPerPage, users.size());

	List<UserAddress> usersPerPage = users.subList(startIndex, endIndex);

	int totalUsers = users.size();
	int totalPages = (int) Math.ceil((double) totalUsers / itemsPerPage);
	totalPages = totalPages == 0 ? 1 : totalPages;

	String userInput = request.getParameter("userInput");

	String pageURL = String.format("%s/admin/ViewUsers?%spage=", request.getContextPath(),
			userInput == null ? "" : "userInput=" + userInput + "&");
	%>
	<header class="viewHeader mt-16">
		<div class="h-64 flex flex-col justify-center items-center">
			<h1 class="font-bold text-2xl my-2 tracking-wider">User
				Management System</h1>
			<form action="<%=request.getContextPath()%>/admin/ViewUsers"
				method="get" class="my-2">
				<input id="userInput" name="userInput" type="text"
					value="<%=userInput == null ? "" : userInput%>"
					class="w-[444px] h-10 px-5 py-3 text-lg rounded-full border-2 border-blue-300 focus:border-l-blue-300 outline-none transition text-greyAccent placeholder:text-gray-300"
					placeholder="Search for a user here!" />
			</form>
		</div>
		<div class="flex justify-end items-end pb-3">
			<a class="" href="<%=request.getContextPath()%>/admin/addUser.jsp"><i
				class="viewIcons fa-solid fa-plus fa-2xl mx-3 hover:cursor-pointer"></i></a>
		</div>
	</header>

	<div class="flex space-x-2">
		<div class="my-5 mx-10">
			<button id="dropdownDefaultButton" data-dropdown-toggle="dropdown"
				class="text-amber-800 bg-pink-100 hover:bg-pink-100 focus:ring-4 focus:outline-none focus:ring-pink-100 font-medium rounded-lg text-sm px-5 py-2.5 text-center inline-flex items-center"
				type="button">
				Filter By  
				<svg class="w-2.5 h-2.5 ml-2.5" aria-hidden="true"
					xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 10 6">
    <path stroke="currentColor" stroke-linecap="round"
						stroke-linejoin="round" stroke-width="2" d="m1 1 4 4 4-4" />
  </svg>
			</button>
			<div id="dropdown"
				class="z-10 hidden bg-white divide-y divide-gray-100 rounded-lg shadow w-44">
				<ul class="py-2 text-sm text-gray-700"
					aria-labelledby="dropdownDefaultButton">
					<li><a href="<%=request.getContextPath()%>/admin/ViewUsers"
						class="block px-4 py-2 hover:bg-pink-100">Nothing</a>
					</li>
					<li><a href="<%=request.getContextPath()%>/admin/ViewUserByPostalCode"
						class="block px-4 py-2 hover:bg-pink-100">Postal Code</a>
					</li>
				</ul>
			</div>


		</div>
		
		<div class="flex flex-col grow">
		<%
		if (users.size() > 0) {
			for (UserAddress user : usersPerPage) {
		%>
		<div class="flex py-3 my-5 mx-10 rounded-lg shadow-lg bg-gray-50">
			<a class="hover:cursor-pointer hover:text-amber-900"
				href="<%=request.getContextPath()%>/admin/UserDetails?userID=<%=user.getUserID()%>">
				<%
				if ((user.getImage()) == null) {
				%> <img alt=""
				src="<%=request.getContextPath()%>/admin/img/defaultUserPFP.png"
				class="w-48 h-48 mx-10 object-cover rounded-full"> <%
 } else {
 %> <img alt="" src="data:image/png;base64, <%=user.getImage()%>"
				class="w-48 h-48 mx-10 object-cover rounded-full"> <%
 }
 %>
			</a>
			<div class="flex flex-col ml-10">
				<a class="hover:cursor-pointer hover:text-amber-900"
					href="<%=request.getContextPath()%>/admin/UserDetails?userID=<%=user.getUserID()%>"><h1
						class="text-3xl font-bold"><%=user.getName()%></h1></a>
				<p class="mt-5 text-lg">Postal Code: <%=user.getPostalCode()%></p>
				<p class="mt-5 text-lg">Street Address: <%=user.getStreetAddress()%></p>
				<p class="mt-5 text-lg">Block: <%=user.getBlockNumber()%></p>
				<p class="mt-5 text-lg">Unit No.: <%=user.getUnitNumber()%></p>
				<p class="mt-5 text-lg"><%=user.getEmail()%></p>
			</div>
			<div class="flex-grow"></div>
			<div class="flex">
				<a
					href="<%=request.getContextPath()%>/admin/EditUserProfile?userID=<%=user.getUserID()%>"><i
					class="viewIcons fa-solid fa-pencil fa-lg mx-3 hover:cursor-pointer"></i></a>
				<a
					href="<%=request.getContextPath()%>/admin/EditUserPassword?userID=<%=user.getUserID()%>"><i
					class="viewIcons fa-solid fa-key fa-lg mx-3 hover:cursor-pointer"></i></a>
				<a class="m-0 p-0 toggleButton" data-user-id="<%=user.getUserID()%>"
					data-user-name="<%=user.getName()%>"> <i
					class="viewIcons fa-solid fa-trash fa-lg mx-3 hover:cursor-pointer"></i>
				</a>
			</div>
		</div>
		<%
		}
		} else {
		%>
		<div class="flex justify-center items-center mt-5">
			<h1 class="text-xl font-semibold">There is no such user in the
				store!</h1>
		</div>
		<%
		}
		%>

		<!-- pagination -->
		<div class="flex justify-center items-center mb-2">
			<ul class="inline-flex items-center -space-x-px">
				<li><a href="<%=pageURL + (iCurrentPage - 1)%>"
					class="<%=iCurrentPage == 1 ? "paginationDisabled pointer-events-none" : "paginationEnabled"%> block px-3 py-2 ml-0 leading-tight border border-gray-300 rounded-l-lg">&laquo;</a></li>
				<li>
					<%
					for (int i = 1; i <= totalPages; i++) {
					%> <a href="<%=pageURL + i%>"
					class="paginationEnabled px-3 py-2 leading-tight border border-gray-300"><%=i%></a>
					<%
					}
					%>
				</li>
				<li><a href="<%=pageURL + (iCurrentPage + 1)%>"
					class="<%=iCurrentPage == totalPages ? "paginationDisabled pointer-events-none" : "paginationEnabled"%> block px-3 py-2 leading-tight border border-gray-300 rounded-r-lg">&raquo;</a></li>
			</ul>
		</div>


		<!-- Modal for confirm delete -->
		<div id="modal"
			class="fixed inset-0 flex items-center justify-center z-50 hidden">
			<div class="bg-white p-8 rounded shadow-lg rounded-lg">
				<h2 class="text-2xl m-0 p-0">Are you sure you want</h2>
				<h2 class="text-2xl m-0 p-0">
					to Delete <span id="userTitle" class="m-0 p-0 text-2xl font-bold"></span>
				</h2>
				<div class="flex mt-5">
					<form id="deleteForm" method="post"
						action="<%=request.getContextPath()%>/admin/DeleteUser">
						<input type="hidden" id="userID" name="userID" value="">
						<button type="submit"
							class="relative inline-flex items-center justify-center p-0.5 mb-2 mr-2 overflow-hidden text-sm font-medium text-gray-900 rounded-lg group bg-gradient-to-br from-purple-500 to-pink-500 group-hover:from-purple-500 group-hover:to-pink-500 hover:text-white focus:ring-4 focus:outline-none focus:ring-purple-200">
							<span
								class="relative px-5 py-2.5 transition-all ease-in duration-75 bg-white rounded-md group-hover:bg-opacity-0 text-black hover:cursor-pointer">
								Delete User </span>
						</button>
					</form>
					<a id="closeButton"
						class="relative inline-flex items-center justify-center p-0.5 mb-2 mr-2 overflow-hidden text-sm font-medium text-gray-900 rounded-lg group bg-gray-500 hover:bg-gray-700 hover:text-white focus:ring-4 focus:outline-none focus:ring-purple-200">
						<span
						class="relative px-5 py-2.5 transition-all ease-in duration-75 bg-white rounded-md group-hover:bg-opacity-0 text-black hover:cursor-pointer">
							Cancel</span>
					</a>
				</div>
			</div>
		</div>
		
		<script>
			const dropdownButton = document
					.getElementById('dropdownDefaultButton');
			const dropdownMenu = document.getElementById('dropdown');

			dropdownButton.addEventListener('click', function() {
				dropdownMenu.classList.toggle('hidden');
			});
		</script>

		<script>
			const items = document.getElementsByClassName('toggleButton');
			for (let i = 0; i < items.length; i++) {
				items[i]
						.addEventListener(
								"click",
								function() {
									const userID = this
											.getAttribute("data-user-id");
									const userName = this
											.getAttribute("data-user-name");

									document.getElementById("userTitle").textContent = userName;
									document.getElementById("userID").value = userID;

									document.getElementById("modal").classList
											.toggle("hidden");
								});
			}
			document.getElementById("closeButton").addEventListener(
					"click",
					function() {
						document.getElementById("modal").classList
								.add("hidden");
					});
		</script>
	</div>
		
	</div>
	
</body>
</html>
