package com.boot.cut_costs.repository;

import org.springframework.data.repository.CrudRepository;

import com.boot.cut_costs.model.Expense;

public interface ExpenseRepository extends CrudRepository<Expense, Long> {
	public Expense findById(long id);
}
