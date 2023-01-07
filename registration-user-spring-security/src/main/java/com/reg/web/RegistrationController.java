package com.reg.web;

import com.reg.dto.UserDataDto;
import com.reg.exception.UserAlreadyExistException;
import com.reg.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/register")
    public String userReg(final Model model){
        UserDataDto user = new UserDataDto();

        model.addAttribute("userData", user);
        return "account/register";
    }

    @PostMapping("/register")
    public String userRegistration(final @Valid UserDataDto userData,
                                   final BindingResult bindingResult,
                                   final Model model){
        if(bindingResult.hasErrors()){
            model.addAttribute("registrationForm", userData);
            return "account/register";
        }
        try {
            userService.register(userData);
        }catch (UserAlreadyExistException e){
            bindingResult.rejectValue(
                    "email",
                    "userData.email",
                    "An account already exists for this email."
            );
            model.addAttribute("registrationForm", userData);
            return "account/register";
        }
        return "account/login";
    }
}
