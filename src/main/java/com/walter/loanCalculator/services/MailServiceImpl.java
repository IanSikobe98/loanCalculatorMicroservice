package com.walter.loanCalculator.services;


import com.walter.loanCalculator.models.EmailAlert;
import com.walter.loanCalculator.models.UserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.concurrent.ForkJoinPool;

@Service
@Slf4j
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final Environment environment;



    @Autowired
    public MailServiceImpl(Environment environment, JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.environment = environment;
    }

    @Override
    public EmailAlert constructEmailObj(UserData userData){
        log.info("User Data is:{}",userData);
        String message = userData.getEmailText();

        log.info("Message is: {}",message);
        EmailAlert emailAlert = new EmailAlert();
        emailAlert.setEmailHeader(environment.getRequiredProperty("ishlaw.email.header"));
        emailAlert.setSubject(environment.getRequiredProperty("ishlaw.email.subject"));
        emailAlert.setFileName(environment.getRequiredProperty("ishlaw.email.fileName"));
        emailAlert.setLogoImage(environment.getRequiredProperty("ishlaw.email.image"));
        emailAlert.setAlertMessage(message);
        emailAlert.setEmailAddress(userData.getEmailAddress());
        return emailAlert;
    }

    @Override
    public void sendMail(String to, String subject, String text) throws Exception {
        log.info("Sending email to: [{}], subject: [{}], text: [{}] ", to, subject, text);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(environment.getProperty("spring.mail.username", "info@tangazoletu.com"));
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        ForkJoinPool.commonPool().execute(() -> {
            try {
                mailSender.send(message);
                log.info("Mail sent successfully ...");
            } catch (Exception e) {
                log.error("Error sending email : {}", e.getMessage());
                e.printStackTrace();
            }
        });
    }
//
    @Override
    public void sendEmail(EmailAlert order)  {

        MimeMessagePreparator preparator = getMessagePreparator(order);
        ForkJoinPool.commonPool().execute(() -> {
            try {
                log.info("Sending Mail .........");
                mailSender.send(preparator);
                log.info("Message Sent...Hurrey!!!");
            } catch (Exception ex) {
                log.info("Send Mail Exception here " + ex.getMessage());
            }
        });
    }


    private MimeMessagePreparator getMessagePreparator(final EmailAlert tokenObject)  {
        log.info("Sending email to: [{}], subject: [{}], text: [{}] ", tokenObject.getEmailAddress(),tokenObject.getSubject(),tokenObject.getAlertMessage());
        MimeMessagePreparator preparator = (MimeMessage mimeMessage) -> {
            mimeMessage.setFrom(new InternetAddress(environment.getRequiredProperty("spring.mail.username"),environment.getRequiredProperty("ishlaw.email.source")));
            mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(tokenObject.getEmailAddress()));
            mimeMessage.setHeader("", tokenObject.getEmailHeader());
            mimeMessage.setText(""+tokenObject.getAlertMessage()+"","US-ASCII","html");
            mimeMessage.setSubject("" + tokenObject.getSubject() + "");


            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(tokenObject.getAlertMessage(), "text/html; charset=UTF-8");

            Multipart multiPart = new MimeMultipart("alternative");

            // create a new imagePart and add it to multipart so that the image is inline attached in the email
            byte[] rawImage = Base64.getDecoder().decode(tokenObject.getLogoImage());
            BodyPart imagePart = new MimeBodyPart();
            ByteArrayDataSource imageDataSource = new ByteArrayDataSource(rawImage,"image/png");

            imagePart.setDataHandler(new DataHandler(imageDataSource));
            imagePart.setHeader("Content-ID", "<qrImage>");
            imagePart.setFileName(tokenObject.getFileName()+".png");

            multiPart.addBodyPart(imagePart);
            multiPart.addBodyPart(htmlPart);

            mimeMessage.setContent(multiPart);
        };
        return preparator;
    }
}
