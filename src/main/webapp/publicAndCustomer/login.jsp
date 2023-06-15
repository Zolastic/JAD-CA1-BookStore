<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/publicAndCustomer/css/login.css">
<script src="https://kit.fontawesome.com/8c8a7e5c88.js"
	crossorigin="anonymous"></script>

<div class="w-1/2  h-full flex-wrap justify-center p-12">
	<h1 class="loginTextHeader text-center text-3xl font-bold">LOG IN</h1>
	<form class="mt-6" action="<%=request.getContextPath()%>/Login"
		method="post">
		<div class="field-wrapper flex relative z-0 w-full mb-8 group">
			<input type="email" name="email" id="email"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
				placeholder=" " required /> <label for="email"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Email</label>
		</div>
		<div class="field-wrapper flex relative z-0 w-full mb-8 group">
			<input type="password" name="password" id="password"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
				placeholder=" " required /> <label for="password"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Password</label>
		</div>
		<%
		String statusCode = request.getParameter("statusCode");
		if (statusCode != null) {
			if (statusCode.equals("401")) {
		%>
		<h1 class="loginErrorMsg">Uh-oh! Invalid Email or Password</h1>
		<%
		}
		}
		%>
		<div class="field-wrapper flex justify-center my-2">
			<a href="/forgetpassword" class="loginLinks">Forget Password?</a>
		</div>
		<div class="field-wrapper flex justify-center my-2">
			<span class="mr-1">Don't have an account?</span>
			<a
				href="<%=request.getContextPath()%>/publicAndCustomer/registrationPage.jsp?type=SignUp"
				class="loginLinks">Sign Up!</a>
		</div>
		<div class="field-wrapper flex justify-center mt-5">
			<button type="submit"
				class="loginButton p-4 text-amber-800 bg-pink-200 hover:bg-pink-250 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center">Login!</button>
		</div>
	</form>
</div>
