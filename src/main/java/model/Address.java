package model;

public class Address {
	private String addrId;
	private String unit_number;
	private String block_number;
	private String street_address;
	private String postal_code;
	private String country;

	public Address(String addrId, String unit_number, String block_number, String street_address, String postal_code,
			String country) {
		super();
		this.addrId = addrId;
		this.unit_number = unit_number;
		this.block_number = block_number;
		this.street_address = street_address;
		this.postal_code = postal_code;
		this.country = country;
	}

	public String getAddrId() {
		return addrId;
	}

	public void setAddrId(String addrId) {
		this.addrId = addrId;
	}

	public String getUnit_number() {
		return unit_number;
	}

	public void setUnit_number(String unit_number) {
		this.unit_number = unit_number;
	}

	public String getBlock_number() {
		return block_number;
	}

	public void setBlock_number(String block_number) {
		this.block_number = block_number;
	}

	public String getStreet_address() {
		return street_address;
	}

	public void setStreet_address(String street_address) {
		this.street_address = street_address;
	}

	public String getPostal_code() {
		return postal_code;
	}

	public void setPostal_code(String postal_code) {
		this.postal_code = postal_code;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

}
