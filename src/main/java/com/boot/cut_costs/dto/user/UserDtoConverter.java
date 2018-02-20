package com.boot.cut_costs.dto.user;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.dto.expense.ExpenseDtoConverter;
import com.boot.cut_costs.dto.group.get.GroupGetDtoConverter;
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
	private GroupGetDtoConverter groupDtoConverter;

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
}
