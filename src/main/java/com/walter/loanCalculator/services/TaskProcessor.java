package com.walter.loanCalculator.services;

import com.walter.loanCalculator.models.ApiResponse;

import java.util.HashMap;

public interface TaskProcessor {

    ApiResponse calculateLoan(HashMap<String,String> requestMap);
}
