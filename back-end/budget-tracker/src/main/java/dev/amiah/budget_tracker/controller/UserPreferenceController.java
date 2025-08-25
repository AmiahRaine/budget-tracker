package dev.amiah.budget_tracker.controller;

import dev.amiah.budget_tracker.assembler.UserPreferenceModelAssembler;
import dev.amiah.budget_tracker.exception.UserPreferenceNotFoundException;
import dev.amiah.budget_tracker.model.UserPreference;
import dev.amiah.budget_tracker.repository.UserPreferenceRepository;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class UserPreferenceController {

    private final UserPreferenceRepository repository;
    private final UserPreferenceModelAssembler assembler;

    public UserPreferenceController(UserPreferenceRepository repository, UserPreferenceModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    /**
     * @return All {@code UserPreference}s in the database.
     */
    @GetMapping("/api/user-preference")
    public CollectionModel<EntityModel<UserPreference>> all() {
        // Add links to each pref and link to aggregate root (which happens to be this method)
        List<EntityModel<UserPreference>> prefs = repository.findAll().stream().map(assembler::toModel).toList();

        return CollectionModel.of(prefs, linkTo(methodOn(UserPreferenceController.class).all()).withRel("userPreferences"));
    }

    /**
     * Saves a new {@code UserPreference} to the database.
     *
     * @param pref The new {@code UserPreference} to save.
     * @return A response with the saved {@code UserPreference}.
     */
    @PostMapping("/api/user-preference")
    public ResponseEntity<?> insert(@RequestBody UserPreference pref) {
        EntityModel<UserPreference> entityModel = assembler.toModel(repository.save(pref));

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    /**
     * Finds the specified {@code UserPreference} with a matching {@code id}
     * and returns it.
     *
     * @param id The {@code id} of the {@code UserPreference} to find.
     * @return The found {@code UserPreference}.
     */
    @GetMapping("/api/user-preference/{id}")
    public EntityModel<UserPreference> one(@PathVariable Long id) {
        UserPreference pref = repository.findById(id).orElseThrow(() -> new UserPreferenceNotFoundException(id));

        return assembler.toModel(pref);
    }

    /**
     * Updates the data of a {@code UserPreference} with the specified {@code id}. If
     * a {@code UserPreference} with that {@code id} is not found, it will insert the
     * {@code UserPreference} instead.
     *
     * @param pref The new {@code UserPreference} data to save.
     * @param id The {@code id} of the {@code UserPreference} to be updated.
     * @return A response with the updated {@code UserPreference}, or a newly inserted one if no existing entry matched the {@code id}.
     */
    @PutMapping("/api/user-preference/{id}")
    public ResponseEntity<?> update(@RequestBody UserPreference pref, @PathVariable Long id) {
        UserPreference updatedPref = repository.findById(id)
                .map(dbPref -> {
                    dbPref.setDatePattern(pref.getDatePattern());
                    dbPref.setDateSeparator(pref.getDateSeparator());
                    return repository.save(dbPref);
                }).orElseGet(() -> repository.save(pref));

        EntityModel<UserPreference> entityModel = assembler.toModel(updatedPref);

        return ResponseEntity.created(entityModel.getRequiredLink(IanaLinkRelations.SELF).toUri()).body(entityModel);
    }

    /**
     * Deletes the {@code UserPreference} with the provided {@code id}.
     *
     * @return An HTTP 204 no content response.
     */
    @DeleteMapping("/api/user-preference/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        repository.deleteById(id);

        return ResponseEntity.noContent().build();
    }

}
