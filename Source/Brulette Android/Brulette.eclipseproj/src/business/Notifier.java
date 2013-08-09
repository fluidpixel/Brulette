package business;

import java.util.Date;

public class Notifier {
	
	private boolean active;
	private Date created;
	private int id;
	private String identifier;
	private String provider;
	private Date updated;
	private int userId;
	
	public Notifier(boolean active, Date created, int id, String identifier, String provider, Date updated, int userId) {
		this.setActive(active);
		this.setCreated(created);
		this.setId(id);
		this.setIdentifier(identifier);
		this.setProvider(provider);
		this.setUpdated(updated);
		this.setUserId(userId);
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
