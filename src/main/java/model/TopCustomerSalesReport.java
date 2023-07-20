package model;

public class TopCustomerSalesReport {
	private User userDetails;
	private double totalSpendwithGST;
	private double totalSpendwithoutGST;
	private int totalBooksBought;
	private int totalOrderMade;
	private double gst;
	public TopCustomerSalesReport(User userDetails, double totalSpendwithGST, double totalSpendwithoutGST,
			int totalBooksBought, int totalOrderMade, double gst) {
		super();
		this.userDetails = userDetails;
		this.totalSpendwithGST = totalSpendwithGST;
		this.totalSpendwithoutGST = totalSpendwithoutGST;
		this.totalBooksBought = totalBooksBought;
		this.totalOrderMade = totalOrderMade;
		this.gst = gst;
	}
	public User getUserDetails() {
		return userDetails;
	}
	public void setUserDetails(User userDetails) {
		this.userDetails = userDetails;
	}
	public double getTotalSpendwithGST() {
		return totalSpendwithGST;
	}
	public void setTotalSpendwithGST(double totalSpendwithGST) {
		this.totalSpendwithGST = totalSpendwithGST;
	}
	public double getTotalSpendwithoutGST() {
		return totalSpendwithoutGST;
	}
	public void setTotalSpendwithoutGST(double totalSpendwithoutGST) {
		this.totalSpendwithoutGST = totalSpendwithoutGST;
	}
	public int getTotalBooksBought() {
		return totalBooksBought;
	}
	public void setTotalBooksBought(int totalBooksBought) {
		this.totalBooksBought = totalBooksBought;
	}
	public int getTotalOrderMade() {
		return totalOrderMade;
	}
	public void setTotalOrderMade(int totalOrderMade) {
		this.totalOrderMade = totalOrderMade;
	}
	public double getGst() {
		return gst;
	}
	public void setGst(double gst) {
		this.gst = gst;
	}
	
}
