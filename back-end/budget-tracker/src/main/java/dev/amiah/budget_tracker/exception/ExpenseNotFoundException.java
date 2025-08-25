package dev.amiah.budget_tracker.exception;

public class ExpenseNotFoundException extends RuntimeException {

    public ExpenseNotFoundException(Long id) {
        super("Expense not found with id " + id);
    }
}
