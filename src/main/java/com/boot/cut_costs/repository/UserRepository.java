package com.boot.cut_costs.repository;

import org.springframework.data.repository.CrudRepository;

import com.boot.cut_costs.model.User;

public interface UserRepository extends CrudRepository<User, Long>{
	
}
