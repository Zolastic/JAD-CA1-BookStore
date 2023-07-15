package model;
import java.util.*;

/**
 * Author(s): Soh Jian Min (P2238856)
 * Description: JAD CA1
 */

public class TransactionHistory {
    private String transactionHistoryID;
    private String transactionDate;
    private double totalAmount;
    private String custID;
    private String addr_id;
    private String paymentIntentId;
    private List<TransactionHistoryItem> transactionHistoryItems;

    public TransactionHistory(String transactionHistoryID, String transactionDate, double totalAmount, String custID, List<TransactionHistoryItem> transactionHistoryItems, String addr_id, String paymentIntentId) {
        this.transactionHistoryID = transactionHistoryID;
        this.transactionDate = transactionDate;
        this.totalAmount = totalAmount;
        this.custID = custID;
        this.addr_id=addr_id;
        this.transactionHistoryItems = transactionHistoryItems;
        this.paymentIntentId=paymentIntentId;
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

	public String getAddrId() {
		return addr_id;
	}

	public void setAddrId(String addrId) {
		this.addr_id = addrId;
	}

	public List<TransactionHistoryItem> getTransactionHistoryItems() {
		return transactionHistoryItems;
	}

	public void setTransactionHistoryItems(List<TransactionHistoryItem> transactionHistoryItems) {
		this.transactionHistoryItems = transactionHistoryItems;
	}

   
}
