<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<%
String registrationType = request.getParameter("type");
if (registrationType != null) {
	if (registrationType.equals("SignUp")) {
%>
<title>Inkwell: Sign Up</title>
<%
} else if (registrationType.equals("OTP")) {
%>
<title>Inkwell: OTP</title>
<%
}
} else {
%>
<title>Inkwell: Login</title>
<%
}
%>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/publicAndCustomer/css/registrationPage.css">
<script src="https://kit.fontawesome.com/8c8a7e5c88.js"
	crossorigin="anonymous"></script>
</head>
<body class="loginPageBody flex justify-center items-center">
	<div class="containerZ main w-screen h-screen flex">
		<div
			class="cardZ mb-2  flex w-2/3 h-4/5 justify-between mx-auto my-20 rounded-md ">
			<div class="flex items-center justify-center left w-1/2 h-auto flex-wrap py-7 rounded-l-md">
				<img
					src="<%=request.getContextPath()%>/publicAndCustomer/img/personReadingBook.png"
					alt="loginPhoto"
					class="registrationPageFloatingImage mx-auto h-auto w-auto object-contain" />
				<h1 class="text-center -mt-2 text-white font-semibold text-xl">
					Discover different dimensions with Inkwell.</h1>
			</div>
			<%
			if (registrationType != null) {
				if (registrationType.equals("SignUp")) {
			%>
			<%@include file="./signUp.jsp"%>
			<%
			} else if (registrationType.equals("OTP")) {
			%>
			<%@include file="./otp.jsp"%>
			<%
			}
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