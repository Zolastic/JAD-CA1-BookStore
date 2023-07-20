package model;

public class OverallSalesReport {
private double totalEarningWithGST;
private double totalEarningWithoutGST;
private double gst;
private int totalTransactionOrders;
private int totalBooksSold;
private String transactionDate;
public OverallSalesReport(double totalEarningWithGST, double totalEarningWithoutGST, double gst,
		int totalTransactionOrders, int totalBooksSold, String transactionDate) {
	super();
	this.totalEarningWithGST = totalEarningWithGST;
	this.totalEarningWithoutGST = totalEarningWithoutGST;
	this.gst = gst;
	this.totalTransactionOrders = totalTransactionOrders;
	this.totalBooksSold = totalBooksSold;
	this.transactionDate=transactionDate;
}

public String getTransactionDate() {
	return transactionDate;
}
public void setTransactionDate(String transactionDate) {
	this.transactionDate = transactionDate;
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
public double getGst() {
	return gst;
}
public void setGst(double gst) {
	this.gst = gst;
}
public int getTotalTransactionOrders() {
	return totalTransactionOrders;
}
public void setTotalTransactionOrders(int totalTransactionOrders) {
	this.totalTransactionOrders = totalTransactionOrders;
}
public int getTotalBooksSold() {
	return totalBooksSold;
}
public void setTotalBooksSold(int totalBooksSold) {
	this.totalBooksSold = totalBooksSold;
}

}
