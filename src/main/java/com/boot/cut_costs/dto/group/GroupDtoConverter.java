package com.boot.cut_costs.dto.group;

import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.dto.expense.ExpenseDtoConverter;
import com.boot.cut_costs.dto.invitation.InvitationDtoConverter;
import com.boot.cut_costs.dto.user.UserDtoConverter;
import com.boot.cut_costs.model.Group;

@Service
public class GroupDtoConverter {

	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private UserDtoConverter userDtoConverter;
	
	@Autowired
	private ExpenseDtoConverter expenseDtoConverter;
	
	@Autowired
	private InvitationDtoConverter invitationDtoConverter;
	
	public GetGroupDto convertToDto(Group group) {
		if (modelMapper.getTypeMap(Group.class, GetGroupDto.class) == null) {
			Converter<Group, GetGroupDto> converter = context -> {
				Group source = context.getSource();
				GetGroupDto target = new GetGroupDto();
				target.setId(source.getId());
				target.setName(source.getName());
				target.setDescription(source.getDescription());
				return target;
			};
			modelMapper.createTypeMap(Group.class, GetGroupDto.class).setConverter(converter);			
		}
		return modelMapper.map(group, GetGroupDto.class);
    }
    
    public ExtendedGetGroupDto convertToExtendedDto(Group group) {
    	if (modelMapper.getTypeMap(Group.class, ExtendedGetGroupDto.class) == null) {
    		Converter<Group, ExtendedGetGroupDto> converter = context -> {
    			Group source = context.getSource();
    			ExtendedGetGroupDto target = new ExtendedGetGroupDto();
    			target.setId(source.getId());
    			target.setDescription(source.getDescription());
    			target.setName(source.getName());
    			target.setImageId(source.getImageId());
    			target.setAdmin(userDtoConverter.convertToDto(source.getAdmin()));
    			target.setExpenses(source.getExpenses().stream()
    					.map(expense -> expenseDtoConverter.convertToDto(expense))
    					.collect(Collectors.toSet()));
    			target.setMembers(source.getMembers().stream()
    					.map(member -> userDtoConverter.convertToDto(member))
    					.collect(Collectors.toSet()));
    			return target;
    		};
    		modelMapper.createTypeMap(Group.class, ExtendedGetGroupDto.class).setConverter(converter);    		
    	}
		return modelMapper.map(group, ExtendedGetGroupDto.class);
    }
	
}