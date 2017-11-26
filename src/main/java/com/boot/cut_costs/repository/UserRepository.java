package com.boot.cut_costs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boot.cut_costs.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findById(long id);

}