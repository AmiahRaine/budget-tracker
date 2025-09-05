package dev.amiah.budget_tracker.repository;

import dev.amiah.budget_tracker.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {


    Page<Expense> findAllByOrderByTimeDesc(Pageable pageable);
}
