package com.company.service;


import com.company.dto.EmailRequestDTO;
import com.company.entity.EmailHistoryEntity;
import com.company.repository.MailHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class MailService {

    @Autowired
    MailHistoryRepository emailHistoryRepository;
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String fromAccount;

    public void sendRegistrationEmail(String toAccount,Integer id) {
//        String message = "Your Activation lin: adsdasdasdasda";
//        sendSimpleEmail(toAccount, "Registration", message);
        StringBuilder builder = new StringBuilder();
        builder.append("<h1 style='align-text:center'>Salom Jiga Qalaysan</h1>");
        builder.append("<b>Mazgi</b>");
        builder.append("<p> <a href='http://localhost:8081/attach/open/123'>Link verification</a> </p>");
        sendEmail(toAccount, "Registration", builder.toString());

    }

    private void sendEmail(String toAccount, String subject, String text) {
        try {
            MimeMessage msg = javaMailSender.createMimeMessage();
            msg.setFrom(fromAccount);
            MimeMessageHelper helper = new MimeMessageHelper(msg, true);
            helper.setTo(toAccount);
            helper.setSubject(subject);
            helper.setText(text, true);
            javaMailSender.send(msg);
            EmailHistoryEntity entity=new EmailHistoryEntity();
            entity.setEmail(toAccount);

            emailHistoryRepository.save(entity);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public EmailRequestDTO reSendEmail(String toAccount,Integer id){

        Long allByEmail = emailHistoryRepository.countResend(toAccount);

        EmailRequestDTO dto=new EmailRequestDTO();

        if(allByEmail>=4){
            dto.setStatus(-1);
            dto.setMessage("4 martadan ko'p resend bo'ldi");
            return dto;
        }

        sendRegistrationEmail(toAccount,id);
        dto.setMessage("Succesfully");
        dto.setStatus(1);
        return dto;
    }

    private void sendSimpleEmail(String toAccount, String subject, String text) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(toAccount);
        msg.setSubject(subject);
        msg.setText(text);
        msg.setFrom(fromAccount);
        javaMailSender.send(msg);
    }
}
