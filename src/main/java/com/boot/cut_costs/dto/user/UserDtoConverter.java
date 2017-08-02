package com.boot.cut_costs.dto.user;

import java.util.stream.Collectors;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.dto.expense.ExpenseDtoConverter;
import com.boot.cut_costs.dto.group.GroupDtoConverter;
import com.boot.cut_costs.dto.invitation.InvitationDtoConverter;
import com.boot.cut_costs.model.User;

@Service
public class UserDtoConverter {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ExpenseDtoConverter expenseDtoConverter;

	@Autowired
	private InvitationDtoConverter invitationDtoConverter;

	@Autowired
	private GroupDtoConverter groupDtoConverter;

	public GetUserDto convertToDto(User user) {
		if (modelMapper.getTypeMap(User.class, GetUserDto.class) == null) {
			Converter<User, GetUserDto> converter = context -> {
				User source = context.getSource();
				GetUserDto target = new GetUserDto();
				target.setDescription(source.getDescription());
				target.setId(source.getId());
				target.setName(source.getName());
				target.setImageId(source.getImageId());
				return target;
			};
			modelMapper.createTypeMap(User.class, GetUserDto.class).setConverter(converter);			
		}
		return modelMapper.map(user, GetUserDto.class);
	}

	public ExtendedGetUserDto convertToExtendedDto(User user) {		
		if (modelMapper.getTypeMap(User.class, ExtendedGetUserDto.class) == null) {
			Converter<User, ExtendedGetUserDto> converter = context -> {
				ExtendedGetUserDto target = new ExtendedGetUserDto();
				User source = context.getSource();
				target.setDescription(source.getDescription());
				target.setId(source.getId());
				target.setName(source.getName());
				target.setImageId(source.getImageId());
				target.setOwnedExpenses(source.getOwnedExpenses().stream()
						.map(expense -> expenseDtoConverter.convertToDto(expense))
						.collect(Collectors.toSet()));
				target.setReceivedExpenses(source.getReceivedExpenses().stream()
						.map(expense -> expenseDtoConverter.convertToDto(expense))
						.collect(Collectors.toSet()));
				target.setReceivedInvitations(source
						.getReceivedInvitations()
						.stream()
						.map(invitation -> invitationDtoConverter
								.convertToDto(invitation))
						.collect(Collectors.toSet()));
				target.setOwnedGroups(source.getOwnedGroups().stream()
						.map(group -> groupDtoConverter.convertToDto(group))
						.collect(Collectors.toSet()));
				target.setMemberGroups(source.getMemberGroups().stream()
						.map(group -> groupDtoConverter.convertToDto(group))
						.collect(Collectors.toSet()));
				return target;
			};
			modelMapper.createTypeMap(User.class, ExtendedGetUserDto.class).setConverter(converter);			
		}
		return modelMapper.map(user, ExtendedGetUserDto.class);
	}
}
