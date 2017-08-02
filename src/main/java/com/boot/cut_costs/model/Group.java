package com.boot.cut_costs.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

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
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;


@Entity
@Table(name="\"Group\"") // Group is a reserved word, use \" as a work-around
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
	@JoinTable(name = "Group_User", 
		joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
	private Set<User> members;

	@OneToMany(cascade=CascadeType.ALL)
	private Set<Expense> expenses;
	
	@Column(name="description")
	private String description;

	@NotEmpty
	@Column(name="name")
	private String name;
	
	@Size(min=15, max=15)
	@Column(name="image_id")
	private String imageId;
	
	public Group() {
		this.expenses = new HashSet<Expense>();
		this.members = new HashSet<User>();
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

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	public String getImageId() {
		return imageId;
	}
	
	public Set<User> getMembers() {
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

	public Set<Expense> getExpenses() {
		return expenses;
	}
	
	public void addExpense(Expense expense) {
		this.expenses.add(expense);
	}
	
	public void removeExpense(Expense expense) {
		this.expenses.remove(expense);
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
