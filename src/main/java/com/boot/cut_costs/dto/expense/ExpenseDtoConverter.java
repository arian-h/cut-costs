package com.boot.cut_costs.dto.expense;

import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.dto.group.GroupDtoConverter;
import com.boot.cut_costs.dto.user.UserDtoConverter;
import com.boot.cut_costs.model.Expense;

@Service
public class ExpenseDtoConverter {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private GroupDtoConverter groupDtoConverter;
	
	@Autowired
	private UserDtoConverter userDtoConverter;
	
    public GetExpenseDto convertToDto(Expense expense) {
    	if (modelMapper.getTypeMap(Expense.class, GetExpenseDto.class) == null) {
        	Converter<Expense, GetExpenseDto> converter = context -> {
        		Expense source = context.getSource();
        		GetExpenseDto target = new GetExpenseDto();
        		target.setAmount(source.getAmount());
        		target.setTitle(source.getTitle());
        		target.setId(source.getId());
        		return target;
        	};
        	modelMapper.createTypeMap(Expense.class, GetExpenseDto.class).setConverter(converter);    		
    	}
    	return modelMapper.map(expense, GetExpenseDto.class);
    }
    
    public ExtendedGetExpenseDto convertToExtendedDto(Expense expense) {
    	if (modelMapper.getTypeMap(Expense.class, ExtendedGetExpenseDto.class) == null) {
        	Converter<Expense, ExtendedGetExpenseDto> converter = context -> {
        		Expense source = context.getSource();
        		ExtendedGetExpenseDto target = new ExtendedGetExpenseDto();
        		target.setId(source.getId());
        		target.setAmount(source.getAmount());
        		target.setTitle(source.getTitle());
        		target.setDescription(source.getDescription());
        		target.setImageId(source.getImageId());
        		target.setGroup(groupDtoConverter.convertToDto(source.getGroup()));
    			target.setSharers(source.getSharers().stream()
    					.map(user -> userDtoConverter.convertToDto(user))
    					.collect(Collectors.toList()));
        		return target;
        	};
        	modelMapper.createTypeMap(Expense.class, ExtendedGetExpenseDto.class).setConverter(converter);
    	}
    	return modelMapper.map(expense, ExtendedGetExpenseDto.class);
    }
}