package com.boot.cut_costs.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

	/*
	 * Nickname
	 */
	@Column(name = "name")
	private String name;

	@Size(min=15, max=15)
	@Column(name="image_id")
	private String imageId;
	
	@Size(max=100)
	@Column(name="description")
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="admin")
	private Set<Group> ownedGroups;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<Group> memberGroups;
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, mappedBy = "owner")
	private Set<Expense> ownedExpenses;

	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<Expense> receivedExpenses;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="inviter")
	private Set<Invitation> ownedInvitations;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="invitee")
	private Set<Invitation> receivedInvitations;

	public User() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Invitation> getOwnedInvitations() {
		return ownedInvitations;
	}

	public void setOwnedInvitations(Set<Invitation> ownedInvitations) {
		this.ownedInvitations = ownedInvitations;
	}
	
	public void addOwnedInvitation(Invitation inv) {
		this.ownedInvitations.add(inv);
	}

	public Set<Invitation> getReceivedInvitations() {
		return receivedInvitations;
	}

	public void setReceivedInvitations(Set<Invitation> receivedInvitations) {
		this.receivedInvitations = receivedInvitations;
	}
	
	public void addReceivedInvitation(Invitation invitation) {
		this.receivedInvitations.add(invitation);
	}
	
	public Set<Expense> getOwnedExpenses() {
		return ownedExpenses;
	}

	public void addOwnedExpense(Expense expense){
		this.ownedExpenses.add(expense);
	}
	
	public Set<Group> getOwnedGroups() {
		return ownedGroups;
	}

	public void addOwnedGroup(Group group){
		this.ownedGroups.add(group);
	}

	public Set<Group> getMemberGroups() {
		return memberGroups;
	}

	public void addMemberGroup(Group group) {
		this.memberGroups.add(group);
	}
	
	public Set<Expense> getReceivedExpenses() {
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

	public void setOwnedGroups(Set<Group> ownedGroups) {
		this.ownedGroups = ownedGroups;
	}

	public void setMemberGroups(Set<Group> memberGroups) {
		this.memberGroups = memberGroups;
	}

	public void setOwnedExpenses(Set<Expense> ownedExpenses) {
		this.ownedExpenses = ownedExpenses;
	}

	public void setReceivedExpenses(Set<Expense> receivedExpenses) {
		this.receivedExpenses = receivedExpenses;
	}

}
