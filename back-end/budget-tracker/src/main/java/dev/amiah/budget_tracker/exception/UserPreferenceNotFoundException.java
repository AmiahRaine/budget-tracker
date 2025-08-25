package dev.amiah.budget_tracker.exception;

public class UserPreferenceNotFoundException extends RuntimeException {

    public UserPreferenceNotFoundException(Long id) {
        super("Could not find user preferences with id " + id);
    }

}
