package com.walter.loanCalculator.exceptions;

import com.walter.loanCalculator.models.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.MessagingException;
import java.io.IOException;


@ControllerAdvice
public class ExceptionsTranslater {
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ResponseWrapper> messageFailure(MessagingException ex) {
        System.out.println("Message error:"+ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(400);
        response.setMessage("Failed to send message");
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(IOException.class)
    public ResponseEntity<ResponseWrapper> IOFailure(IOException ex) {
        System.out.println("data input/output error:"+ex);
        ResponseWrapper response = new ResponseWrapper();
        response.setCode(400);
        response.setMessage("Failed to send message");
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
    }
}