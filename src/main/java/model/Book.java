package model;

public class Book {
	private int BookID;
	private String ISBN;
	private String Title;
	private String Author;
	private String Publisher;
	private String Publication_date; 
	private String Description;
	private int GenreID;
	private String Img;
	private int Sold;
	private int Inventory;
	private double Price;
	
	public Book(int bookID, String iSBN, String title, String author, String publisher, String publication_date, String description,
			int genreID, String img, int sold, int inventory, double price) {
		super();
		ISBN = iSBN;
		Title = title;
		Author = author;
		Publisher = publisher;
		Publication_date = publication_date;
		Description = description;
		GenreID = genreID;
		Img = img;
		Sold = sold;
		Inventory = inventory;
		Price = price;
		BookID=bookID;
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

	public int getGenreID() {
		return GenreID;
	}

	public void setGenreID(int genreID) {
		GenreID = genreID;
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
	
	
	

}