package com.boot.cut_costs.dto.group.get;

import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.dto.expense.ExpenseDtoConverter;
import com.boot.cut_costs.dto.invitation.InvitationDtoConverter;
import com.boot.cut_costs.dto.user.UserDtoConverter;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;

@Service
public class GroupGetDtoConverter {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserDtoConverter userDtoConverter;

	@Autowired
	private ExpenseDtoConverter expenseDtoConverter;

	@Autowired
	private InvitationDtoConverter invitationDtoConverter;

	public SnippetGetGroupDto convertToDto(Group group, User loggedInUser) {
		if (modelMapper.getTypeMap(Group.class, SnippetGetGroupDto.class) == null) {
			Converter<Group, SnippetGetGroupDto> converter = context -> {
				Group source = context.getSource();
				SnippetGetGroupDto target = new SnippetGetGroupDto();
				target.setId(source.getId());
				target.setName(source.getName());
				target.setNumberOfMembers(source.getMembers().size() + 1); //including the admin
				target.setNumberOfExpenses(source.getExpenses().size());
				target.setIsAdmin(source.isAdmin(loggedInUser));
				return target;
			};
			modelMapper.createTypeMap(Group.class, SnippetGetGroupDto.class).setConverter(converter);			
		}
		return modelMapper.map(group, SnippetGetGroupDto.class);
    }

    public FullGroupGetDto convertToExtendedDto(Group group, User loggedInUser) {
    	if (modelMapper.getTypeMap(Group.class, FullGroupGetDto.class) == null) {
    		Converter<Group, FullGroupGetDto> converter = context -> {
    			Group source = context.getSource();
    			FullGroupGetDto target = new FullGroupGetDto();
    			target.setId(source.getId());
    			target.setName(source.getName());
    			target.setDescription(source.getDescription());
    			target.setAdmin(userDtoConverter.convertToDto(source.getAdmin()));
    			target.setIsAdmin(source.isAdmin(loggedInUser));
    			target.setExpenses(source.getExpenses().stream()
    					.map(expense -> expenseDtoConverter.convertToDto(expense))
    					.collect(Collectors.toList()));
    			return target;
    		};
    		modelMapper.createTypeMap(Group.class, FullGroupGetDto.class).setConverter(converter);    		
    	}
		return modelMapper.map(group, FullGroupGetDto.class);
    }

}