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
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="ExpenseEntity")
public class Expense implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

	@Size(max=100)
	private String description;

	@Size(min=5, max=25)
	private String title;

	@Min(value=0L)
	private long amount;

	@Size(min=15, max=15)
	private String imageId;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@NotNull
	@JoinColumn(name="owner_id", referencedColumnName="id")
	private User owner;

	@ManyToOne(cascade = CascadeType.REFRESH)
	@NotNull
	@JoinColumn(referencedColumnName="id")
	private Group group;

	@ManyToMany
	@JoinTable(name = "expense_sharer", joinColumns = @JoinColumn(name = "expense_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "sharer_id", referencedColumnName = "id"))
	private List<User> sharers;

	public Expense() {
		this.sharers = new ArrayList<User>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getImageId() {
		return imageId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getAmount() {
		return amount;
	}

	public void setAmount(long amount) {
		this.amount = amount;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public List<User> getSharers() {
		return sharers;
	}

	public void addSharer(User user) {
		this.sharers.add(user);
	}

	public void removeSharer(User user) {
		this.sharers.remove(user);
	}

	public void addShareres(List<User> users) { 
		this.sharers.addAll(users);
	}

	public void setShareres(List<User> users) {
		this.sharers = users;
	}

	public boolean hasSharer(User sharer) {
		return this.sharers.contains(sharer);
	}
}
