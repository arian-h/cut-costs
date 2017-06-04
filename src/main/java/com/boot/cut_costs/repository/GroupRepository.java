package com.boot.cut_costs.repository;

import org.springframework.data.repository.CrudRepository;

import com.boot.cut_costs.model.Group;

public interface GroupRepository extends CrudRepository<Group, Long> {
	public Group findById(long id);
	
}
