package com.boot.cut_costs.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boot.cut_costs.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findById(long id);
	public List<User> findByNameStartingWithAndIdNotIn(String searchTerm, List<Long> excludeIds);
	public List<User> findByNameStartingWithAndIdInAndIdNotIn(String searchTerm, List<Long> groupId, List<Long> contributorIds);
}