package com.security.web;

import com.security.dto.SampleInputs;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class SampleController {

    @PostMapping("/sampleInputs")
    public String saveInputs(@Valid SampleInputs sampleInputs, BindingResult bindingResult) {

       if (bindingResult.hasErrors()){

           log.error(bindingResult.getFieldErrors().toString());
           bindingResult.rejectValue(
                   "textField",
                   "sampleInputs.textField",
                   "Поле не может быть пустым."
           );

           return "account/sampleInputs";
       }

        log.info("command object = {}", sampleInputs);

        return "account/sampleInputs";
    }


    @GetMapping("/sampleInputs")
    public String sampleInputs(Model model) {
        model.addAttribute("sampleInputs", new SampleInputs());
        return "account/sampleInputs";
    }

}
