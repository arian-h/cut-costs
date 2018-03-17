package com.boot.cut_costs.controller;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.dto.user.get.UserExtendedGetDto;
import com.boot.cut_costs.dto.user.get.UserGetDtoConverter;
import com.boot.cut_costs.dto.user.post.PostUserDto;
import com.boot.cut_costs.exception.InputValidationException;
import com.boot.cut_costs.model.User;
import com.boot.cut_costs.service.UserService;
import com.boot.cut_costs.validator.UserDtoValidator;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserDtoValidator updateUserDtoValidator;

	@Autowired
	private UserGetDtoConverter userDtoConverter;

	@RequestMapping(path = "/{userId}", method = RequestMethod.GET)
	public UserExtendedGetDto get(@PathVariable long userId) throws IllegalAccessException, InvocationTargetException {
		return userDtoConverter.convertToExtendedDto(userService.loadById(userId));
	}

	@RequestMapping(path = "", method = RequestMethod.PUT)
	public UserExtendedGetDto update(@RequestBody PostUserDto userDto, Principal principal, BindingResult result) throws IOException {
		updateUserDtoValidator.validate(userDto, result);
		if (result.hasErrors()) {
			throw new InputValidationException(result.getFieldError().getField());
		}
		User user = userService.update(userDto.getName(), userDto.getDescription(), userDto.getImage(), principal.getName());
		return userDtoConverter.convertToExtendedDto(user);
	}
}