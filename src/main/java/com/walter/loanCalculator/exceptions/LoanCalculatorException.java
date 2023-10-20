package com.walter.loanCalculator.exceptions;
import com.walter.loanCalculator.Utils.LoanCalculatorConstants;
import org.springframework.http.HttpStatus;

public class LoanCalculatorException extends Exception {
    LoanCalculatorConstants.ApiResponseCodes responseCode;
    final String responseMessage;
    final HttpStatus httpStatus;

    public LoanCalculatorException(String message) {
        super(message);
        this.responseCode = LoanCalculatorConstants.ApiResponseCodes.GENERAL_ERROR;
        this.responseMessage = message;
        this.httpStatus = HttpStatus.EXPECTATION_FAILED;
    }

    public LoanCalculatorException(String message, LoanCalculatorConstants.ApiResponseCodes responseCode) {
        super(message);
        this.responseCode = responseCode;
        this.responseMessage = message;
        this.httpStatus = HttpStatus.EXPECTATION_FAILED;
    }

    public LoanCalculatorException(String message, LoanCalculatorConstants.ApiResponseCodes responseCode, String responseMessage) {
        super(message);
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.httpStatus = HttpStatus.EXPECTATION_FAILED;
    }

    public LoanCalculatorException(String message, LoanCalculatorConstants.ApiResponseCodes responseCode, String responseMessage, HttpStatus httpStatus) {
        super(message);
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
        this.httpStatus = httpStatus;
    }

    public LoanCalculatorConstants.ApiResponseCodes getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
