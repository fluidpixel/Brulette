package business;

import java.util.List;

public class BrewTags {
	
	private int id;
	private int userId;
	private String name;
	private List<String> tags;
	
	
	public BrewTags(int id, int userId, String name, List<String> tags){
		this.userId = userId;
		this.tags = tags;
		this.name = name;
		this.id = id;
	}
	
	public int getId(){
		return id;
	}
	
	public int getUserId(){
		return userId;
	}
	
	public List<String> getTags(){
		return tags;
	}
	
	public String getName(){
		return name;
	}

}
