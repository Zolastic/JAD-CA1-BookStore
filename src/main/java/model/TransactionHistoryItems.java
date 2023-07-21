package model;

public class TransactionHistoryItems {
	private String transactionHistoryItemID;
	private String transactionHistoryID;
	private String bookID;
	private int quantity;
	private int reviewed;
	
	public TransactionHistoryItems() {
	}

	public String getTransactionHistoryItemID() {
		return transactionHistoryItemID;
	}

	public void setTransactionHistoryItemID(String transactionHistoryItemID) {
		this.transactionHistoryItemID = transactionHistoryItemID;
	}

	public String getTransactionHistoryID() {
		return transactionHistoryID;
	}

	public void setTransactionHistoryID(String transactionHistoryID) {
		this.transactionHistoryID = transactionHistoryID;
	}

	public String getBookID() {
		return bookID;
	}

	public void setBookID(String bookID) {
		this.bookID = bookID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getReviewed() {
		return reviewed;
	}

	public void setReviewed(int reviewed) {
		this.reviewed = reviewed;
	}
	
	
}
