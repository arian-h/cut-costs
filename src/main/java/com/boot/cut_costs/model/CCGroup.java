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
@Table(name="CCGroup")
public class CCGroup implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

	@NotNull
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinColumn(name="admin_id", referencedColumnName="id")
	private CCUser admin;

	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "CCGroup_CCUser", 
		joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"), 
		inverseJoinColumns = @JoinColumn(name = "member_id", referencedColumnName = "id"))
	private Set<CCUser> members;

	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<CCExpense> expenses;
	
	@Column(name="description")
	private String description;

	@NotEmpty
	@Column(name="name")
	private String name;
	
	@Size(min=15, max=15)
	@Column(name="image_id")
	private String imageId;
	
	public CCGroup() {}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public CCUser getAdmin() {
		return admin;
	}

	public void setAdmin(CCUser admin) {
		this.admin = admin;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	public String getImageId() {
		return imageId;
	}
	
	public Set<CCUser> getMembers() {
		return members;
	}

	public void setMembers(Set<CCUser> members) {
		this.members = members;
	}

	public Set<CCExpense> getExpenses() {
		return expenses;
	}

	public void setExpenses(Set<CCExpense> expenses) {
		this.expenses = expenses;
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
