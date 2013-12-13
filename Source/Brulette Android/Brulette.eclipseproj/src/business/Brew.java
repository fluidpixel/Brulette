package business;

import java.util.Date;

public class Brew {
	
	private String name;
	private BrewCategory category;
	private int id;
	private int popularity;
	private Date created;
	private Date updated;
	
	public Brew(String name, BrewCategory category){
		this.setName(name);
		this.setCategory(category);
	}
	
	public Brew(String name, BrewCategory category, int id, int popularity, Date created, Date updated){
		this.name = name;
		this.category = category;
		this.id = id;
		this.popularity = popularity;
		this.created = created;
		this.updated = updated;
	}

	public String getTag() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BrewCategory getCategorie() {
		return category;
	}

	public void setCategory(BrewCategory category) {
		this.category = category;
	}

	public int getPopularity() {
		return popularity;
	}

	public void setPopularity(int popularity) {
		this.popularity = popularity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	

}
