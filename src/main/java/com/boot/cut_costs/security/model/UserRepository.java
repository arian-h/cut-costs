package com.boot.cut_costs.security.model;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserDetails, String>{
	public UserDetails findByUsername(String username);
}