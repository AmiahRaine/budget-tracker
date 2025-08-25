package dev.amiah.budget_tracker.util;

import java.util.function.Consumer;

public class ControllerUtil {

    /**
     * Calls a method with one parameter only if provided argument is not {@code null}. If the object is {@code null},
     * then the method is not called.
     *
     * @param thing The object to check if {@code null}.
     * @param setter The method to input {@code thing} into if it's not {@code null}.
     * @param <T> The type of the object to check.
     */
    public static <T> void setIfNotNull(T thing, Consumer<T> setter) {
        if (thing != null) {
            setter.accept(thing);
        }
    }

}
