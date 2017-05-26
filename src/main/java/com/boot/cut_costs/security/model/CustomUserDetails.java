package com.boot.cut_costs.security.model;

import java.util.ArrayList;
import java.util.Collection;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.boot.cut_costs.model.CCUser;

@Entity
@Table(name="UserDetails")
public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Id")
    private long id;

	@Column(name = "Username", unique=true)
	private String username;
	
	@NotNull
	@JoinColumn(name="User")
	@OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	private CCUser user;

	@Column(name = "Password")
	private String password;

	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "User_Role", 
				joinColumns = @JoinColumn(name = "UserId"), 
				inverseJoinColumns = @JoinColumn(name = "RoleId"))
	private Set<Role> roles;

	public CustomUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities) {
		this.setUsername(username);
		this.setPassword(password);
		this.roles = new HashSet<Role>();
		for (GrantedAuthority authority: authorities) {
			roles.add(new Role(authority.getAuthority()));
		}
	}

	public CustomUserDetails() { // jpa only
	}
	
	
	public CCUser getUser() {
		return user;
	}

	public void setUser(CCUser user) {
		this.user = user;
	}

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	@Override
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		ArrayList<String> authoritiesList = new ArrayList<String>();
		for (Role role: getRoles()) {
			authoritiesList.add(role.getRole());
		}			
		return AuthorityUtils.commaSeparatedStringToAuthorityList(String.join(",", authoritiesList));
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}