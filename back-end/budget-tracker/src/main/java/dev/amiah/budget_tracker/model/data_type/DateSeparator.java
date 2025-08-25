package dev.amiah.budget_tracker.model.data_type;

public enum DateSeparator {
    SPACE(" ", "Space"),
    SLASH("/", "Slash"),
    DOT(".", "Dot"),
    HYPHEN("-", "Hyphen");

    private final String separator;

    private final String humanName;

    DateSeparator(String separator, String humanReadableName) {
        this.separator = separator;
        this.humanName = humanReadableName;
    }

    /**
     * @return the {@code String} associated with the enum. For example, {@code SLASH} returns "/"
     */
    public String getSeparator() {
        return separator;
    }

    /**
     * @return the human-readable form of this enum's name. For example, {@code SLASH} returns "Slash"
     */
    public String getHumanReadableName() {
        return humanName;
    }

    /**
     * Converts a {@code String} to a {@code DateSeparator} by comparing the input {@code String} to the
     * values in the enum. Purposefully defaults to {@link DateSeparator#SPACE} if input is invalid.
     */
    protected static DateSeparator fromString(String separator) {
        return switch (separator) {
            case "/" -> SLASH;
            case "." -> DOT;
            case "-" -> HYPHEN;
            default -> SPACE;
        };
    }

}
