package com.craft.biddingSystem.controllers;

import com.craft.biddingSystem.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// Controller that handles custom exceptions
@ControllerAdvice
public class GlobalExceptionHandlerController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler({BetCreateException.class, BetDeleteException.class, LotCreateException.class,
            LotDeleteException.class, WinningBetCreateException.class})
    public String handle400CustomExceptions(Exception ex, Model model){
        logger.error(ex.getMessage());
        model.addAttribute("error", 400);
        return "error";
    }

    @ExceptionHandler({BetNotFoundException.class, LotNotFoundException.class,
            UserNotFoundException.class})
    public String handle404CustomExceptions(Exception ex, Model model){
        logger.error(ex.getMessage());
        model.addAttribute("error", 404);
        return "error";
    }
}