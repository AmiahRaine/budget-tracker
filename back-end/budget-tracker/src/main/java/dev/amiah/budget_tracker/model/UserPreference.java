package dev.amiah.budget_tracker.model;

import dev.amiah.budget_tracker.model.data_type.DatePattern;
import dev.amiah.budget_tracker.model.data_type.DateSeparator;
import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name = "user_preference")
public class UserPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "preference_id")
    private Long id;

    // Using length of 3 since the separator is being stored directly in the table. Not using 1 so there is a bit of wiggle room.
    @Column(name = "date_separator", nullable = false, length = 3)
    @Enumerated(EnumType.STRING)
    private DateSeparator dateSeparator = DateSeparator.SPACE;

    @Column(name = "date_pattern", nullable = false)
    @Enumerated(EnumType.STRING)
    private DatePattern datePattern = DatePattern.MONTH_DAY_YEAR;

    @Transient
    private String datePatternComplete;

    public UserPreference() {}

    @PostLoad
    @PostPersist
    @PostUpdate
    public void calculateCompleteDatePattern() {
        if (datePattern.allowsSeparators()) {
            datePatternComplete = datePattern.getPattern().replace(" ", dateSeparator.getSeparator());
        }
        else {
            datePatternComplete = datePattern.getPattern();
        }
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setDateSeparator(DateSeparator dateSeparator) {
        this.dateSeparator = dateSeparator;
    }

    public void setDatePattern(DatePattern datePattern) {
        this.datePattern = datePattern;
    }

    // Getters

    public Long getId() {
        return id;
    }

    /**
     * Gets the {@link DateSeparator} preference selected by this user. To get this
     * user's complete date pattern, use {@link #getDatePatternComplete()}
     * instead.
     *
     * @return This user's {@link DateSeparator} preference.
     * @see #getDatePatternComplete()
     */
    public DateSeparator getDateSeparator() {
        return dateSeparator;
    }

    /**
     * Gets the {@link DatePattern} preference selected by this user. To get this
     * user's complete date pattern, use {@link #getDatePatternComplete()}
     * instead.
     *
     * @return This user's {@link DatePattern} preference.
     * @see #getDatePatternComplete()
     */
    public DatePattern getDatePattern() {
        return datePattern;
    }

    /**
     * Gets this user's {@link DatePattern} with their preferred {@link DateSeparator}
     * added in. The complete date pattern is automatically updated whenever {@link DatePattern}
     * or {@link DateSeparator} is changed and is not persisted in the database.
     *
     * @return This user's complete date preference.
     * @see #getDatePattern()
     * @see #getDateSeparator()
     */
    public String getDatePatternComplete() {
        return datePatternComplete;
    }

    @Override
    public String toString() {
        return "UserPreference{" +
                "id=" + id +
                ", dateSeparator=" + dateSeparator +
                ", datePattern=" + datePattern +
                ", datePatternComplete='" + datePatternComplete + '\'' +
                '}';
    }
}
