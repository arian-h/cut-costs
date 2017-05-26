package com.boot.cut_costs.model;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="CCInvitation")
public class CCInvitation implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;
	
	@NotNull
	@JoinColumn(name="inviter", referencedColumnName="id")
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	private CCUser inviter;
	
	@NotNull
	@JoinColumn(name="invitee", referencedColumnName="id")
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	private CCUser invitee;
	
	//@Size(max=30)
	@Column(name="description")
	private String description;
	
	@NotNull
	@JoinColumn(name="group_id", referencedColumnName = "id")
	@ManyToOne(cascade=CascadeType.REFRESH, fetch=FetchType.EAGER)
	private CCGroup group;
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public CCUser getInviter() {
		return inviter;
	}
	
	public void setInviter(CCUser inviter) {
		this.inviter = inviter;
	}
	
	public CCUser getInvitee() {
		return invitee;
	}
	
	public void setInvitee(CCUser invitee) {
		this.invitee = invitee;
	}
	
	public CCGroup getGroup() {
		return group;
	}
	
	public void setGroup(CCGroup group) {
		this.group = group;
	}
	
}
