package com.walter.loanCalculator.services;

import com.walter.loanCalculator.models.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static com.walter.loanCalculator.Utils.LoanCalculatorConstants.*;

@Service
@Slf4j
public class TaskProcessorImpl  implements  TaskProcessor{

    @Autowired
    Environment environment;
    @Override
    public ApiResponse calculateLoan(HashMap<String,String> requestMap){
        ApiResponse apiResponse = new ApiResponse();
        try {
            HashMap<String,String> mathParams = new HashMap<>();
            Double periodInstallments = 0.0;
            Integer noOfPayments = 0;
            Double loanAmount = Double.valueOf(requestMap.getOrDefault("loanAmount","0"));
            String paymentFrequency = requestMap.getOrDefault("paymentFrequency","");
            Double loanPeriod = Double.valueOf(requestMap.getOrDefault("loanPeriod","0"));
            String startDate = requestMap.getOrDefault("startDate","");
            String interestRateType = requestMap.getOrDefault("interestRateType","");
            Double interestRate = Double.valueOf(requestMap.getOrDefault("interestRate","0"));

            mathParams =calculatePeriodInterest(interestRate,paymentFrequency,loanPeriod);
            periodInstallments =calculatePeriodInstallment(interestRateType,loanAmount,interestRate,mathParams,loanPeriod);
            noOfPayments = Integer.valueOf(mathParams.getOrDefault("noOfPayments","0"));


            JSONArray jsonArray = new JSONArray();
            Double remainingBalance = loanAmount;
            Double interestPayment = 0.0;
            for(int i=0 ; i<noOfPayments;i++){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("paymentNumber",String.valueOf(i+1));
                jsonObject.put("periodInstallment",String.valueOf(periodInstallments));
                interestPayment = calculateInterestPayment(loanAmount,mathParams);
                jsonObject.put("interestPayment",String.valueOf(interestPayment));
                mathParams = calculatePrincipalPaymentAndRemainingBalance(interestRateType,periodInstallments,interestPayment,remainingBalance,mathParams);
                jsonObject.put("principalPayment",mathParams.get("principalPayment"));
                jsonObject.put("remainingBalance",mathParams.get("remainingBalance"));
                remainingBalance = Double.valueOf(mathParams.get("remainingBalance"));
                jsonArray.put(jsonObject);

            }
            apiResponse.setResponseCode("00");
            apiResponse.setResponseBody(String.valueOf(jsonArray));
            apiResponse.setMessage("TEst");
            log.info("Successful loan application");

        }
        catch (Exception e ){
            e.printStackTrace();
            apiResponse.setResponseCode("01");
            apiResponse.setMessage("loan calculation hs failed");
        }
        return apiResponse;
    }


    private HashMap<String,String> calculatePeriodInterest(Double interestRate,String paymentFrequency,Double loanTerm){
        HashMap<String ,String> mathParams = new HashMap<>();
        Double periodInterest =  0.0;
        long noOfPayments = 0;
        switch (paymentFrequency){
            case ANNUALLY:
                periodInterest= interestRate/1;
                noOfPayments =Math.round(loanTerm * 1);
                mathParams.put("paymentFrequency",String.valueOf(1));
                break;
            case SEMI_ANNUALY:
                periodInterest= interestRate/2;
                noOfPayments =Math.round(loanTerm * 2);
                mathParams.put("paymentFrequency",String.valueOf(2));
                break;

            case QUARTERLY:
                periodInterest= interestRate/4;
                noOfPayments =Math.round(loanTerm * 4);
                mathParams.put("paymentFrequency",String.valueOf(4));
                break;

            case MONTHLY:
                periodInterest= interestRate/12;
                noOfPayments =Math.round(loanTerm * 12);
                mathParams.put("paymentFrequency",String.valueOf(12));
                break;

            case BIWEEKLY:
                periodInterest= interestRate/26;
                noOfPayments =Math.round(loanTerm * 26);
                mathParams.put("paymentFrequency",String.valueOf(26));
                break;

            case WEEKLY:
                periodInterest= interestRate/52;
                noOfPayments =Math.round(loanTerm * 52);
                mathParams.put("paymentFrequency",String.valueOf(52));
                break;
        }

        mathParams.put("periodInterest", String.valueOf(periodInterest));
        mathParams.put("noOfPayments", String.valueOf(noOfPayments));

        return mathParams;
    }


    private Double calculatePeriodInstallment(String interestRateType,Double loanAmount,Double interestRate,HashMap<String,String> mathParams,Double loanTerm){
        Double installments = 0.0;
        Double periodInterest = Double.valueOf(mathParams.getOrDefault("periodInterest","0"));
        Double noOfPayments = Double.valueOf(mathParams.getOrDefault("noOfPayments","0"));
        Double paymentFrequency = Double.valueOf(mathParams.getOrDefault("paymentFrequency","0"));

        switch (interestRateType){
            case REDUCING_BALANCE:
                //formula : [P × r(1 + r)^n] / [(1 + r)^n – 1]
                installments = 1+ periodInterest;
                installments = Math.pow(installments,noOfPayments);
                installments = installments-1;
                Double numeratorCalc = 1+periodInterest;
                numeratorCalc = Math.pow(numeratorCalc,noOfPayments);
                numeratorCalc = periodInterest* numeratorCalc;
                installments = numeratorCalc * loanAmount;
                installments = numeratorCalc/installments;

                break;
            case FLAT_RATE:
                //Formula :  (Loan Amount + Total Interest) / Number of Payments
                //Total Interest = Loan Amount * Annual Interest Rate * (Loan Term / Payment Frequency)
                Double totalInterest = loanAmount*interestRate*(noOfPayments/paymentFrequency);
                installments = (loanAmount+totalInterest) / noOfPayments;
        }
        return installments;
    }


    private Double calculateInterestPayment(Double remainingBal,HashMap<String,String> mathParams){
        Double interestPayment = 0.0;
        Double periodInterest = Double.valueOf(mathParams.getOrDefault("periodInterest","0"));
        interestPayment = remainingBal * periodInterest;
        return  interestPayment;
    }

    private HashMap<String,String> calculatePrincipalPaymentAndRemainingBalance(String interestRateType,Double periodInstallments,Double interestPayment,Double remainingBalance,HashMap<String ,String> mathParams){
        Double principalPayment = 0.0;
                switch(interestRateType){
                    case REDUCING_BALANCE:
                        principalPayment =periodInstallments- interestPayment;
                        break;
                }

                remainingBalance = remainingBalance -principalPayment;
                mathParams.put("principalPayment", String.valueOf(principalPayment));
                mathParams.put("remainingBalance", String.valueOf(remainingBalance));
                return mathParams;
    }
}
