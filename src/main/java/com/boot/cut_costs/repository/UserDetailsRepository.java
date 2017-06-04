package com.boot.cut_costs.repository;

import org.springframework.data.repository.CrudRepository;

import com.boot.cut_costs.model.CustomUserDetails;

public interface UserDetailsRepository extends CrudRepository<CustomUserDetails, String> {
	
	public CustomUserDetails findByUsername(String username);
	public boolean existsByUsername(String username);
	
}