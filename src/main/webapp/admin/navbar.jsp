<%@include file="../tailwind-css.jsp"%>
<nav class="bg-gray-900 fixed w-full z-20 top-0 left-0 border-b border-gray-600">
  <div class="max-w-screen-xl flex flex-wrap items-center justify-between mx-auto p-4">
  <a href="<%=request.getContextPath()%>/admin/index.jsp" class="flex items-center">
      <img src="<%=request.getContextPath()%>/admin/img/ink-bottle.png" class="h-8 mr-3" alt="Flowbite Logo">
      <span class="self-center text-2xl font-semibold whitespace-nowrap tracking-wide text-white">Inkwell</span>
  </a>
  <form class="flex md:order-2" action="<%=request.getContextPath()%>/Logout" method="post">
      <button type="submit" class="text-amber-800 bg-pink-100 hover:bg-pink-200 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-4 py-2 text-center mr-3 md:mr-0">Log Out</button>
  </form>
  <div class="items-center justify-between hidden w-full md:flex md:w-auto md:order-1" id="navbar-sticky">
    <ul class="flex flex-col p-4 md:p-0 mt-4 font-medium border border-gray-100 rounded-lg bg-gray-50 md:flex-row md:space-x-8 md:mt-0 md:border-0 bg-gray-800 md:bg-gray-900 border-gray-700">
      <li>
        <a href="<%=request.getContextPath()%>/" class="block py-2 pl-3 pr-4 text-gray-100 rounded hover:bg-gray-100 md:hover:bg-transparent md:hover:text-pink-100 md:p-0 ">Home (Customer)</a>
      </li>
      <li>
        <a href="<%=request.getContextPath()%>/admin/ViewBooks" class="block py-2 pl-3 pr-4 text-gray-100 rounded hover:bg-gray-100 md:hover:bg-transparent md:hover:text-pink-100 md:p-0 ">Books</a>
      </li>
      <li>
        <a href="<%=request.getContextPath()%>/admin/ViewUsers" class="block py-2 pl-3 pr-4 text-gray-100 rounded hover:bg-gray-100 md:hover:bg-transparent md:hover:text-pink-100 md:p-0 ">Users</a>
      </li>
      <li>
        <a href="<%=request.getContextPath()%>/admin/ViewAuthors" class="block py-2 pl-3 pr-4 text-gray-100 rounded hover:bg-gray-100 md:hover:bg-transparent md:hover:text-pink-100 md:p-0 ">Authors</a>
      </li>
      <li>
        <a href="<%=request.getContextPath()%>/admin/ViewPublishers" class="block py-2 pl-3 pr-4 text-gray-100 rounded hover:bg-gray-100 md:hover:bg-transparent md:hover:text-pink-100 md:p-0 ">Publishers</a>
      </li>
      <li>
        <a href="<%=request.getContextPath()%>/admin/ViewGenres" class="block py-2 pl-3 pr-4 text-gray-100 rounded hover:bg-gray-100 md:hover:bg-transparent md:hover:text-pink-100 md:p-0 ">Genres</a>
      </li>
      <li>
        <a href="<%=request.getContextPath()%>/admin/SalesManagementServlet" class="block py-2 pl-3 pr-4 text-gray-100 rounded hover:bg-gray-100 md:hover:bg-transparent md:hover:text-pink-100 md:p-0 ">Sales</a>
      </li>
    </ul>
  </div>
  </div>
</nav>
