package com.boot.cut_costs.service;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import jersey.repackaged.com.google.common.collect.Lists;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.model.CustomUserDetails;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.UserRepository;
import com.boot.cut_costs.utils.CommonUtils;

@Service
@Transactional
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	public User loadById(long id) {
		User user = userRepository.findById(id);
		if (user == null) {
			throw new ResourceNotFoundException("User not found id: " + id);
		}
		return user;
	}

	public User update(String name, String description, String image, String username) throws IOException {
		User user = customUserDetailsService.loadUserByUsername(username).getUser();
		user.setName(name);
		user.setDescription(description);
		user.setImageId(CommonUtils.decodeBase64AndSaveImage(image));
		userRepository.save(user);
		logger.debug("User updated id: " + user.getId());
		return user;
	}

	public User loadByUsername(String username) {
		CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
		return userDetails.getUser();
	}
	
	public void save(User user) {
		userRepository.save(user);
	}
	
	public List<User> findAll() {
		return Lists.newArrayList(userRepository.findAll());
	}
}
