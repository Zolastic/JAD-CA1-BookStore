package model;

public class Book {
	private int BookID;
	private String ISBN;
	private String Title;
	private String Author;
	private String Publisher;
	private String Publication_date; 
	private String Description;
	private String GenreName;
	private String Img;
	private int Sold;
	private int Inventory;
	private double Price;
	private double Rating;
	public Book(int bookID, String iSBN, String title, String author, String publisher, String publication_date,
			String description, String genreName, String img, int sold, int inventory, double price, double rating) {
		super();
		BookID = bookID;
		ISBN = iSBN;
		Title = title;
		Author = author;
		Publisher = publisher;
		Publication_date = publication_date;
		Description = description;
		GenreName = genreName;
		Img = img;
		Sold = sold;
		Inventory = inventory;
		Price = price;
		Rating = rating;
	}
	public int getBookID() {
		return BookID;
	}
	public void setBookID(int bookID) {
		BookID = bookID;
	}
	public String getISBN() {
		return ISBN;
	}
	public void setISBN(String iSBN) {
		ISBN = iSBN;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getAuthor() {
		return Author;
	}
	public void setAuthor(String author) {
		Author = author;
	}
	public String getPublisher() {
		return Publisher;
	}
	public void setPublisher(String publisher) {
		Publisher = publisher;
	}
	public String getPublication_date() {
		return Publication_date;
	}
	public void setPublication_date(String publication_date) {
		Publication_date = publication_date;
	}
	public String getDescription() {
		return Description;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getGenreName() {
		return GenreName;
	}
	public void setGenreName(String genreName) {
		GenreName = genreName;
	}
	public String getImg() {
		return Img;
	}
	public void setImg(String img) {
		Img = img;
	}
	public int getSold() {
		return Sold;
	}
	public void setSold(int sold) {
		Sold = sold;
	}
	public int getInventory() {
		return Inventory;
	}
	public void setInventory(int inventory) {
		Inventory = inventory;
	}
	public double getPrice() {
		return Price;
	}
	public void setPrice(double price) {
		Price = price;
	}
	public double getRating() {
		return Rating;
	}
	public void setRating(double rating) {
		Rating = rating;
	}
	
	
}