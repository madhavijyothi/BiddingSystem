package com.craft.BiddingSystemTest.validators;

import com.craft.biddingSystem.request.SignUpRequest;
import com.craft.biddingSystem.validators.PasswordMatchesValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PasswordMatchesValidatorTest {

    PasswordMatchesValidator passwordMatchesValiator = new PasswordMatchesValidator();

    @Test
    public void isValid_SamePasswords_ShouldReturnTrue(){
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("");
        signUpRequest.setUsername("");
        signUpRequest.setPassword("12345");
        signUpRequest.setConfirmPassword("134");

        Assertions.assertFalse(passwordMatchesValiator.isValid(signUpRequest, null));
    }

    @Test
    public void isValid_NotSamePasswords_ShouldReturnFalse(){
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setEmail("");
        signUpRequest.setUsername("");
        signUpRequest.setPassword("12345");
        signUpRequest.setConfirmPassword("12345");

        Assertions.assertTrue(passwordMatchesValiator.isValid(signUpRequest, null));
    }

}
