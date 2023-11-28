package com.craft.BiddingSystemTest.request;

import com.craft.biddingSystem.request.SignUpRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@SpringBootTest
public class SignUpRequestTest {

    Validator validator;

    @BeforeEach
    public void setup(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validate_InvalidSignUpData_ShouldHaveConstraintViolations(){
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("a@.");
        signUpRequest.setUsername("");
        signUpRequest.setPassword("1234");
        signUpRequest.setConfirmPassword("4321");

        Set<ConstraintViolation<SignUpRequest>> violationSet = validator.validate(signUpRequest);

        Assertions.assertTrue(violationSet.size() > 0);
    }

    @Test
    public void validate_BorderlineSignUpData_ShouldNotHaveConstraintViolations(){
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("a@aa.aa");
        signUpRequest.setUsername("u");
        signUpRequest.setPassword("12345678");
        signUpRequest.setConfirmPassword("12345678");

        Set<ConstraintViolation<SignUpRequest>> violationSet = validator.validate(signUpRequest);

        Assertions.assertEquals(0, violationSet.size());
    }

    @Test
    public void validate_CorrectSignUpData_ShouldNotHaveConstraintViolations(){
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("simple@email.com");
        signUpRequest.setUsername("user");
        signUpRequest.setPassword("password12");
        signUpRequest.setConfirmPassword("password12");

        Set<ConstraintViolation<SignUpRequest>> violationSet = validator.validate(signUpRequest);

        Assertions.assertEquals(0, violationSet.size());
    }

}
