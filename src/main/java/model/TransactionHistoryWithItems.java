package model;
import java.util.*;


public class TransactionHistoryWithItems {
    private String transactionHistoryID;
    private String transactionDate;
    private double totalAmount;
    private String custID;
    private String addr_id;
    private String paymentIntentId;
    private List<TransactionHistoryItemBook> transactionHistoryItems;
    private double gstPercent;
    private String fullAddr;

    public TransactionHistoryWithItems(String transactionHistoryID, String transactionDate, double totalAmount, String custID, List<TransactionHistoryItemBook> transactionHistoryItems, String addr_id, String paymentIntentId, double gstPercent, String fullAddr) {
        this.transactionHistoryID = transactionHistoryID;
        this.transactionDate = transactionDate;
        this.totalAmount = totalAmount;
        this.custID = custID;
        this.addr_id=addr_id;
        this.transactionHistoryItems = transactionHistoryItems;
        this.paymentIntentId=paymentIntentId;
        this.gstPercent=gstPercent;
        this.fullAddr=fullAddr;
    }


	public String getFullAddr() {
		return fullAddr;
	}


	public void setFullAddr(String fullAddr) {
		this.fullAddr = fullAddr;
	}


	public String getAddr_id() {
		return addr_id;
	}

	public void setAddr_id(String addr_id) {
		this.addr_id = addr_id;
	}

	public double getGstPercent() {
		return gstPercent;
	}

	public void setGstPercent(double gstPercent) {
		this.gstPercent = gstPercent;
	}

	public String getPaymentIntentId() {
		return paymentIntentId;
	}

	public void setPaymentIntentId(String paymentIntentId) {
		this.paymentIntentId = paymentIntentId;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
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

	public String getCustID() {
		return custID;
	}

	public void setCustID(String custID) {
		this.custID = custID;
	}


	public List<TransactionHistoryItemBook> getTransactionHistoryItems() {
		return transactionHistoryItems;
	}

	public void setTransactionHistoryItems(List<TransactionHistoryItemBook> transactionHistoryItems) {
		this.transactionHistoryItems = transactionHistoryItems;
	}

   
}
