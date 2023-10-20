package com.walter.loanCalculator.services;



import com.walter.loanCalculator.models.EmailAlert;
import com.walter.loanCalculator.models.UserData;

import java.io.UnsupportedEncodingException;

public interface MailService {

    EmailAlert constructEmailObj(UserData userData);
    void sendMail(String to, String subject, String text) throws Exception;
    void sendEmail(EmailAlert order) throws UnsupportedEncodingException;
}
