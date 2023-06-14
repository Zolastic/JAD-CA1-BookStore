<!-- Modal(hide by default) -->
<div class="w-full h-full">
	<div id="modal"
		class="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50 hidden">
		<div id="modalContent"
			class="flex items-center justify-center flex-col bg-white p-5 rounded-lg w-64">
			<h2 id="modalMessage" class="text-lg font-bold text-center"></h2>
			<button id="close"
				class="mt-4 bg-slate-600 hover:bg-slate-700 text-white px-4 py-2 rounded">OK</button>
		</div>
	</div>
</div>
<!-- Script for modal -->
<script>
	function showModal(message) {
		var modal = document.getElementById("modal");
		var modalContent = document.getElementById("modalContent");
		var modalMessage = document.getElementById("modalMessage");
		var closeButton = document.getElementById("close");

		modalMessage.innerText = message;
		modal.classList.remove("hidden");

		closeButton.onclick = function() {
			modal.classList.add("hidden");
		};
	}
</script>