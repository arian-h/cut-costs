package com.boot.cut_costs.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name="greeting")
public class Greeting {
	@Id
	private final long id;

	private final String content;

	public long getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public Greeting(long id, String content) {
		this.id = id;
		this.content = content;
	}

}