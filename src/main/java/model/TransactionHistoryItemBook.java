package model;


public class TransactionHistoryItemBook{
    private String transactionHistoryItemID;
    private String bookID;
    private int quantity;
    private int reviewed;
    private Book book;
    private double price;
    

    public TransactionHistoryItemBook() {
		super();
	}

	public TransactionHistoryItemBook(String transactionHistoryItemID, String bookID, int quantity, int reviewed, double price) {
        this.transactionHistoryItemID = transactionHistoryItemID;
        this.bookID = bookID;
        this.quantity = quantity;
        this.reviewed = reviewed;
        this.price =price;
    }
	
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getTransactionHistoryItemID() {
		return transactionHistoryItemID;
	}

	public void setTransactionHistoryItemID(String transactionHistoryItemID) {
		this.transactionHistoryItemID = transactionHistoryItemID;
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

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

}
