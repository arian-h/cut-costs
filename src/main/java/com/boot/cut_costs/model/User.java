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

	@OneToMany(cascade = CascadeType.ALL, mappedBy="owner")
	private List<Expense> ownedExpenses;

	@ManyToMany(cascade = CascadeType.REFRESH)
	private List<Expense> receivedExpenses;

	@OneToMany(cascade=CascadeType.ALL)
	private List<Invitation> ownedInvitations;

	@OneToMany(cascade=CascadeType.ALL)
	private List<Invitation> receivedInvitations;

	public User() {
		this.ownedGroups = new ArrayList<Group>();
		this.memberGroups = new ArrayList<Group>();
		this.ownedInvitations = new ArrayList<Invitation>();
		this.receivedInvitations = new ArrayList<Invitation>();
		this.ownedExpenses = new ArrayList<Expense>();
		this.receivedExpenses = new ArrayList<Expense>();
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
	
	public List<Expense> getOwnedExpenses() {
		return ownedExpenses;
	}

	public void addOwnedExpense(Expense expense){
		this.ownedExpenses.add(expense);
	}
	
	public List<Group> getOwnedGroups() {
		return ownedGroups;
	}

	public void addOwnedGroup(Group group){
		this.ownedGroups.add(group);
	}

	public List<Group> getMemberGroups() {
		return memberGroups;
	}

	public void addMemberGroup(Group group) {
		this.memberGroups.add(group);
	}
	
	public List<Expense> getReceivedExpenses() {
		return receivedExpenses;
	}

	public void addReceivedExpense(Expense expense) {
		this.receivedExpenses.add(expense);
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

	public void setOwnedExpenses(List<Expense> ownedExpenses) {
		this.ownedExpenses = ownedExpenses;
	}

	public void setReceivedExpenses(List<Expense> receivedExpenses) {
		this.receivedExpenses = receivedExpenses;
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
