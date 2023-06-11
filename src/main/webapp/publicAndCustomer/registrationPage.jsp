<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Inkwell: Login</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/publicAndCustomer/css/registrationPage.css">
<script src="https://kit.fontawesome.com/8c8a7e5c88.js"
	crossorigin="anonymous"></script>
</head>
<body class="loginPageBody flex justify-center items-center">
	<div class="containerZ main w-screen h-screen flex">
		<div
			class="cardZ flex w-2/3 h-3/5 justify-between mx-auto my-20 rounded-md overflow-hidden">
			<div class="left  w-1/2 h-full flex-wrap py-7">
				<img
					src="<%=request.getContextPath()%>/publicAndCustomer/img/personReadingBook.png"
					alt="loginPhoto"
					class="registrationPageFloatingImage w-3/5 mx-auto" />
				<h1 class="text-center mt-2 text-white font-semibold text-xl">
					Discover different dimensions with Inkwell.</h1>
			</div>
			<%
			String signUpState = request.getParameter("signUp");
			if (signUpState != null) {
			%>
			<%@include file="./signUp.jsp"%>
			<%
			} else {
			%>
			<%@include file="./login.jsp"%>
			<%
			}
			%>
		</div>
	</div>
</body>
</html>