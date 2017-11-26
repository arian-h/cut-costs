package com.boot.cut_costs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boot.cut_costs.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
	public Group findById(long id);
}