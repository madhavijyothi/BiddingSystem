package com.craft.BiddingSystemTest.request;

import com.craft.biddingSystem.request.LotRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@SpringBootTest
public class LotRequestTest {
    Validator validator;

    @BeforeEach
    public void setup(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void validate_InvalidLotRequestData_ShouldHaveConstraintViolations(){
        LotRequest lotRequest = new LotRequest();
        lotRequest.setTitle("");
        lotRequest.setDescription("");
        lotRequest.setEndDate(LocalDateTime.now());
        lotRequest.setInitCost(BigDecimal.valueOf(-1));

        Set<ConstraintViolation<LotRequest>> violationSet = validator.validate(lotRequest);

        Assertions.assertTrue(violationSet.size() > 0);
    }

    @Test
    public void validate_BorderlineLotRequestData_ShouldNotHaveConstraintViolations(){
        LotRequest lotRequest = new LotRequest();
        lotRequest.setTitle("test");
        lotRequest.setDescription("test");
        lotRequest.setEndDate(LocalDateTime.now().plusMinutes(1));
        lotRequest.setInitCost(BigDecimal.valueOf(0));

        Set<ConstraintViolation<LotRequest>> violationSet = validator.validate(lotRequest);

        Assertions.assertEquals(0, violationSet.size());
    }

    @Test
    public void validate_CorrectLotRequestData_ShouldNotHaveConstraintViolations(){
        LotRequest lotRequest = new LotRequest();
        lotRequest.setTitle("test");
        lotRequest.setDescription("test");
        lotRequest.setEndDate(LocalDateTime.now().plusDays(1));
        lotRequest.setInitCost(BigDecimal.valueOf(10));

        Set<ConstraintViolation<LotRequest>> violationSet = validator.validate(lotRequest);

        Assertions.assertEquals(0, violationSet.size());
    }

}
