package com.example.web.withoutannotation;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class InfoController {

    @RequestMapping( "info/home")
    public String getAbout() {
        return "home";
    }

/*    @GetMapping ( "register")
    public String getRegister(final @Valid UserDataDto userData,
                              final BindingResult bindingResult,
                              final Model model) {
        return "register";
    }*/
}
