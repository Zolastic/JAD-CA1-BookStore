package model;

/**
 * Author(s): Soh Jian Min (P2238856)
 * Description: JAD CA1
 */

public class ReviewHistoryClass {
	private Book book;
	private String review_id;
	private String custID;
	private String bookID;
	private String review_text;
	private double rating;
	private String ratingDate;
	private String transaction_history_itemID;
	public ReviewHistoryClass(Book book, String review_id, String custID, String bookID, String review_text, double rating,
			String ratingDate, String transaction_history_itemID) {
		super();
		this.book = book;
		this.review_id = review_id;
		this.custID = custID;
		this.bookID = bookID;
		this.review_text = review_text;
		this.rating = rating;
		this.ratingDate = ratingDate;
		this.transaction_history_itemID=transaction_history_itemID;
	}
	public String getTransaction_history_itemID() {
		return transaction_history_itemID;
	}
	public void setTransaction_history_itemID(String transaction_history_itemID) {
		this.transaction_history_itemID = transaction_history_itemID;
	}
	public Book getBook() {
		return book;
	}
	public void setBook(Book book) {
		this.book = book;
	}
	public String getReview_id() {
		return review_id;
	}
	public void setReview_id(String review_id) {
		this.review_id = review_id;
	}
	public String getCustID() {
		return custID;
	}
	public void setCustID(String custID) {
		this.custID = custID;
	}
	public String getBookID() {
		return bookID;
	}
	public void setBookID(String bookID) {
		this.bookID = bookID;
	}
	public String getReview_text() {
		return review_text;
	}
	public void setReview_text(String review_text) {
		this.review_text = review_text;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public String getRatingDate() {
		return ratingDate;
	}
	public void setRatingDate(String ratingDate) {
		this.ratingDate = ratingDate;
	}


}
