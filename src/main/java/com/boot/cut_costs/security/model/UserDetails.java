package com.boot.cut_costs.security.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

@Entity
@Table(name="USER")
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@NotBlank
	@Column(name = "USERNAME", unique=true)
	private String username;
	
	@NotBlank
	@Column(name = "PASSWORD")
	private String password;

	@Column(name = "LOCKED")
	private boolean locked;

	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
	@JoinTable(name = "USER_ROLE", 
				joinColumns = @JoinColumn(name = "USER_ID"), 
				inverseJoinColumns = @JoinColumn(name = "ROLE_ID"))
	private Set<Role> roles;

	public UserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities) {
		this.setUsername(username);
		this.setPassword(password);
		this.roles = new HashSet<Role>();
		for (GrantedAuthority authority: authorities) {
			roles.add(new Role(authority.getAuthority()));
		}
	}

	public UserDetails() { // jpa only
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
		return !isLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

}