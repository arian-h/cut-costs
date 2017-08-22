package com.boot.cut_costs.utils;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.GroupRepository;

@Component
@Transactional
public class GroupTestingUtil {

	@Autowired
	private GroupRepository groupRepository;
	
	public GroupTestingUtil() {}

	/**
	 * Create a group object and save the it to the db
	 */
	public Group createGroup(String groupName, String description, String imageId, User admin) {
		Group group = new Group();
		group.setAdmin(admin);
		group.setName(groupName);
		group.setDescription(description);
		group.setImageId(imageId);
		groupRepository.save(group);
		return group;
	}
	
}
