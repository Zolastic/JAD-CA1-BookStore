<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Inkwell: Edit Profile</title>
<%@include file="../tailwind-css.jsp"%>
<script src="https://kit.fontawesome.com/8c8a7e5c88.js"
	crossorigin="anonymous"></script>
</head>
<body>
	<%@ page import="java.util.*, model.*"%>
	<%
	User user = (User) request.getAttribute("user");
	String image = user.getImage() == null ? request.getContextPath() + "/publicAndCustomer/img/defaultUserPFP.png" : "data:image/png;base64, " + user.getImage();
	%>

	<%@include file="navBar/headerNavCustomer.jsp"%>
	<div class="my-8 mx-48">
		<h1 class="text-2xl font-bold tracking-wide mt-28 mb-8 p-0">Edit
			Profile</h1>
		<form id="upload-form" action="<%=request.getContextPath()%>/EditProfile" method="post"
			enctype="multipart/form-data">
			<input type="text" name="userID" id=""
			value="<%=user.getUserID()%>" class="hidden"
				placeholder=" " required />

			<!-- profile picture/ image -->
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
					</div> <input id="dropzone-file" type="file" class="hidden" name="image"
					value="<%=user.getImage()%>" />
				</label>
				<img id="pfp" class="pfpImage ml-2 border-2 border-pink-100 bg-gray-50 w-64 h-64 rounded-full flex justify-center items-center" src="<%= image %>">
			</div>


			<!-- name -->
			<div class="relative z-0 w-full mt-5 mb-8 group">
				<input type="text" name="name" id="name" value="<%=user.getName()%>"
					class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
					placeholder=" " required /> <label for="name"
					class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Name</label>
			</div>

			<!-- email -->
			<div class="relative z-0 w-full mb-8 group">
				<input type="email" name="email" id="email"
					value="<%=user.getEmail()%>"
					class="block py-2.5 px-0 w-full text-sm text-gray-900 bg-transparent border-0 border-b-2 border-gray-300 appearance-none focus:outline-none focus:ring-0 focus:border-pink-100 peer"
					placeholder=" " required /> <label for="email"
					class="peer-focus:font-medium absolute text-sm text-gray-500 duration-300 transform -translate-y-6 scale-75 top-3 -z-10 origin-[0] peer-focus:left-0 peer-focus:text-amber-800 peer-placeholder-shown:scale-100 peer-placeholder-shown:translate-y-0 peer-focus:scale-75 peer-focus:-translate-y-6">Email</label>
			</div>

			<button type="submit"
				class="text-amber-800 bg-pink-100 hover:bg-pink-200 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm w-full sm:w-auto px-5 py-2.5 text-center">Save
				Changes!</button>
		</form>
	</div>
</body>
<script>
	function handleDragOver(e) {
		e.preventDefault();
		e.currentTarget.classList.add("border-blue-500"); // Add a visual indication when dragging over the dropzone
	}

	function handleDragLeave(e) {
		e.currentTarget.classList.remove("border-blue-500"); // Remove the visual indication when leaving the dropzone
	}

	function handleDrop(e) {
		e.preventDefault();
		e.currentTarget.classList.remove("border-blue-500"); // Remove the visual indication when dropping the file

		const file = e.dataTransfer.files[0]; // Get the dropped file
		// Do something with the file (e.g., upload it or display preview)
		const dropzoneFileInput = document.getElementById("dropzone-file");
	    dropzoneFileInput.files = e.dataTransfer.files;
	    //dropzoneFileInput.value = file.name;

		// Example: Display a preview image
		const reader = new FileReader();
		reader.onload = function(e) {
			const imagePreview = document.getElementById("pfp");
			imagePreview.src = e.target.result;
		};
		reader.readAsDataURL(file);
	}
</script>
</html>