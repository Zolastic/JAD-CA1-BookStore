<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Admin: Edit Book</title>
<%@include file="../tailwind-css.jsp"%>
<link rel="stylesheet"
	href="<%=request.getContextPath()%>/admin/css/feedbackMessages.css">
</head>
<body class="my-8 mx-48">
	<%@ page import="java.util.*, model.*"%>
	<%@include file="./navbar.jsp"%>
	<%
	Book book = (Book) request.getAttribute("book");
	List<Author> authors = (List<Author>) request.getAttribute("authors");
	List<Publisher> publishers = (List<Publisher>) request.getAttribute("publishers");
	List<Genre> genres = (List<Genre>) request.getAttribute("genres");
	String statusCode = request.getParameter("statusCode");
	String image = book.getImg() == null ? request.getContextPath() + "/admin/img/No_Image_Available.jpg" : "data:image/png;base64, " + book.getImg();
	
	%>
	<h1 class="text-2xl font-bold tracking-wide mt-28 mb-8 p-0">Edit Book (<%= book.getTitle() %>)</h1>
	<form class="mt-3" action="<%=request.getContextPath()%>/admin/EditBook"
		method="post" enctype="multipart/form-data">

		<!-- image -->
			<div class="flex items-center justify-center w-full">
				<label for="dropzone-file"
					class="flex flex-col items-center justify-center w-full h-64 border-2 border-pink-100 rounded-lg cursor-pointer bg-gray-50"
					ondragover="handleDragOver(event)"
					ondragleave="handleDragLeave(event)" ondrop="handleDrop(event)">
					<div class="flex flex-col items-center justify-center pt-5 pb-6">
						<p class="mb-2 text-sm text-gray-500 dark:text-gray-400">
							<span class="font-semibold text-amber-800">Profile Picture</span>
						</p>
						<p class="mb-2 text-sm text-gray-500 dark:text-gray-400">
							<span class="font-semibold">Click to upload</span> or drag and
							drop
						</p>
						<p class="text-xs text-gray-500 dark:text-gray-400">SVG, PNG,
							JPG or GIF</p>
					</div> <input id="dropzone-file" type="file" class="hidden" name="image" onchange="handleFileSelect(event)" />
				</label> <img id="pfp"
					class="pfpImage ml-2 border-2 border-pink-100 bg-gray-50 w-36 h-64 rounded-md flex justify-center items-center object-cover"
					src="<%= image %>">
			</div>

		<input type="text" name="bookID" id="bookID"
			value="<%=book.getBookID()%>" class="hidden" placeholder=" " required />
		<!-- title -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="text" name="title" id="title"
				value="<%=book.getTitle()%>"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
				placeholder=" " required /> <label for="title"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Title</label>
		</div>

		<!-- ISBN -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="text" name="isbn" id="isbn" value="<%=book.getISBN()%>"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
				placeholder=" " required /> <label for="isbn"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">ISBN</label>
		</div>

		<!-- description -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="text" name="description" id="description"
				value="<%=book.getDescription()%>"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
				placeholder=" " required /> <label for="description"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Description</label>
		</div>

		<!-- price -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="number" name="price" id="price" step="0.01"
				value="<%=book.getPrice()%>"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
				placeholder=" " required /> <label for="price"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Price</label>
		</div>

		<!-- author -->
		<div class="relative z-0 w-full mb-8 group">
			<select
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
				name="author" required>
				<%
				for (Author author : authors) {
					if (author.getName().equals(book.getAuthor())) {
				%>
				<option value="<%=author.getId()%>" selected><%=author.getName()%></option>
				<%
				} else {
				%>
				<option value="<%=author.getId()%>"><%=author.getName()%></option>
				<%
				}
				}
				%>
			</select> <label for="author"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Author</label>
		</div>


		<!-- publisher -->
		<div class="relative z-0 w-full mb-8 group">
			<select
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
				name="publisher" required>
				<%
				for (Publisher publisher : publishers) {
					if (publisher.getName().equals(book.getPublisher())) {
				%>
				<option value="<%=publisher.getId()%>" selected><%=publisher.getName()%></option>
				<%
				} else {
				%>
				<option value="<%=publisher.getId()%>"><%=publisher.getName()%></option>
				<%
				}
				}
				%>
			</select> <label for="publisher"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Publisher</label>
		</div>

		<!-- publication date -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="date" name="date" id="date"
				value="<%=book.getPublication_date()%>"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
				placeholder=" " required /> <label for="date"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Publication
				Date</label>
		</div>

		<!-- number of books sold -->
		<div class="relative z-0 w-full mb-8 group">
			<input type="number" name="sold" id="sold"
				value="<%=book.getSold()%>"
				class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
				placeholder=" " required /> <label for="sold"
				class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">
				Sold</label>
		</div>

		<!-- quantity and genre/ category -->
		<div class="grid md:grid-cols-2 md:gap-6">
			<div class="relative z-0 w-full mb-8 group">
				<input type="number" name="quantity" id="quantity"
					value="<%=book.getInventory()%>"
					class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
					placeholder=" " required /> <label for="quantity"
					class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Quantity</label>
			</div>
			<div class="relative z-0 w-full mb-8 group">
				<select
					class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
					name="genre" required>
					<%
					for (Genre genre : genres) {
						if (genre.getName().equals(book.getGenreName())) {
					%>
					<option value="<%=genre.getId()%>" selected><%=genre.getName()%></option>
					<%
					} else {
					%>
					<option value="<%=genre.getId()%>"><%=genre.getName()%></option>
					<%
					}
					}
					%>
				</select> <label for="genre"
					class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Genre</label>
			</div>
		</div>
		
		<div class="-mt-1 mb-2">
			<%
			if (statusCode != null) {
				if (statusCode.equals("200")) {
			%>
			<h1 class="successMessage tracking-wide">Book successfully updated!</h1>
			<%
			} else {
			%>
			<h1 class="errorMessage tracking-wide">Uh-oh! Error</h1>
			<%
			}
			}
			%>
		</div>
		
		<button type="submit"
			class="text-amber-800 bg-pink-100 hover:bg-pink-200 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center">Save
			Changes!</button>
	</form>
</body>
<script>
	function handleDragOver(e) {
		e.preventDefault();
		e.currentTarget.classList.add("border-pink-200");
	}

	function handleDragLeave(e) {
		e.currentTarget.classList.remove("border-pink-200");
	}

	function handleDrop(e) {
		e.preventDefault();
		e.currentTarget.classList.remove("border-pink-200");

		const file = e.dataTransfer.files[0];
		const dropzoneFileInput = document.getElementById("dropzone-file");
		dropzoneFileInput.files = e.dataTransfer.files;

		const reader = new FileReader();
		reader.onload = function(e) {
			const imagePreview = document.getElementById("pfp");
			imagePreview.src = e.target.result;
		};
		reader.readAsDataURL(file);
	}

	function handleFileSelect(e) {
		const file = event.target.files[0];
		const reader = new FileReader();

		reader.onload = function(e) {
			const imagePreview = document.getElementById("pfp");
			imagePreview.src = e.target.result;
		};

		reader.readAsDataURL(file);
	}
</script>
</html>