package com.craft.biddingSystem.validators;


import com.craft.biddingSystem.annotations.ValidEmail;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

// Validator checks the correctness of e-mail
public class EmailValidator implements ConstraintValidator<ValidEmail, String> {
   private static final String EMAIL_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
           + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

   @Override
   public void initialize(ValidEmail constraint) {
   }

   @Override
   public boolean isValid(String email, ConstraintValidatorContext context) {
      return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
   }
}
