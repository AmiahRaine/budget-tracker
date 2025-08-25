package dev.amiah.budget_tracker.model.data_type;

public enum ExpenseCategory {
    FOOD("Food"),
    CLOTHING("Clothing"),
    HOUSING("Housing"),
    UTILITIES("Utilities"),
    ENTERTAINMENT("Entertainment"),
    TRANSPORT("Transport"),
    BUSINESS("Business"),
    EDUCATION("Education"),
    MEDICINE("Medicine"),
    PERSONAL_CARE("Personal Care"),
    PLUSHIES("Plushies"),
    OTHER("Other");

    private final String humanName;

    ExpenseCategory(String humanReadableName) {
        this.humanName = humanReadableName;
    }

    /**
     * @return the human-readable form of this enum's name. For example, {@code Food} returns "Food"
     */
    public String getHumanReadableName() {
        return humanName;
    }
}
