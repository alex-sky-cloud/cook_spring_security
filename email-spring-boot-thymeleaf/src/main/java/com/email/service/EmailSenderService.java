package com.email.service;

import com.email.dto.MailDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private final JavaMailSender emailSender;

    private final SpringTemplateEngine templateEngine;

    /**
     *
     * @param mailDto - данные, которые нужны для заполнения шаблона и заполнения служебных данных,
     *             для отправки письма
     * @param nameTemplate - имя шаблона, который расположен в /resources/templates
     *                     (например - "newsletter-template")
     */
    public void sendEmail (MailDto mailDto, String nameTemplate)  {

        MimeMessage message = emailSender.createMimeMessage();


        MimeMessageHelper helper;
        try {
            helper = new MimeMessageHelper(message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name ());

            ClassPathResource attachment = new ClassPathResource("static/pdf/practice-react-programming.pdf");
            ClassPathResource image = new ClassPathResource("static/img/auth.png");

            helper.addAttachment("reactive-programming-book", attachment);

            helper.setTo(mailDto.mailTo()); /*адрес получателя*/
            helper.setSubject(mailDto.subject()); /*тема письма*/
            /*важно указать тот же адрес отправителя, который у вас указан в application.yml*/
            helper.setFrom(mailDto.mailFrom());

            Context context = new Context();

            /*данные для заполнения шаблона письма. Используется Srping EL*/
            context.setVariables(mailDto.propertiesMail());

            String html = templateEngine.process(nameTemplate, context);
            helper.setText(html, true);

            /*Это обязательно нужно установить после вызова метода setText()*/
            helper.addInline("auth", image);
        } catch (MessagingException e) {
            log.error(e.getLocalizedMessage());
          //  throw new RuntimeException(e);
        }

        emailSender.send(message);
    }
}
