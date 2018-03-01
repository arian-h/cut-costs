package com.boot.cut_costs.dto.expense.get;

import java.util.List;

import com.boot.cut_costs.dto.group.get.GroupSnippetGetDto;
import com.boot.cut_costs.dto.user.get.UserSnippetGetDto;

public class ExpenseExtendedGetDto extends ExpenseSnippetGetDto {

	private static final long serialVersionUID = 1L;

	private GroupSnippetGetDto groupDto;
	
	private List<UserSnippetGetDto> sharers;
	
	private String imageId;
	
	private String description;
	
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public GroupSnippetGetDto getGroup() {
		return groupDto;
	}

	public void setGroup(GroupSnippetGetDto group) {
		this.groupDto = group;
	}

	public List<UserSnippetGetDto> getSharers() {
		return sharers;
	}

	public void setSharers(List<UserSnippetGetDto> sharers) {
		this.sharers = sharers;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
