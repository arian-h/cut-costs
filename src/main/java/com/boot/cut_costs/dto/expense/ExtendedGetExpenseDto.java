package com.boot.cut_costs.dto.expense;

import java.util.Set;

import com.boot.cut_costs.dto.group.GetGroupDto;
import com.boot.cut_costs.dto.user.GetUserDto;

public class ExtendedGetExpenseDto extends GetExpenseDto {

	private static final long serialVersionUID = 1L;

	private GetGroupDto groupDto;
	
	private Set<GetUserDto> sharers;
	
	private String imageId;
	
	private String description;
	
	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public GetGroupDto getGroup() {
		return groupDto;
	}

	public void setGroup(GetGroupDto group) {
		this.groupDto = group;
	}

	public Set<GetUserDto> getSharers() {
		return sharers;
	}

	public void setSharers(Set<GetUserDto> sharers) {
		this.sharers = sharers;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
