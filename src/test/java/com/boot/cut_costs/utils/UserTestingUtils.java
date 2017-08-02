package com.boot.cut_costs.utils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boot.cut_costs.model.User;
import com.boot.cut_costs.repository.UserRepository;

@Component
public class UserTestingUtils {

	public UserTestingUtils() {
	}

	@Autowired
	private UserRepository userRepository;
	
	public long createUser(String name, String description, String imageId) {
		User user = new User();
		user.setName(name);
		user.setDescription(description);
		user.setImageId(imageId);
		userRepository.save(user);
		return user.getId();
	}

}
