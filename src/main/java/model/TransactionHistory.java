package model;
import java.util.*;

/**
 * Author(s): Soh Jian Min (P2238856)
 * Description: JAD CA1
 */

public class TransactionHistory {
    private String transactionHistoryID;
    private String transactionDate;
    private double subtotal;
    private String custID;
    private String address;
    private List<TransactionHistoryItem> transactionHistoryItems;

    public TransactionHistory(String transactionHistoryID, String transactionDate, double subtotal, String custID, String address, List<TransactionHistoryItem> transactionHistoryItems) {
        this.transactionHistoryID = transactionHistoryID;
        this.transactionDate = transactionDate;
        this.subtotal = subtotal;
        this.custID = custID;
        this.address = address;
        this.transactionHistoryItems = transactionHistoryItems;
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

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public String getCustID() {
		return custID;
	}

	public void setCustID(String custID) {
		this.custID = custID;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public List<TransactionHistoryItem> getTransactionHistoryItems() {
		return transactionHistoryItems;
	}

	public void setTransactionHistoryItems(List<TransactionHistoryItem> transactionHistoryItems) {
		this.transactionHistoryItems = transactionHistoryItems;
	}

   
}
