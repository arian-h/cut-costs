package com.boot.cut_costs.dto.invitation;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.dto.group.GroupDtoConverter;
import com.boot.cut_costs.dto.user.UserDtoConverter;
import com.boot.cut_costs.model.Invitation;

@Service
public class InvitationDtoConverter {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private GroupDtoConverter groupDtoConverter;
	
	@Autowired
	private UserDtoConverter userDtoConverter;
	
    public GetInvitationDto convertToDto(Invitation invitation) {
    	if (modelMapper.getTypeMap(Invitation.class, GetInvitationDto.class) == null) {
        	Converter<Invitation, GetInvitationDto> converter = context -> {
        		GetInvitationDto target = new GetInvitationDto();
        		Invitation source = context.getSource();
        		target.setId(source.getId());
    			target.setGroup(groupDtoConverter.convertToDto(source.getGroup()));
    			target.setInviter(userDtoConverter.convertToDto(source.getInviter())); 
        		return target;
        	};
        	modelMapper.createTypeMap(Invitation.class, GetInvitationDto.class).setConverter(converter);    		
    	}
    	return modelMapper.map(invitation, GetInvitationDto.class);
    }
    
}
