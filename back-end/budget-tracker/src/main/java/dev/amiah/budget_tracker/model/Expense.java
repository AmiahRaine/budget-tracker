package dev.amiah.budget_tracker.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.amiah.budget_tracker.model.data_type.ExpenseCategory;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;


@Entity
@Table(name = "expense")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long id;

    @Column(name = "name")
    private String name;
    @Column(name = "amount")
    private Double amount;
    @Column(name = "time")
    private LocalDateTime time;
    @Column(name = "counterparty")
    private String counterparty; // Just the name; additional wording here dependent on if amount is + or -

    @Column(name = "category")
    @Enumerated(EnumType.STRING)
    private ExpenseCategory category;


    public Expense() {}

    public Expense(String name, Double amount, String counterparty, ExpenseCategory category) {
        this.name = name;
        this.amount = amount;
        this.counterparty = counterparty;
        this.category = category;
        this.time = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    @PrePersist
    private void onPrePersist() {
        // Make sure time is set
        if (time == null) {
            time = LocalDateTime.now();
        }
    }

    /**
     * @return The {@code counterparty}, prefixed with "Received from" or "Paid to" depending on
     * if the {@code amount} is positive or negative. If {@code counterparty}
     * {@linkplain String#isBlank() is blank} or {@code null}, will use "Unknown" instead.
     */
    @JsonProperty("counterpartyText")
    public String getCounterpartyText() {
        return (amount >= 0 ? "Received from " : "Paid to ")
             + (counterparty != null && !counterparty.isBlank() ? counterparty : "Unknown");
    }

    /**
     * @return The human-readable name of the category.
     */
    @JsonProperty("categoryText")
    public String getCategoryText() {
        return category.getHumanReadableName();
    }

    /**
     * Creates a formatted version of the time of the transaction, which follows
     * the user's date formatting preference.
     *
     * @param pref The preferences of the user who this expense belongs to.
     * @return The time of the transaction, formatted to the user's preference.
     */
    public String getTimeFormatted(UserPreference pref) {
        if (time != null) {
            return time.format(DateTimeFormatter.ofPattern(pref.getDatePatternComplete()));
        }
        // In practice, it should never get to this point because it is set in onPrePersist().
        // Although, it could happen if set to null afterward.
        return "How did we get here?";
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }


    public void setTime(LocalDateTime time) {
        this.time = time.truncatedTo(ChronoUnit.SECONDS);
    }

    public void setCounterparty(String counterparty) {
        this.counterparty = counterparty;
    }

    public void setCategory(ExpenseCategory category) {
        this.category = category;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public String getCounterparty() {
        return counterparty;
    }

    public ExpenseCategory getCategory() {
        return category;
    }

    // Overrides

    // Auto-generated toString
    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", amount=" + amount +
                ", time=" + time +
                ", counterparty='" + counterparty + '\'' +
                ", category=" + category +
                '}';
    }

}
