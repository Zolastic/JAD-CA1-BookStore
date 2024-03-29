package model;

public class Genre {
	private String id;
	private String name;
	private String img;
	private String imgPublicID;
	
	public Genre(String id, String name, String img) {
		this.id = id;
		this.name = name;
		this.img=img;
	}
	
	public Genre(String id, String name, String img, String imgPublicID) {
		super();
		this.id = id;
		this.name = name;
		this.img = img;
		this.imgPublicID = imgPublicID;
	}



	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
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
	public String getImgPublicID() {
		return imgPublicID;
	}
	public void setImgPublicID(String imgPublicID) {
		this.imgPublicID = imgPublicID;
	}
	
	
	
}
