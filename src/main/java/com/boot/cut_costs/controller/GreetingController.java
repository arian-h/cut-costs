package com.boot.cut_costs.controller;

import java.security.Principal;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boot.cut_costs.model.Greeting;

@RestController
public class GreetingController {
	
	private final String template = "Hello, %s!";
	private final AtomicLong counter = new AtomicLong();
	
	@RequestMapping(path="/greeting",method=RequestMethod.POST)
	public Greeting doGreeting(Principal principal, SecurityContextHolder auth) {
		String userId = principal.getName();
		Greeting greeting = new Greeting(counter.incrementAndGet(), String.format(template, userId));
		return greeting;
	}
}
