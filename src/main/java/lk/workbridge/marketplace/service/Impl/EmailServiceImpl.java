package lk.workbridge.marketplace.service.Impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lk.workbridge.marketplace.service.EmailService;
import lk.workbridge.marketplace.service.VerificationCodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final VerificationCodeService service;


    @Override
    public boolean sendVerificationEmail(String userEmail) {
        try {

            String verificationCode = generateVerificationCode();


            service.saveVerificationCode(userEmail, verificationCode, 5);


            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(userEmail);
            helper.setSubject("Email Verification");


            String htmlContent = buildEmailContent(verificationCode);
            helper.setText(htmlContent, true);


            mailSender.send(message);
            return true;

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String generateVerificationCode() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    private String buildEmailContent(String code) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<body>" +
                "<h2>Email Verification</h2>" +
                "<p>Your verification code is:</p>" +
                "<h1 style='color: #4CAF50; font-size: 32px;'>" + code + "</h1>" +
                "<p>This code will expire in 5 minutes.</p>" +
                "<p>If you didn't request this, please ignore this email.</p>" +
                "</body>" +
                "</html>";
    }
}
