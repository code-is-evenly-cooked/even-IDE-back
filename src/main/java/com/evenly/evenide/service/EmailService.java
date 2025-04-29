package com.evenly.evenide.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendResetPasswordEmail(String to, String resetUrl){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[even ide] 비밀번호 재설정 링크 입니다.");
        message.setText("""
                안녕하세요. even ide 입니다.
                
                아래 링크를 클릭하시면 비밀번호를 재설정 하실 수 있습니다.
                %s
                해당 링크는 2시간 동안만 유효합니다.
                감사합니다!
                """.formatted(resetUrl));
        mailSender.send(message);
    }
}
