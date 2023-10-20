package com.walter.loanCalculator.controllers;

import com.walter.loanCalculator.models.ApiResponse;
import com.walter.loanCalculator.models.EmailAlert;
import com.walter.loanCalculator.models.ResponseWrapper;
import com.walter.loanCalculator.services.MailService;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.walter.loanCalculator.models.UserData;


@RequestMapping("/email")
@RestController
@Scope("request")
@CrossOrigin
public class EmailController {

   private final MailService mailService;

    public EmailController(MailService mailService) {
        this.mailService = mailService;
    }


    @RequestMapping(method = RequestMethod.POST ,value = "/sendemail")
    public ResponseEntity<?> sendEmail(@RequestBody UserData userData){
        ApiResponse response = new ApiResponse();
        ResponseEntity<?> res;
        try {
            EmailAlert emailAlert = new EmailAlert();
            emailAlert = mailService.constructEmailObj(userData);
            mailService.sendEmail(emailAlert);
            response.setMessage("Send Email was Successful");
            response.setResponseCode("00");
        }

        catch (Exception e){
            e.printStackTrace();
            response.setResponseCode("01");
            response.setMessage("Send Email has failed");
            e.printStackTrace();
        }
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}

