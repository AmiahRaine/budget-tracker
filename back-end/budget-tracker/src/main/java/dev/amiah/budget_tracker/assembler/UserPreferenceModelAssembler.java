package dev.amiah.budget_tracker.assembler;


import dev.amiah.budget_tracker.controller.UserPreferenceController;
import dev.amiah.budget_tracker.model.UserPreference;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserPreferenceModelAssembler implements RepresentationModelAssembler<UserPreference, EntityModel<UserPreference>> {

    @Override
    public EntityModel<UserPreference> toModel(UserPreference pref) {

        return EntityModel.of(pref,
                linkTo(methodOn(UserPreferenceController.class).one(pref.getId())).withSelfRel(),
                linkTo(methodOn(UserPreferenceController.class).all()).withRel("userPreferences"));
    }
}
