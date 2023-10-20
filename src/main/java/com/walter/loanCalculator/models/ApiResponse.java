package com.walter.loanCalculator.models;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApiResponse {
    private String message;
    private String narration;

    private String responseDescription;
    private String responseCode;
    private Object responseBody;


    private Map<String, String> responseData = new HashMap<>();



    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNarration() {
        return narration;
    }

    public void setNarration(String narration) {
        this.narration = narration;
    }

    public String getResponseDescription() {
        return responseDescription;
    }

    public void setResponseDescription(String responseDescription) {
        this.responseDescription = responseDescription;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }


    public Map<String, String> getResponseData() {
        return responseData;
    }

    public void setResponseData(Map<String, String> responseData) {
        this.responseData = responseData;
    }

    @Override
    public String toString() {
        return "ApiResponse{" + "message:" + message + ", narration:" + narration + ", responseDescription:" + responseDescription +", responseCode :" + responseCode +  ", responseBody : " + responseBody +  '}';
    }
}