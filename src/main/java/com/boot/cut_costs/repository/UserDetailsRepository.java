package com.boot.cut_costs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boot.cut_costs.model.CustomUserDetails;

public interface UserDetailsRepository extends JpaRepository<CustomUserDetails, String> {
	
	public CustomUserDetails findByUsername(String username);
	public boolean existsByUsername(String username);
	
}