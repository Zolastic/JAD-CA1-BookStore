package model;

import java.util.List;

public class CustomerListByBooks {
	private User userDetails;
	private List<String> transactionDates;
	private List<Integer> quantityPurchased;

	public CustomerListByBooks(User userDetails, List<String> transactionDates, List<Integer> quantityPurchased) {
		super();
		this.userDetails = userDetails;
		this.transactionDates = transactionDates;
	}

	public User getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(User userDetails) {
		this.userDetails = userDetails;
	}

	public List<String> getTransactionDates() {
		return transactionDates;
	}

	public void setTransactionDates(List<String> transactionDates) {
		this.transactionDates = transactionDates;
	}

	public List<Integer> getQuantityPurchased() {
		return quantityPurchased;
	}

	public void setQuantityPurchased(List<Integer> quantityPurchased) {
		this.quantityPurchased = quantityPurchased;
	}

}
