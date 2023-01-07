package com.email.dto;

import java.util.List;
import java.util.Map;

public record MailDto(

        String mailFrom,
        String mailTo,
        String subject,

        List<Object> attachments,

        Map<String, Object> propertiesMail) {


    public static final class MailDtoBuilder {
        private String mailFrom;
        private String mailTo;
        private String subject;
        private List<Object> attachments;
        private Map<String, Object> propertiesMail;

        private MailDtoBuilder() {
        }

        public static MailDtoBuilder aMailDto() {
            return new MailDtoBuilder();
        }

        public MailDtoBuilder mailFrom(String mailFrom) {
            this.mailFrom = mailFrom;
            return this;
        }

        public MailDtoBuilder mailTo(String mailTo) {
            this.mailTo = mailTo;
            return this;
        }

        public MailDtoBuilder subject(String subject) {
            this.subject = subject;
            return this;
        }

        public MailDtoBuilder attachments(List<Object> attachments) {
            this.attachments = attachments;
            return this;
        }

        public MailDtoBuilder propertiesMail(Map<String, Object> propertiesMail) {
            this.propertiesMail = propertiesMail;
            return this;
        }

        public MailDto build() {
            return new MailDto(mailFrom, mailTo, subject, attachments, propertiesMail);
        }
    }
}