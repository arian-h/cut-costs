package com.boot.cut_costs.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="Invitation")
public class Invitation implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@JsonIgnore
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private long id;

	@NotNull
	@JoinColumn(name = "inviter_id", referencedColumnName = "id")
	@ManyToOne
	private User inviter;

	@NotNull
	@JoinColumn(name="invitee_id", referencedColumnName="id")
	@ManyToOne
	private User invitee;
	
	@Column(name="description")
	private String description;
	
	@NotNull
	@JoinColumn(name = "group_id", referencedColumnName = "id")
	@ManyToOne
	private Group group;

	public Invitation() {}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public User getInviter() {
		return inviter;
	}
	
	public void setInviter(User inviter) {
		this.inviter = inviter;
	}
	
	public User getInvitee() {
		return invitee;
	}
	
	public void setInvitee(User invitee) {
		this.invitee = invitee;
	}
	
	public Group getGroup() {
		return group;
	}
	
	public void setGroup(Group group) {
		this.group = group;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

}
