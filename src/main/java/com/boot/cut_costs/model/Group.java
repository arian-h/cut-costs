package com.boot.cut_costs.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="\"Group\"") // Group is a reserved word, use \" as a work-around
public class Group implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

	@NotNull
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="admin_id", referencedColumnName="id")
	private User admin;

	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "Group_User", 
		joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
	private Set<User> members;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
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
	
	public void addMember(User member) {
		this.members.add(member);
	}

	public Set<Expense> getExpenses() {
		return expenses;
	}
	
	public void addExpense(Expense expense) {
		this.expenses.add(expense);
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

}
