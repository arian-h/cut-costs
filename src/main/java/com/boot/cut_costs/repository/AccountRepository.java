package com.boot.cut_costs.repository;

import org.springframework.data.repository.CrudRepository;

import com.boot.cut_costs.config.security.CustomUserDetails;

public interface AccountRepository extends CrudRepository<CustomUserDetails, String>{
	public CustomUserDetails findByUsername(String username);
}