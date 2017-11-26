package com.boot.cut_costs.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.boot.cut_costs.model.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
	public Expense findById(long id);
}
