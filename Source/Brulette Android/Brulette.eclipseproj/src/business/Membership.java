package business;

import java.util.Date;

public class Membership {
	
	private int id;
	private int team_id;
	private int user_id;
	private String team_name;
	private String user_name;
	private boolean active;
	private boolean block_by_team;
	private boolean block_by_user;
	private Date created;
	private Date updated;
	
	public Membership(int id, int team_id, int user_id, String team_name, String user_name, boolean active, boolean block_by_team, boolean block_by_user, Date created, Date updated ){
		this.setId(id);
		this.setTeam_id(team_id);
		this.setUser_id(user_id);
		this.setTeam_name(team_name);
		this.setUser_name(user_name);
		this.setActive(active);
		this.setBlock_by_team(block_by_team);
		this.setBlock_by_user(block_by_user);
		this.setCreated(created);
		this.setUpdated(updated);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTeam_id() {
		return team_id;
	}

	public void setTeam_id(int team_id) {
		this.team_id = team_id;
	}

	public int getUser_id() {
		return user_id;
	}

	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	public String getTeam_name() {
		return team_name;
	}

	public void setTeam_name(String team_name) {
		this.team_name = team_name;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isBlock_by_team() {
		return block_by_team;
	}

	public void setBlock_by_team(boolean block_by_team) {
		this.block_by_team = block_by_team;
	}

	public boolean isBlock_by_user() {
		return block_by_user;
	}

	public void setBlock_by_user(boolean block_by_user) {
		this.block_by_user = block_by_user;
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
