package dev.amiah.budget_tracker.repository;

import dev.amiah.budget_tracker.model.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    @Query("SELECT SUM(e.amount) FROM Expense e")
    BigDecimal getTotalBalance();

    Page<Expense> findAllByOrderByTimeDesc(Pageable pageable);
}
