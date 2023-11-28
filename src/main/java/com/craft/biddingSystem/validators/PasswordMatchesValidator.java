package com.craft.biddingSystem.validators;


import com.craft.biddingSystem.annotations.PasswordMatches;
import com.craft.biddingSystem.request.SignUpRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

// Validator checks if the confirm password is the same as the password
public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {
   @Override
   public void initialize(PasswordMatches constraint) {
   }

   @Override
   public boolean isValid(Object obj, ConstraintValidatorContext context) {
      SignUpRequest signUpRequest = (SignUpRequest) obj;
      return signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword());
   }
}
