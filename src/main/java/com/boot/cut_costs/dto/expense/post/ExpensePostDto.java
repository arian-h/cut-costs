package com.boot.cut_costs.dto.expense.post;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.boot.cut_costs.dto.expense.ExpenseBaseDto;
import com.boot.cut_costs.exception.BadRequestException;

public class ExpensePostDto extends ExpenseBaseDto {

	private static final long serialVersionUID = -9041216867439951403L;

	private MultipartFile image;

	private String description;

	private List<Long> sharers;

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public MultipartFile getImage() {
		return image;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Long> getSharers() {
		return sharers;
	}

	public void setSharers(String sharers) {
		this.sharers = null;
		try {
			if (sharers != null && sharers.length() > 2 && sharers.charAt(0) == '[' && sharers.charAt(sharers.length() - 1) == ']') {
				sharers = sharers.substring(1, sharers.length() - 1);
				String[] values = sharers.split(",");
				List<Long> l = new ArrayList<Long>();
				for (String s: values) {
					l.add(Long.parseLong(s.trim()));
				}
				this.sharers = l;
			}			
		} catch(ClassCastException e) {
			throw new BadRequestException("Invalid sharers ids passed");
		}

	}
}
