package com.craft.biddingSystem.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

// Controller that handles all exceptions
@Controller
public class CustomErrorController implements ErrorController {

    Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model){

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        if (status != null) {
            Integer statusCode = Integer.valueOf(status.toString());
            logger.error("Error with status code: " + statusCode);
            model.addAttribute("error", statusCode);
        }

        return "error";
    }
}