package com.boot.cut_costs.repository;

import org.springframework.data.repository.CrudRepository;

import com.boot.cut_costs.model.Invitation;

public interface InvitationRepository extends CrudRepository<Invitation, Long> {
	public Invitation findById(long id);
}
