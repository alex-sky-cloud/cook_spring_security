package com.email.web;

import com.email.dto.EmailRequestDto;
import com.email.dto.MailDto;
import com.email.dto.StatusResponse;
import com.email.service.EmailSenderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class MailRestController {

    private final EmailSenderService emailSenderService;

    @GetMapping("read/mail")
    public MailDto getMap(){

        Map<String, Object> propertiesMap = new HashMap<>();
        propertiesMap.put("name", "Developer Tom");
        propertiesMap.put("location", "United States");
        propertiesMap.put("sign", "Java Developer");
        propertiesMap.put("typeTemplate" , "newsletter-template");

        return MailDto
                .MailDtoBuilder
                .aMailDto()
                .mailTo("web.design.cloud@mail.ru")
                .mailFrom("may.cloud@mail.ru")
                .subject("Пример шаблона письма.")
                .propertiesMail(propertiesMap)
                .build();
    }

    @PostMapping("send/mail")
    public ResponseEntity<String> sendMail(@RequestBody MailDto dto){

        String typeTemplate = (String) dto.propertiesMail().get("typeTemplate");

        emailSenderService.sendEmail(dto, typeTemplate);

        return ResponseEntity.ok(StatusResponse.OK);
    }
}
