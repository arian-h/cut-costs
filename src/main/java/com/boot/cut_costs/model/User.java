package com.boot.cut_costs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="User")
public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
	private long id;

	@Column(name = "name")
	private String name;

	@Size(min=15, max=15)
	@Column(name="image_id")
	private String imageId;

	@Size(max=100)
	@Column(name="description")
	private String description;

	@OneToMany(cascade = CascadeType.ALL, mappedBy="admin")
	private List<Group> ownedGroups;

	@ManyToMany(cascade = CascadeType.REFRESH, mappedBy="members")
	private List<Group> memberGroups;

	@ManyToMany(cascade = CascadeType.REFRESH)
	private List<Expense> expenses;

	@OneToMany(cascade=CascadeType.ALL)
	private List<Invitation> ownedInvitations;

	@OneToMany(cascade=CascadeType.ALL)
	private List<Invitation> receivedInvitations;

	public User() {
		this.ownedGroups = new ArrayList<Group>();
		this.memberGroups = new ArrayList<Group>();
		this.ownedInvitations = new ArrayList<Invitation>();
		this.receivedInvitations = new ArrayList<Invitation>();
		this.expenses = new ArrayList<Expense>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Invitation> getOwnedInvitations() {
		return ownedInvitations;
	}

	public void setOwnedInvitations(List<Invitation> ownedInvitations) {
		this.ownedInvitations = ownedInvitations;
	}
	
	public void addOwnedInvitation(Invitation inv) {
		this.ownedInvitations.add(inv);
	}

	public List<Invitation> getReceivedInvitations() {
		return receivedInvitations;
	}

	public void setReceivedInvitations(List<Invitation> receivedInvitations) {
		this.receivedInvitations = receivedInvitations;
	}
	
	public void addReceivedInvitation(Invitation invitation) {
		this.receivedInvitations.add(invitation);
	}
	
	public List<Expense> getExpenses() {
		return expenses;
	}

	public void removeExpense(Expense expense) {
		this.expenses.remove(expense);
	}

	public void addExpense(Expense expense){
		this.expenses.add(expense);
	}
	
	public List<Group> getOwnedGroups() {
		return ownedGroups;
	}

	public void addOwnedGroup(Group group){
		this.ownedGroups.add(group);
	}
	
	public void removeOwnedGroup(Group group) {
		this.ownedGroups.remove(group);
	}

	public List<Group> getMemberGroups() {
		return memberGroups;
	}

	public void addMemberGroup(Group group) {
		this.memberGroups.add(group);
	}
	
	public void removeMemberGroup(Group group) {
		this.memberGroups.remove(group);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getImageId() {
		return imageId;
	}
	
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public void setOwnedGroups(List<Group> ownedGroups) {
		this.ownedGroups = ownedGroups;
	}

	public void setMemberGroups(List<Group> memberGroups) {
		this.memberGroups = memberGroups;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (!(other instanceof User)) {
			return false;
		}
		User otherUser = (User)other;
		return otherUser.getId() == getId();
	}

}
