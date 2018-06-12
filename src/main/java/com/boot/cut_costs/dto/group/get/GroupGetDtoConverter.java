package com.boot.cut_costs.dto.group.get;

import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.dto.expense.get.ExpenseGetDtoConverter;
import com.boot.cut_costs.dto.invitation.get.InvitationGetDtoConverter;
import com.boot.cut_costs.dto.user.get.UserGetDtoConverter;
import com.boot.cut_costs.model.Group;
import com.boot.cut_costs.model.User;

@Service
public class GroupGetDtoConverter {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private UserGetDtoConverter userDtoConverter;

	@Autowired
	private ExpenseGetDtoConverter expenseDtoConverter;

	@Autowired
	private InvitationGetDtoConverter invitationDtoConverter;

	public GroupSnippetGetDto convertToDto(Group group, User loggedInUser) {
		if (modelMapper.getTypeMap(Group.class, GroupSnippetGetDto.class) == null) {
			Converter<Group, GroupSnippetGetDto> converter = context -> {
				Group source = context.getSource();
				GroupSnippetGetDto target = new GroupSnippetGetDto();
				target.setId(source.getId());
				target.setName(source.getName());
				target.setNumberOfMembers(source.getMembers().size() + 1); // +1 for admin
				target.setNumberOfExpenses(source.getExpenses().size());
				target.setIsAdmin(source.isAdmin(loggedInUser));
				target.setTotalAmount(source.getExpenses().stream().map(expense -> expense.getAmount()).mapToLong(amount -> amount).sum());
				return target;
			};
			modelMapper.createTypeMap(Group.class, GroupSnippetGetDto.class).setConverter(converter);
		}
		return modelMapper.map(group, GroupSnippetGetDto.class);
    }

    public GroupExtendedGetDto convertToExtendedDto(Group group, User loggedInUser) {
    	if (modelMapper.getTypeMap(Group.class, GroupExtendedGetDto.class) == null) {
    		Converter<Group, GroupExtendedGetDto> converter = context -> {
    			Group source = context.getSource();
    			GroupExtendedGetDto target = new GroupExtendedGetDto();
    			target.setId(source.getId());
    			target.setName(source.getName());
    			target.setDescription(source.getDescription());
    			target.setAdmin(userDtoConverter.convertToDto(source.getAdmin()));
    			target.setIsAdmin(source.isAdmin(loggedInUser));
    			target.setExpenses(source.getExpenses().stream()
					.map(expense -> expenseDtoConverter.convertToDto(expense, loggedInUser))
					.collect(Collectors.toList()));
    			target.setMembers(source.getMembers().stream()
    				.map(member -> userDtoConverter.convertToDto(member))
    				.collect(Collectors.toList()));
    			return target;
    		};
    		modelMapper.createTypeMap(Group.class, GroupExtendedGetDto.class).setConverter(converter);    		
    	}
		return modelMapper.map(group, GroupExtendedGetDto.class);
	}

}