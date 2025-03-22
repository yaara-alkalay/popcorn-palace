package com.att.tdp.popcorn_palace.validation;

import com.att.tdp.popcorn_palace.model.Showtime;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

// This method is invoked to validate a Showtime object
public class ShowtimeValidator implements ConstraintValidator<ValidShowtime, Showtime> {
    @Override
    public boolean isValid(Showtime showtime, ConstraintValidatorContext context) {
        if (showtime == null) {
            return true; // Let @NotNull handle null cases
        }
        if (showtime.getStartTime() == null || showtime.getEndTime() == null) {
            return false;
        }
        return showtime.getStartTime().isBefore(showtime.getEndTime());
    }
}
