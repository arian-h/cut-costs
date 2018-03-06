package com.boot.cut_costs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.boot.cut_costs.model.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {

	public Invitation findById(long id);

	@Query("SELECT COUNT(*) FROM Invitation where inviter_id = :inviterId AND invitee_id = :inviteeId AND group_id = :groupId")
	public Long countInvitation(@Param("inviterId") long inviterId,
			@Param("inviteeId") long inviteeId, @Param("groupId") long groupId);

}