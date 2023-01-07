package com.email.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class EmailRequestDto {

    private String fromMail;

    private String toMail;

    private String name;

    private String location;

    private String subject;

    private String sign;

    private String typeTemplate;
}