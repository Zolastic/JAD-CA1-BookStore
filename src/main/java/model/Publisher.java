package model;

public class Publisher {
	private String id;
	private String name;

	public Publisher(String publisherID, String name) {
		this.id = publisherID;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
