package model;

public class User {
	private String userID;
	private String name;
	private String email;
	private String password;
	private String role;
	private String image;
	private String secret;
	
	public User(String userID, String name, String email, String role, String image) {
		super();
		this.userID = userID;
		this.name = name;
		this.email = email;
		this.password = "";
		this.role = role;
		this.image = image;
		this.secret = "";
	}
	
	public User(String userID, String name, String email, String password, String role, String image, String secret) {
		super();
		this.userID = userID;
		this.name = name;
		this.email = email;
		this.password = password;
		this.role = role;
		this.image = image;
		this.secret = secret;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}
	
	
}
