function printReport() {
    const printDiv = document.getElementById('printdiv');
    let cloned = printDiv.cloneNode(true);
  	document.body.appendChild(cloned);
  	cloned.classList.add("printable");
  	cloned.classList.remove("hidden");
  	window.print();
  	document.body.removeChild(cloned);
}
