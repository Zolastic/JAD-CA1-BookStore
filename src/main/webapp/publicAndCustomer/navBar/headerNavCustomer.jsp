<!--
  - Author(s): Soh Jian Min
  - Date:6/6/2023
  - Copyright Notice:-
  - @(#)
  - Description: JAD CA1
  -->
<nav class="bg-gray-500">
	<div class="flex items-center justify-between h-20 w-full">
		<div class="flex items-center ml-5">
			<img src="/CA1-assignment/publicAndCustomer/img/ink-bottle.png"
				class="h-8 mr-3" alt="Flowbite Logo"> <span class="self-center text-2xl font-semibold whitespace-nowrap text-white">Inkwell</span>
		</div>
		<div class="flex items-center justify-center">
			<a href="/CA1-assignment/publicAndCustomer/home.jsp"
				class="text-white hover:text-blue-900 px-3 py-2 text-sm font-bold">HOME</a>
			<a href="/CA1-assignment/allBooksPage?userIDAvailable=true"
				class="text-white hover:text-blue-900 px-3 py-2 text-sm font-bold">BOOK</a>
			<a href="/CA1-assignment/categoryMenuPage?userIDAvailable=true"
				class="text-white hover:text-blue-900 px-3 py-2 text-sm font-bold">CATEGORIES</a>
		</div>


		<div class="flex items-center mr-5">
			<span class="text-white pr-5"> <a
				href="/CA1-assignment/cartPage?userIDAvailable=true"> <i
					class="fas fa-shopping-cart text-3xl transform hover:scale-110"></i>
			</a>
			</span> <span class="text-white pr-5"> <i
				class="fas fa-user text-3xl transform hover:scale-110"></i>
			</span>
			<%
			if (((String) session.getAttribute("role")).equals("admin")) {
			%>
			<a href=""
				class="bg-white text-black hover:bg-gray-400 hover:text-white transform hover:scale-110 px-3 py-2 text-sm font-bold mr-2">Admin</a>
			<%
			}
			%>

			<a href=""
				class="bg-slate-600 text-white hover:bg-slate-800 transform hover:scale-110 px-3 py-2 text-sm font-bold">LOGOUT</a>
		</div>



	</div>
</nav>
