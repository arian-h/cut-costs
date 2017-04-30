package com.boot.cut_costs.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name="ACCOUNT_USER")
public class CustomUserDetails implements UserDetails {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "USER_ID")
	private long id;

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
	private Set<CustomRole> roles;

	public CustomUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<GrantedAuthority> authorities) {
		this.setUsername(username);
		this.setPassword(password);
		this.roles = new HashSet<CustomRole>();
		for (GrantedAuthority authority: authorities) {
			roles.add(new CustomRole(authority.getAuthority()));
		}
	}

	public CustomUserDetails() { // jpa only
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

	public Set<CustomRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<CustomRole> roles) {
		this.roles = roles;
	}
	
	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		ArrayList<String> authoritiesList = new ArrayList<String>();
		for (CustomRole role: getRoles()) {
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
	
	//delete if not necessary
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    
	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

}