package model;

public class TransactionHistory {
	private String transactionHistoryID;
	private String transactionDate;
	private double totalAmount;
	private String customerID;
	private String addressID;
	private String paymentInpaymentIntentID;
	private double gstPercentage;
	private String fullAddress;
	
	public TransactionHistory() {
	}

	public String getTransactionHistoryID() {
		return transactionHistoryID;
	}

	public void setTransactionHistoryID(String transactionHistoryID) {
		this.transactionHistoryID = transactionHistoryID;
	}

	public String getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getAddressID() {
		return addressID;
	}

	public void setAddressID(String addressID) {
		this.addressID = addressID;
	}

	public String getPaymentInpaymentIntentID() {
		return paymentInpaymentIntentID;
	}

	public void setPaymentInpaymentIntentID(String paymentInpaymentIntentID) {
		this.paymentInpaymentIntentID = paymentInpaymentIntentID;
	}

	public double getGstPercentage() {
		return gstPercentage;
	}

	public void setGstPercentage(double gstPercentage) {
		this.gstPercentage = gstPercentage;
	}

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}
	
	
	
}
