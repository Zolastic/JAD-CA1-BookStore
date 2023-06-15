<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/publicAndCustomer/css/login.css">
<script src="https://kit.fontawesome.com/8c8a7e5c88.js"
	crossorigin="anonymous"></script>

<div class="w-1/2  h-full flex-wrap justify-center p-12">
	<h1 class="loginTextHeader text-center text-3xl font-bold">OTP Verification</h1>
	<form class="mt-6" action="<%=request.getContextPath()%>/OTP"
		method="post">
		
		<div class="flex justify-center items-center">
			<input class="m-2 border h-10 w-10 text-center form-control rounded-lg" type="text" id="first" name="first" maxlength="1" /> 
            <input class="m-2 border h-10 w-10 text-center form-control rounded-lg" type="text" id="second" name="second" maxlength="1" /> 
            <input class="m-2 border h-10 w-10 text-center form-control rounded-lg" type="text" id="third" name="third" maxlength="1" /> 
            <input class="m-2 border h-10 w-10 text-center form-control rounded-lg" type="text" id="fourth" name="fourth" maxlength="1" />
            <input class="m-2 border h-10 w-10 text-center form-control rounded-lg" type="text" id="fifth" name="fifth" maxlength="1" /> 
            <input class="m-2 border h-10 w-10 text-center form-control rounded-lg" type="text" id="sixth" name="sixth" maxlength="1" />
		</div>
		
		<%
		String statusCode = request.getParameter("statusCode");
		if (statusCode != null) {
			if (statusCode.equals("401")) {
		%>
		<h1 class="loginErrorMsg">Uh-oh! Invalid OTP</h1>
		<%
		}
		}
		%>
		<div class="field-wrapper flex justify-center my-2">
			<a href="#" class="loginLinks">Resend OTP</a>
		</div>
		<div class="field-wrapper flex justify-center my-2">
			<span class="mr-1">Back to</span>
			<a
				href="<%=request.getContextPath()%>/publicAndCustomer/registrationPage.jsp?"
				class="loginLinks">Log In</a>
		</div>
		<div class="field-wrapper flex justify-center mt-5">
			<button type="submit"
				class="loginButton p-4 text-amber-800 bg-pink-200 hover:bg-pink-250 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center">Verify OTP!</button>
		</div>
	</form>
	
	<script>
		const inputs = document.getElementsByTagName('input');
		console.log('Length', inputs.length);
		for (let i = 0; i < inputs.length; i++) {
			inputs[i].addEventListener('keyup', (event) => {
				if (event.target.value.length == 1) {
					const nextElement = event.target.nextElementSibling;
					if (nextElement != null) {
						nextElement.focus();
					} else {
						document.getElementsByTagName('form')[0].submit();
					}
				}
			});
		}
		
		document.addEventListener("paste", (e) => {
		  	if (e.target.type === "text") {
		   	var data = e.clipboardData.getData('Text').split('');
		   	for (let i = 0; i < Math.min(inputs.length, data.length); i++) {
		   		inputs[i].value = data[i];	
		   	}
		   	
		   	if (data.length >= 6) {
		   		document.getElementsByTagName('form')[0].submit();
		   	}
		  }
		});
	</script>
</div>
