package model;

public class BookReport {
	private Book bookDetails;
	private int qtySold;
	private double totalEarningWithGST;
	private double totalEarningWithoutGST;
	private double gstPercent;

	public BookReport(Book bookDetails, int qtySold, double totalEarningWithGST, double totalEarningWithoutGST,
			double gstPercent) {
		super();
		this.bookDetails = bookDetails;
		this.qtySold = qtySold;
		this.totalEarningWithGST = totalEarningWithGST;
		this.totalEarningWithoutGST = totalEarningWithoutGST;
		this.gstPercent = gstPercent;
	}

	public Book getBookDetails() {
		return bookDetails;
	}

	public void setBookDetails(Book bookDetails) {
		this.bookDetails = bookDetails;
	}

	public int getQtySold() {
		return qtySold;
	}

	public void setQtySold(int qtySold) {
		this.qtySold = qtySold;
	}

	public double getTotalEarningWithGST() {
		return totalEarningWithGST;
	}

	public void setTotalEarningWithGST(double totalEarningWithGST) {
		this.totalEarningWithGST = totalEarningWithGST;
	}

	public double getTotalEarningWithoutGST() {
		return totalEarningWithoutGST;
	}

	public void setTotalEarningWithoutGST(double totalEarningWithoutGST) {
		this.totalEarningWithoutGST = totalEarningWithoutGST;
	}

	public double getGstPercent() {
		return gstPercent;
	}

	public void setGstPercent(double gstPercent) {
		this.gstPercent = gstPercent;
	}

}
