package business;

import java.util.Date;

public class Round {

	private int id;
	private int karma;
	private int idTeam;
	private int time;
	private int userId;
	private Date created;
	private Date closes;
	private RoundStatus status;

	public Round(int id, int karma, int idTeam, int time, int userId, Date created, Date closed, RoundStatus status ){
		this.setId(id);
		this.setKarma(karma);
		this.setIdTeam(idTeam);
		this.setUserId(userId);
		this.setCreated(created);
		this.setCloses(closed);
		this.setStatus(status);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getKarma() {
		return karma;
	}

	public void setKarma(int karma) {
		this.karma = karma;
	}

	public int getIdTeam() {
		return idTeam;
	}

	public void setIdTeam(int idTeam) {
		this.idTeam = idTeam;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getCloses() {
		return closes;
	}

	public void setCloses(Date closes) {
		this.closes = closes;
	}

	public RoundStatus getStatus() {
		return status;
	}

	public void setStatus(RoundStatus status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object obj){
		if(this.id == ((Round)obj).getId() && this.status == ((Round)obj).getStatus())
			return true;
		else
			return false;
	}

}
