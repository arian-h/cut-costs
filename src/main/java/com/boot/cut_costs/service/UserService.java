package com.boot.cut_costs.service;

import java.io.IOException;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.DTO.UserDto;
import com.boot.cut_costs.model.CustomUserDetails;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.UserRepository;
import com.boot.cut_costs.util.ImageUtils;

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
		logger.debug("New User was created");
	}
	
	public User load(long id) {
		try {
			return userRepository.findById(id);
		} catch (Exception e) {
			throw new ResourceNotFoundException("user with id " + id + " not found");
		}
	}
	
	public void update(CustomUserDetails userDetails, UserDto userDto) throws IOException {
		User user = userDetails.getUser();
		if (userDto.getName() != null) {
			user.setName(userDto.getName());			
		}
		if (userDto.getDescription() != null) {
			user.setDescription(userDto.getDescription());
		}
		if (userDto.getImage() != null) {
			//save useruserDto image to a file, generate an id for it
			String imageData = userDto.getImage();
			String imageId = ImageUtils.decodeBase64AndSaveImage(imageData);
			user.setImageId(imageId);
		}
		userRepository.save(user);
	}
	
	public User getUserByUsername(String username) {
		CustomUserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
		return userDetails.getUser();
	}
}
