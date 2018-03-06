package com.boot.cut_costs.dto.invitation.get;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boot.cut_costs.dto.group.get.GroupGetDtoConverter;
import com.boot.cut_costs.dto.user.get.UserGetDtoConverter;
import com.boot.cut_costs.model.Invitation;
import com.boot.cut_costs.model.User;

@Service
public class InvitationGetDtoConverter {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private GroupGetDtoConverter groupDtoConverter;
	
	@Autowired
	private UserGetDtoConverter userDtoConverter;
	
    public InvitationSnippetGetDto convertToDto(Invitation invitation, User loggedInUser) {
    	if (modelMapper.getTypeMap(Invitation.class, InvitationSnippetGetDto.class) == null) {
        	Converter<Invitation, InvitationSnippetGetDto> converter = context -> {
        		InvitationSnippetGetDto target = new InvitationSnippetGetDto();
        		Invitation source = context.getSource();
        		target.setId(source.getId());
    			target.setGroup(groupDtoConverter.convertToDto(source.getGroup(), loggedInUser));
    			target.setInviter(userDtoConverter.convertToDto(source.getInviter())); 
        		return target;
        	};
        	modelMapper.createTypeMap(Invitation.class, InvitationSnippetGetDto.class).setConverter(converter);    		
    	}
    	return modelMapper.map(invitation, InvitationSnippetGetDto.class);
    }
    
}
