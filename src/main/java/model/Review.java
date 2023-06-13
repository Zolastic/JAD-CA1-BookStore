package model;

public class Review {
	private Book book;
	private String review_id;
	private String custID;
	private String bookID;
	private String review_text;
	private double rating;
	private String ratingDate;
	public Review(Book book, String review_id, String custID, String bookID, String review_text, double rating,
			String ratingDate) {
		super();
		this.book = book;
		this.review_id = review_id;
		this.custID = custID;
		this.bookID = bookID;
		this.review_text = review_text;
		this.rating = rating;
		this.ratingDate = ratingDate;
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
