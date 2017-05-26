package com.boot.cut_costs.controller;

import java.security.Principal;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.model.CCGroup;

@RestController
@RequestMapping("/group")
public class GroupController {
	
	@RequestMapping(path="",method=RequestMethod.POST)
	public void createGroup(@RequestBody CCGroup group) {
		
		String username = "administrator";
		
		
//		POST - /group
//		Create a new group and set the user who created group as admin
	}
	
	@RequestMapping(path="/{groupId}",method=RequestMethod.DELETE)
	public void deleteGroup(@PathVariable String groupId, Principal principal) {
//		POST - /group
//		Create a new group and set the user who created group as admin
	}
}
