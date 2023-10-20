package com.walter.loanCalculator.models;

import lombok.Data;

@Data
public class EmailAlert {

    private String alertMessage;

    private String emailHeader;

    private String subject;

    private  String emailAddress;

    private String fileName;

    //Sample picture
    private String logoImage;
}
