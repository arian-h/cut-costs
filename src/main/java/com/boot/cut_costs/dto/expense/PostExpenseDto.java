package com.boot.cut_costs.dto.expense;

import java.util.Set;

public class PostExpenseDto extends AbstractExpenseDto {

	private static final long serialVersionUID = 1L;
	/*
	 * Base64 image data
	 */
	private String image;

	private String description;

	private Set<Long> sharers;

	public void setImage(String image) {
		this.image = image;
	}

	public String getImage() {
		return image;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<Long> getSharers() {
		return sharers;
	}

	public void setSharers(Set<Long> sharers) {
		this.sharers = sharers;
	}
}
