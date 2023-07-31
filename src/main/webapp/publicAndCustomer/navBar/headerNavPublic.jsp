<nav class="bg-gray-500">
	<div class="flex items-center justify-between h-20 w-full">
		<div class="flex items-center ml-5">
			<img src="<%=request.getContextPath()%>/publicAndCustomer/img/ink-bottle.png"
				class="h-8 mr-3" alt="Flowbite Logo"> <span class="self-center text-2xl font-semibold whitespace-nowrap text-white">Inkwell</span>
		</div>
		<div class="flex items-center justify-center">
			<a href="<%=request.getContextPath()%>/"
				class="text-white hover:text-blue-950 px-3 py-2 text-sm font-bold">HOME</a>
			<a href="<%=request.getContextPath()%>/AllBooksPage"
				class="text-white hover:text-blue-950 px-3 py-2 text-sm font-bold">BOOKS</a>
			<a href="<%=request.getContextPath()%>/CategoryMenuPage"
				class="text-white hover:text-blue-950 px-3 py-2 text-sm font-bold">CATEGORIES</a>
		</div>


		<div class="flex items-center mr-5">
			<a href="<%=request.getContextPath()%>/publicAndCustomer/registrationPage.jsp"
				class="bg-white text-black hover:bg-gray-400 hover:text-white transform hover:scale-110 px-3 py-2 text-sm font-bold mr-2">LOGIN</a>
			<a href="<%=request.getContextPath()%>/publicAndCustomer/registrationPage.jsp?signUp=true"
				class="bg-slate-600 text-white hover:bg-slate-800 transform hover:scale-110 px-3 py-2 text-sm font-bold">SIGNUP</a>
		</div>
	</div>
</nav>
