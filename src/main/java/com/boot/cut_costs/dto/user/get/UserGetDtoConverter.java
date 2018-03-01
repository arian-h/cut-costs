package com.boot.cut_costs.dto.user.get;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.dto.expense.get.ExpenseGetDtoConverter;
import com.boot.cut_costs.dto.group.get.GroupGetDtoConverter;
import com.boot.cut_costs.dto.invitation.InvitationDtoConverter;
import com.boot.cut_costs.model.User;

@Service
public class UserGetDtoConverter {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ExpenseGetDtoConverter expenseDtoConverter;

	@Autowired
	private InvitationDtoConverter invitationDtoConverter;

	@Autowired
	private GroupGetDtoConverter groupDtoConverter;

	public UserExtendedGetDto convertToExtendedDto(User user) {
		if (modelMapper.getTypeMap(User.class, UserExtendedGetDto.class) == null) {
			Converter<User, UserExtendedGetDto> converter = context -> {
				User source = context.getSource();
				UserExtendedGetDto target = new UserExtendedGetDto();
				target.setDescription(source.getDescription());
				target.setId(source.getId());
				target.setName(source.getName());
				target.setImageId(source.getImageId());
				return target;
			};
			modelMapper.createTypeMap(User.class, UserExtendedGetDto.class).setConverter(converter);			
		}
		return modelMapper.map(user, UserExtendedGetDto.class);
	}

	public UserSnippetGetDto convertToDto(User user) {
		if (modelMapper.getTypeMap(User.class, UserSnippetGetDto.class) == null) {
			Converter<User, UserSnippetGetDto> converter = context -> {
				User source = context.getSource();
				UserSnippetGetDto target = new UserSnippetGetDto();
				target.setId(source.getId());
				target.setName(source.getName());
				return target;
			};
			modelMapper.createTypeMap(User.class, UserSnippetGetDto.class).setConverter(converter);			
		}
		return modelMapper.map(user, UserSnippetGetDto.class);
	}

}