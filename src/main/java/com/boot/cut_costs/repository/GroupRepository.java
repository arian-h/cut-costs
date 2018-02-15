package com.boot.cut_costs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boot.cut_costs.model.Group;

public interface GroupRepository extends JpaRepository<Group, Long> {
	public Group findById(long id);
	//TODO this is wrong !!! it should take into account the username
	@Query("SELECT COUNT(*) FROM Group where admin_id = :adminId AND name = :groupName")
	public Long countByName(@Param("groupName") String groupName, @Param("adminId") long adminId);
}