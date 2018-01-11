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
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="GroupEntity")
public class Group implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

	@NotNull
	@ManyToOne
	@JoinColumn(name = "admin_id", referencedColumnName = "id")
	private User admin;

	@ManyToMany
	@JoinTable(name = "group_user", 
		joinColumns = @JoinColumn(name="group_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
	private List<User> members;

	@OneToMany(cascade=CascadeType.ALL)
	private List<Expense> expenses;

	@OneToMany(cascade=CascadeType.ALL)
	private List<Invitation> invitations;

	@Column(name="description")
	private String description;

	@NotEmpty
	@Column(name="name")
	private String name;

	public Group() {
		this.expenses = new ArrayList<Expense>();
		this.members = new ArrayList<User>();
		this.invitations = new ArrayList<Invitation>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public User getAdmin() {
		return admin;
	}

	public boolean isAdmin(User user) {
		return admin.equals(user);
	}

	public void setAdmin(User admin) {
		this.admin = admin;
	}

	public List<User> getMembers() {
		return members;
	}

	public boolean isMember(User user) {
		return members.contains(user);
	}
	
	public void addMember(User member) {
		this.members.add(member);
	}
	
	public void removeMember(User user) {
		this.members.remove(user);
	}

	public List<Expense> getExpenses() {
		return expenses;
	}
	
	public void addExpense(Expense expense) {
		this.expenses.add(expense);
	}
	
	public void removeExpense(Expense expense) {
		this.expenses.remove(expense);
	}
	
	public List<Invitation> getInvitations() {
		return invitations;
	}
	
	public void addInvitation(Invitation invitation) {
		this.invitations.add(invitation);
	}
	
	public void removeInvitation(Invitation invitation) {
		this.invitations.remove(invitation);
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public boolean equals(Object other) {
		if (other == null) {
			return false;
		}
		if (other == this) {
			return true;
		}
		if (!(other instanceof Group)) {
			return false;
		}
		Group otherGroup = (Group)other;
		return otherGroup.getId() == getId();
	}
}
