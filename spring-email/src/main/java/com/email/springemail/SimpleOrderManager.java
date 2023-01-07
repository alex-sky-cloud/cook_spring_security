package com.email.springemail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

//@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleOrderManager implements OrderManager {

    private final MailSender mailSender;
    private final SimpleMailMessage templateMessage;

    @Override
    public void placeOrder(Order order) {

        // Здесь выполняем обработку заказа...

        // Вызываем компоненты, которые позволят сохранить результаты обработки
        // в базу данных...

        // Создаем потоко-безопасную копию template message и настраиваем ее
        SimpleMailMessage simpleMailMessage = buildTemplateMessage(order);

        /*выполняем отправку сообщения*/
        try{
            this.mailSender.send(simpleMailMessage);
        }
        catch (MailException ex) {
            // simply log it and go on...
            log.error(ex.getMessage());
        }
    }

    private SimpleMailMessage buildTemplateMessage(Order order){

        SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);

        msg.setTo(order.customer().emailAddress());

        msg.setText(
                "Dear " + order.customer().firstName()
                        + order.customer().lastName()
                        + ", thank you for placing order. Your order number is "
                        + order.id()
        );

        return msg;
    }
}
