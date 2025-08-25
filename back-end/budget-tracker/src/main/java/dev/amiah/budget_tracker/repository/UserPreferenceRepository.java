package dev.amiah.budget_tracker.repository;

import dev.amiah.budget_tracker.model.UserPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPreferenceRepository extends JpaRepository<UserPreference, Long> {
}
