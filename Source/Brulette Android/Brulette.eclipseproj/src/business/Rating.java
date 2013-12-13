package business;

import java.util.Date;

public class Rating {
	
	private String comment;
	private int id;
	private int userId;
	private int roundId;
	private int stars;
	private Date created;
	private Date updated;
	
	public Rating(String comment, int id, int userId, int roundId, int stars, Date created, Date updated) {
		this.setComment(comment);
		this.setId(id);
		this.setUserId(userId);
		this.setRoundId(roundId);
		this.setStars(stars);
		this.setCreated(created);
		this.setUpdated(updated);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getRoundId() {
		return roundId;
	}

	public void setRoundId(int roundId) {
		this.roundId = roundId;
	}

	public int getStars() {
		return stars;
	}

	public void setStars(int stars) {
		this.stars = stars;
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
