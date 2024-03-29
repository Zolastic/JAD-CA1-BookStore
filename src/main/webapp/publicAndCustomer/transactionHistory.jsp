<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="model.Book"%>
<%@ page import="model.TransactionHistoryWithItems"%>
<%@ page import="model.TransactionHistoryItemBook"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Transaction History</title>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css" rel="stylesheet">
    <%@ include file="../../tailwind-css.jsp"%>
</head>
<body>
<%
	boolean error = false;
	List<TransactionHistoryWithItems> transactionHistories = null;
    String validatedUserID = (String) request.getAttribute("validatedUserID");
    String scrollPosition = (String) request.getAttribute("scrollPosition");
    try{
    	 transactionHistories = (List<TransactionHistoryWithItems>) request.getAttribute("transactionHistories");
    }catch (ClassCastException e) {
		error = true;
	}
    if (validatedUserID != null && transactionHistories != null && !error) {
%>
<%@ include file="navBar/headerNavCustomer.jsp"%>
<div class="flex items-center justify-between">
    <h1 class="text-3xl font-bold m-5">Transaction History</h1>
    <%
    if (transactionHistories.size() != 0) {
    %>
    <span class="cursor-pointer text-gray-500 hover:text-gray-700"
          onclick="goBack()"><i class="fas fa-times fa-2x m-5 pt-2 mr-10"></i></span>
    <%
    }
    %>
</div>

<%
if (transactionHistories.size() == 0) {
%>
<!-- If user has no transaction history -->
<div class="fixed inset-0 flex flex-col items-center justify-center mt-10">
    <p class="mb-4">No Transaction History</p>
    <button class="bg-slate-500 text-white px-4 py-2 rounded hover:bg-slate-700 transform hover:scale-110"
            onclick="goBack()">Back
    </button>
</div>
<%
} else {
        for (TransactionHistoryWithItems transactionHistory : transactionHistories) {
            double totalAmt = transactionHistory.getTotalAmount();
            double gstPercent=transactionHistory.getGstPercent();
            double subtotal = (totalAmt / (100+gstPercent)) * 100;
            double gst = totalAmt - subtotal;
%>
<!-- Show the transaction history -->
<div class="m-5 shadow-lg p-2 mb-5 border border-gray-300">
    <div class="flex items-center m-2">
        <p class="italic text-lg">
            Transaction Date:
            <%=(new java.text.SimpleDateFormat("dd MMMM yyyy, hh:mm:ss a")).format(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(transactionHistory.getTransactionDate()))%>
        </p>
    </div>
    <div class="border border-gray-300"></div>

    <ul>
        <%
        List<TransactionHistoryItemBook> transactionHistoryItems = transactionHistory.getTransactionHistoryItems();
                                for (TransactionHistoryItemBook transactionItem : transactionHistoryItems) {
                                    String urlToBookDetails = request.getContextPath()+"/BookDetailsPage?bookID=" + transactionItem.getBook().getBookID();
        %>
        <li class="flex items-center justify-between p-4 px-3 border-b border-gray-300">
            <div class="flex items-center">
                <a href="<%=urlToBookDetails%>">
                    <div class="flex-shrink-0 w-16 h-16 mr-4">
                        <% if (transactionItem.getBook().getImg() != null) { %>
                        <img class="h-full object-contain" src="<%=transactionItem.getBook().getImg()%>">
                        <% } else { %>
                        <i class="fas fa-image fa-3x"></i>
                        <% } %>
                    </div>
                </a>

                <div class="w-80">
                    <a href="<%=urlToBookDetails%>">
                        <h2 class="text-lg font-bold"><%=transactionItem.getBook().getTitle()%></h2>
                    </a>

                    <p class="text-gray-600">Author: <%=transactionItem.getBook().getAuthor()%></p>
                </div>
            </div>
            <div class="w-70">
                <p>Quantity Purchased: <%=transactionItem.getQuantity()%></p>
            </div>
            <div class="w-30">
                <p class="text-gray-600">Paid: $<%=String.format("%.2f", transactionItem.getPrice())%></p>
            </div>
            <% if (transactionItem.getReviewed() == 0) { %>
            <form action="<%=request.getContextPath()%>/Review" method="get" id="review_<%=transactionItem.getBook().getBookID()%>">
                <input type="hidden" name="bookID" value="<%=transactionItem.getBook().getBookID()%>">
                <input type="hidden" name="custID" value="<%=transactionHistory.getCustID()%>">
                <input type="hidden" name="scrollPosition" id="scrollPosition_<%=transactionItem.getBook().getBookID()%>">
                <input type="hidden" name="transactionHistoryItemID" value="<%=transactionItem.getTransactionHistoryItemID()%>">
                <button class="bg-slate-500 hover:bg-slate-600 p-2 text-white mr-5"
                        onclick="review('review_<%=transactionItem.getBook().getBookID()%>','scrollPosition_<%=transactionItem.getBook().getBookID()%>')">Review
                </button>
            </form>
            <% } else { %>
            <button class="bg-gray-400 cursor-not-allowed p-2 text-white mr-5" disabled>Reviewed</button>
            <% } %>
        </li>
        <% } %>
    </ul>

    <div class="border border-gray-300"></div>
    <div class="flex items-center justify-end mt-2 mr-8">
        <p class="text-md font-semibold">Subtotal: $<%=String.format("%.2f", subtotal)%></p>
    </div>
    <div class="flex items-center justify-end mt-2 mr-8">
        <p class="text-md font-semibold">GST(8%): $<%=String.format("%.2f", gst)%></p>
    </div>
    <div class="flex items-center justify-end mb-2 mr-8">
        <p class="text-lg font-bold">Total Amount Paid: $<%=String.format("%.2f", totalAmt)%></p>
    </div>
</div>
<%
    }
}
} else {
%>
<script>
    if (<%=validatedUserID%> == null) {
        window.location.href = "registrationPage.jsp";
    } else {
        window.location.href = "<%=request.getContextPath()%>/ProfilePage?userID=" + <%=validatedUserID%>;
    }
</script>
<%
}
%>
<script>
    window.addEventListener('load', () => {
        if (!isNaN(<%=scrollPosition%>)) {
            window.scrollTo(0, <%=scrollPosition%>);
        }
    });

    function goBack() {
        window.location.href = "<%=request.getContextPath()%>/ProfilePage?userID=" + <%=validatedUserID%>;
    }

    function review(formID, scrollURL) {
        let scrollPosition = window.scrollY;
        document.getElementById(scrollURL).value = scrollPosition;
        document.getElementById(formID).submit();
    }
</script>
</body>
</html>
