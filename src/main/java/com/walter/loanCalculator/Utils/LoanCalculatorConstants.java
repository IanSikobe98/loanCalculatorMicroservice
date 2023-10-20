package com.walter.loanCalculator.Utils;

public class LoanCalculatorConstants {

    public static final String ANNUALLY = "1";

    public static final String SEMI_ANNUALY= "2";

    public static final String QUARTERLY= "3";

    public static final String MONTHLY= "4";

    public static final String WEEKLY= "5";

    public static final String BIWEEKLY= "6";

    public static final String REDUCING_BALANCE = "1";

    public static final String FLAT_RATE ="2";


    public enum ApiResponseCodes {
        OK("00"),
        GENERAL_ERROR("01");
        private final String code;
        ApiResponseCodes(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }
}
