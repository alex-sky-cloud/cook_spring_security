package com.security.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@ToString
@NoArgsConstructor
@Setter
@Getter
public class SampleInputs {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "Please provide a date.")
    private Date dateField;

    @NotNull(message = "Please provide a value")
    private Double doubleField;

    @NotNull(message = "Please provide a value")
    private Integer numberField;

    @NotBlank(message = "This value must not be empty.")
    private String textField;

    @NotBlank(message = "This value must not be empty.")
    private String colorField;

    @NotNull(message = "Please provide a date.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date dateTimeField;
}
