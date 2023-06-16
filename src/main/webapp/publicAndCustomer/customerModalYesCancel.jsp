<!-- ModalYesCancel (hidden by default) -->
<div class="w-full h-full">
	<div id="modalYesCancel"
		class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 hidden">
		<div id="modalContentYesCancel"
			class="flex items-center justify-center flex-col bg-white p-5 rounded-lg w-64">
			<h2 id="modalMessageYesCancel" class="text-lg font-bold text-center"></h2>
			<div class="flex justify-center mt-4">
				<button id="yes"
					class="mr-2 bg-green-700 hover:bg-green-600 text-white px-4 py-2 rounded">Yes</button>
				<button id="cancel"
					class="bg-rose-900 hover:bg-red-600 text-white px-4 py-2 rounded">Cancel</button>
			</div>
		</div>
	</div>
</div>

<!-- Script for modalYesCancel -->
<script>
	function showModalYesCancel(message, formId, scrollURL) {
		let modal = document.getElementById("modalYesCancel");
		let modalContent = document.getElementById("modalContentYesCancel");
		let modalMessage = document.getElementById("modalMessageYesCancel");
		let yesButton = document.getElementById("yes");
		let cancelButton = document.getElementById("cancel");
		let scrollPosition = window.scrollY;
		document.getElementById(scrollURL).value = scrollPosition;

		modalMessage.innerText = message;
		modal.classList.remove("hidden");

		yesButton.onclick = function() {
			var form = document.getElementById(formId);
			if (form) {
				// Submit the form
				form.submit();
			}
			modal.classList.add("hidden");
		};

		cancelButton.onclick = function() {
			modal.classList.add("hidden");
		};
	}
</script>
