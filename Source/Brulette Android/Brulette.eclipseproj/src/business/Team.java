package business;

import java.util.Date;


public class Team {
	
	private int id;
	private String name;
	private String password;
	private String slug;
	private Long latitude;
	private Long longitude;
	private Date created;
	private Date updated;
	
	public Team(int id, String name, String password, String slug, Long latitude, Long longitude, Date created, Date updated) {
		this.setId(id);
		this.setName(name);
		this.setPassword(password);
		this.setSlug(slug);
		this.setCreated(created);
		this.setUpdated(updated);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSlug() {
		return slug;
	}

	public void setSlug(String slug) {
		this.slug = slug;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public Long getLatitude() {
		return latitude;
	}

	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}

	public Long getLongitude() {
		return longitude;
	}

	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}

}
