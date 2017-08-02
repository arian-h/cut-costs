package com.boot.cut_costs.service;

import java.io.IOException;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.dto.user.AbstractUserDto;
import com.boot.cut_costs.model.CustomUserDetails;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.UserRepository;
import com.boot.cut_costs.utils.CommonUtils;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	private static Logger logger = LoggerFactory.getLogger(UserService.class);

	@Transactional
	public void save(User user) {
		userRepository.save(user);
		logger.debug("New User was created id: " + user.getId() + ", name: " + user.getName());
	}
	
	public User loadById(long id) {
		User user = userRepository.findById(id);
		if (user == null) {
			throw new ResourceNotFoundException("user with id " + id + " not found");
		}
		return user;
	}
	
	public void update(AbstractUserDto userDto, String username) throws IOException {
		User user = customUserDetailsService.loadUserByUsername(username).getUser();
		user.setName(userDto.getName());
		user.setDescription(userDto.getDescription());
		userRepository.save(user);
		logger.debug("New User was updated id: " + user.getId() + ", name: " + user.getName());
	}
	
	public User loadByUsername(String username) {
		CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
		return userDetails.getUser();
	}

	public void uploadImage(String image, String username) throws IOException {
		User user = customUserDetailsService.loadUserByUsername(username).getUser();
		user.setImageId(CommonUtils.decodeBase64AndSaveImage(image));
	}
}
