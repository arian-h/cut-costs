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

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;


@Entity
@Table(name="CCUser")
public class CCUser implements Serializable {

	private static final long serialVersionUID = 1L;

	public CCUser() {}
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
	private long id;

	@Size(min=15, max=15)
	@Column(name="image_id")
	private String imageId;
	
	@NotBlank
	@Email
	@Column(name="email")
	private String email;
	
	@Size(max=100)
	@Column(name="description")
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, mappedBy="admin")
	private Set<CCGroup> ownedGroup;
	
	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<CCGroup> memberGroups;
	
	@OneToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER, mappedBy = "owner")
	private Set<CCExpense> ownedExpenses;

	@ManyToMany(cascade = CascadeType.ALL, fetch=FetchType.EAGER)
	private Set<CCExpense> receivedExpenses;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="inviter")
	private Set<CCInvitation> ownedInvitations;

	@OneToMany(cascade=CascadeType.ALL, mappedBy="invitee")
	private Set<CCInvitation> receivedInvitations;

	public Set<CCInvitation> getOwnedInvitations() {
		return ownedInvitations;
	}

	public void setOwnedInvitations(Set<CCInvitation> ownedInvitations) {
		this.ownedInvitations = ownedInvitations;
	}

	public Set<CCInvitation> getReceivedInvitations() {
		return receivedInvitations;
	}

	public void setReceivedInvitations(Set<CCInvitation> receivedInvitations) {
		this.receivedInvitations = receivedInvitations;
	}
	
	public Set<CCExpense> getOwnedExpenses() {
		return ownedExpenses;
	}

	public void setOwnedExpenses(Set<CCExpense> ownedExpenses) {
		this.ownedExpenses = ownedExpenses;
	}
	
	public Set<CCGroup> getOwnedGroup() {
		return ownedGroup;
	}

	public void setOwnedGroup(Set<CCGroup> ownedGroup) {
		this.ownedGroup = ownedGroup;
	}

	public Set<CCGroup> getMemberGroups() {
		return memberGroups;
	}

	public void setMemberGroups(Set<CCGroup> memberGroups) {
		this.memberGroups = memberGroups;
	}

	public Set<CCExpense> getReceivedExpenses() {
		return receivedExpenses;
	}

	public void setReceivedExpenses(Set<CCExpense> receivedExpenses) {
		this.receivedExpenses = receivedExpenses;
	}

	
	public String getImageId() {
		return imageId;
	}
	
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

}
