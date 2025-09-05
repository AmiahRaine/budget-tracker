package dev.amiah.budget_tracker.controller;

import dev.amiah.budget_tracker.assembler.ExpenseModelAssembler;
import dev.amiah.budget_tracker.dto.TotalBalanceObject;
import dev.amiah.budget_tracker.exception.ExpenseNotFoundException;
import dev.amiah.budget_tracker.model.Expense;
import dev.amiah.budget_tracker.repository.ExpenseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static dev.amiah.budget_tracker.util.ControllerUtil.setIfNotNull;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class ExpenseController {

    private final ExpenseRepository repository;
    private final ExpenseModelAssembler assembler;
    private final PagedResourcesAssembler<Expense> pagedAssembler;

    public ExpenseController(ExpenseRepository repository, ExpenseModelAssembler assembler,
                             PagedResourcesAssembler<Expense> pagedAssembler)
    {
        this.repository = repository;
        this.assembler = assembler;
        this.pagedAssembler = pagedAssembler;
    }

    /**
     * Retrieves expenses ordered by time descending in pages of a specified size.
     *
     * @param page Which page of entries should be retrieved
     * @param size The number of entries per page
     * @return A page of {@code Expense}s.
     */
    @GetMapping("/api/expenses/paged")
    public PagedModel<EntityModel<Expense>> findExpensesPaged(@RequestParam(value = "page", defaultValue = "0") int page,
                                                              @RequestParam(value = "size", defaultValue = "20") int size)
    {
        Pageable pageable = PageRequest.of(page, size);
        Page<Expense> pagedExpenses = repository.findAllByOrderByTimeDesc(pageable);

        return pagedAssembler.toModel(pagedExpenses, assembler);
    }

    /**
     * @return A sum of all {@code Expense} amount values.
     */
    @GetMapping("/api/expenses/total")
    public TotalBalanceObject getTotalBalance() {
        return new TotalBalanceObject(repository.getTotalBalance());
    }

    // Basic CRUD operations

    /**
     * @return All {@code Expense}s in the database.
     */
    @GetMapping("/api/expenses")
    public CollectionModel<EntityModel<Expense>> all() {
        // Add links to each expense and link to aggregate root (which happens to be this method)
        List<EntityModel<Expense>> expenses =  repository.findAll().stream().map(assembler::toModel).toList();

        // Return the above, with a link to self
        return CollectionModel.of(expenses, linkTo(methodOn(ExpenseController.class).all()).withSelfRel());
    }

    /**
     * Saves a new {@code Expense} to the database.
     *
     * @param expense The new {@code Expense} to save.
     * @return A response with the saved {@code Expense}.
     */
    @PostMapping("/api/expenses")
    public ResponseEntity<?> insert(@RequestBody Expense expense) {
        EntityModel<Expense> entityModel = assembler.toModel(repository.save(expense));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    /**
     * Finds the specified {@code Expense} with a matching {@code id}
     * and returns it.
     *
     * @param id The {@code id} of the {@code Expense} to find.
     * @return The found {@code Expense}.
     */
    @GetMapping("/api/expenses/{id}")
    public EntityModel<Expense> one(@PathVariable Long id) {
        Expense expense = repository.findById(id).orElseThrow(() -> new ExpenseNotFoundException(id));

        return assembler.toModel(expense);
    }

    /**
     * Updates the data of an {@code Expense} with the specified {@code id}. If
     * an {@code Expense} with that {@code id} is not found, it will insert the
     * {@code Expense} instead.
     *
     * @param expense The new {@code Expense} data to save.
     * @param id The {@code id} of the {@code Expense} to be updated.
     * @return A response with the updated {@code Expense}, or a newly inserted one if no existing entry matched the {@code id}.
     */
    @PutMapping("/api/expenses/{id}")
    public ResponseEntity<?> update(@RequestBody Expense expense, @PathVariable Long id) {
        Expense updatedExpense = repository.findById(id)
            .map(dbExpense -> {
                dbExpense.setName(expense.getName());
                dbExpense.setAmount(expense.getAmount());
                dbExpense.setTime(expense.getTime());
                dbExpense.setCounterparty(expense.getCounterparty());
                dbExpense.setCategory(expense.getCategory());
                return repository.save(dbExpense);
            }).orElseGet(() -> repository.save(expense));

        EntityModel<Expense> entityModel = assembler.toModel(updatedExpense);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }


    /**
     * Patches the data of an {@code Expense} with the specified {@code id}. Data that is {@code null} will
     * be ignored and not set. The {@code Expense} with the specified {@code id} must exist, otherwise an
     * {@link ExpenseNotFoundException} will be thrown.
     *
     * @param expense The new {@code Expense} data to save.
     * @param id The {@code id} of the {@code Expense} to be patched.
     * @return A response with the patched {@code Expense}.
     */
    @PatchMapping("/api/expenses/{id}")
    public ResponseEntity<?> patch(@RequestBody Expense expense, @PathVariable Long id) {
        Expense patchedExpense = repository.findById(id)
                .map(dbExpense -> {
                    setIfNotNull(expense.getName(), dbExpense::setName);
                    setIfNotNull(expense.getAmount(), dbExpense::setAmount);
                    setIfNotNull(expense.getTime(), dbExpense::setTime);
                    setIfNotNull(expense.getCounterparty(), dbExpense::setCounterparty);
                    setIfNotNull(expense.getCategory(), dbExpense::setCategory);
                    return repository.save(dbExpense);
                }).orElseThrow(() -> new ExpenseNotFoundException(id));

        EntityModel<Expense> entityModel = assembler.toModel(patchedExpense);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    /**
     * Deletes the {@code Expense} with the provided {@code id}.
     *
     * @return An HTTP 204 no content response.
     */
    @DeleteMapping("/api/expenses/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
