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
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="CCExpense")
public class CCExpense implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private long id;

	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@NotNull
	@JoinColumn(name="owner_id", referencedColumnName="id")
	private CCUser owner;

	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@NotNull
	@JoinColumn(name="group_id", referencedColumnName="id")
	private CCGroup group;

	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "CCExpense_CCSharer", joinColumns = @JoinColumn(name = "expense_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "sharer_id", referencedColumnName = "id"))
	private Set<CCUser> sharers;
	
	@Size(max=100)
	private String description;
	
	@Size(min=5, max=25)
	private String title;
	
	@Min(value=0L)
	private long amount;
	
	@Size(min=15, max=15)
	private String imageId;
	
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

	public CCUser getOwner() {
		return owner;
	}

	public void setOwner(CCUser owner) {
		this.owner = owner;
	}

	public CCGroup getGroup() {
		return group;
	}

	public void setGroup(CCGroup group) {
		this.group = group;
	}

	public Set<CCUser> getSharers() {
		return sharers;
	}

	public void setSharers(Set<CCUser> sharers) {
		this.sharers = sharers;
	}

}
