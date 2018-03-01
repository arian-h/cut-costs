package com.boot.cut_costs.dto.expense.get;

import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.dto.group.get.GroupGetDtoConverter;
import com.boot.cut_costs.dto.user.get.UserGetDtoConverter;
import com.boot.cut_costs.model.Expense;
import com.boot.cut_costs.model.User;

@Service
public class ExpenseGetDtoConverter {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private GroupGetDtoConverter groupDtoConverter;

	@Autowired
	private UserGetDtoConverter userDtoConverter;

    public ExpenseSnippetGetDto convertToDto(Expense expense, User loggedInUser) {
    	if (modelMapper.getTypeMap(Expense.class, ExpenseSnippetGetDto.class) == null) {
        	Converter<Expense, ExpenseSnippetGetDto> converter = context -> {
        		Expense source = context.getSource();
        		ExpenseSnippetGetDto target = new ExpenseSnippetGetDto();
        		target.setAmount(source.getAmount());
        		target.setTitle(source.getTitle());
        		target.setId(source.getId());
        		target.setOwner(userDtoConverter.convertToDto(loggedInUser));
        		return target;
        	};
        	modelMapper.createTypeMap(Expense.class, ExpenseSnippetGetDto.class).setConverter(converter);    		
    	}
    	return modelMapper.map(expense, ExpenseSnippetGetDto.class);
    }

    public ExpenseExtendedGetDto convertToExtendedDto(Expense expense, User loggedInUser) {
    	if (modelMapper.getTypeMap(Expense.class, ExpenseExtendedGetDto.class) == null) {
        	Converter<Expense, ExpenseExtendedGetDto> converter = context -> {
        		Expense source = context.getSource();
        		ExpenseExtendedGetDto target = new ExpenseExtendedGetDto();
        		target.setId(source.getId());
        		target.setAmount(source.getAmount());
        		target.setTitle(source.getTitle());
        		target.setDescription(source.getDescription());
        		target.setImageId(source.getImageId());
        		target.setGroup(groupDtoConverter.convertToDto(source.getGroup(), loggedInUser));
        		target.setOwner(userDtoConverter.convertToDto(loggedInUser));
    			target.setSharers(source.getSharers().stream()
    					.map(user -> userDtoConverter.convertToDto(user))
    					.collect(Collectors.toList()));
        		return target;
        	};
        	modelMapper.createTypeMap(Expense.class, ExpenseExtendedGetDto.class).setConverter(converter);
    	}
    	return modelMapper.map(expense, ExpenseExtendedGetDto.class);
    }

}