package com.boot.cut_costs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boot.cut_costs.model.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
	public Invitation findById(long id);
}
