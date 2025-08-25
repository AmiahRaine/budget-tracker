package dev.amiah.budget_tracker.model.data_type;

public enum DatePattern {
    MONTH_DAY_YEAR("M d yyyy"),
    DAY_MONTH_YEAR("d M yyyy"),
    YEAR_MONTH_DAY("yyyy M d"),
    YEAR_DAY_MONTH("yyyy d M"),

    MONTH_DAY_YEAR_WORD("MMM d yyyy"),
    DAY_MONTH_YEAR_WORD("dd MMM yyyy"),
    YEAR_MONTH_DAY_WORD("yyyy MMM d"),
    YEAR_DAY_MONTH_WORD("yyyy d MMM"),

    MONTH_DAY_YEAR_LONG("E, MMM d, yyyy"),
    DAY_MONTH_YEAR_LONG("E, d MMM, yyyy"),
    YEAR_MONTH_DAY_LONG("E, yyyy, MMM d"),
    YEAR_DAY_MONTH_LONG("E, yyyy, d MMM");


    private final String pattern;

    DatePattern(String pattern) {
        this.pattern = pattern;
    };

    /**
     * @return {@code true} if the {@code DatePattern} allows custom separators.
     */
    public boolean allowsSeparators() {
        return (this == MONTH_DAY_YEAR || this == DAY_MONTH_YEAR
             || this == YEAR_DAY_MONTH || this == YEAR_MONTH_DAY);
    }

    public String getPattern() {
        return pattern;
    }

}
