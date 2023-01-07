package com.email.springemail;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleOrderManagerSecond implements OrderManager{

    private final JavaMailSender mailSender;


    public void build() throws MessagingException {
        // of course you would use DI in any real-world cases
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("mail.host.com");

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setTo("test@host.com");
        helper.setText("Thank you for ordering!");

        sender.send(message);
    }

    public void makeAttachments() throws MessagingException {

        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("mail.host.com");

        MimeMessage message = sender.createMimeMessage();

// Используйте флаг - true , чтобы указать, что вам нужно multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("test@host.com");

        helper.setText("Check out this image!");

// Пример создания вложения в виде файла с расширением *.jpg
        FileSystemResource file = new FileSystemResource(new File("c:/Sample.jpg"));
        helper.addAttachment("CoolImage.jpg", file);

        sender.send(message);
    }


    public void addInlineResources() throws MessagingException {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost("mail.host.com");

        MimeMessage message = sender.createMimeMessage();

// Используйте флаг - true , чтобы указать, что вам нужно multipart message
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo("test@host.com");

// Используйте флаг - true , чтобы указать, что включаемый `text` является HTML
        helper.setText("<html><body><img src='cid:identifier1234'></body></html>", true);

// здесь указываем путь к изображению, которое нужно встроить в страницу
        FileSystemResource res = new FileSystemResource(new File("c:/Sample.jpg"));
        helper.addInline("identifier1234", res);

        sender.send(message);
    }

    @Override
    public void placeOrder(Order order) {

        MimeMessagePreparator messagePreparator = buildTemplateMessage(order);

        try {
            this.mailSender.send(messagePreparator);
        }
        catch (MailException ex) {
            // simply log it and go on...
            log.error(ex.getMessage());
        }

    }

    private MimeMessagePreparator buildTemplateMessage(Order order){

        return mimeMessage -> {

            mimeMessage.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(
                            order.customer().emailAddress()
                    )
            );
            mimeMessage.setFrom(new InternetAddress("mark7548@rambler.ru"));
            mimeMessage.setText("Dear " + order.customer().firstName() + " " +
                    order.customer().lastName() + ", thanks for your order. " +
                    "Your order number is " + order.id() + ".");
        };
    }
}
