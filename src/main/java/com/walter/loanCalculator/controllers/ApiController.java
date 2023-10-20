package com.walter.loanCalculator.controllers;

import com.walter.loanCalculator.models.ApiResponse;
import com.walter.loanCalculator.services.TaskProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@RestController
@Scope("request")
@CrossOrigin
@RequestMapping("/api")
@Slf4j
public class ApiController {


    @Autowired
    TaskProcessor taskProcessor;

    @RequestMapping(value = "/calculate", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> calculateLoan(HttpServletRequest request, @RequestBody HashMap<String, String> requestMap) {
        log.info("request {}",requestMap);
        ResponseEntity<?> res;
        ApiResponse apiResponse = new ApiResponse();
        try{
            apiResponse =taskProcessor.calculateLoan(requestMap);
        }
        catch (Exception e){
            e.printStackTrace();
            apiResponse.setResponseCode("01");
            apiResponse.setMessage("loan calculation hs failed");
        }
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    }
