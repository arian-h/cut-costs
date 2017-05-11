package com.boot.cut_costs.security.model;

import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<CustomUserDetails, String>{
	public CustomUserDetails findByUsername(String username);
}