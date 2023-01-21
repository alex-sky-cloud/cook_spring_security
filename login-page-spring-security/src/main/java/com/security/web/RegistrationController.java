package com.security.web;

import com.security.dto.UserDataDto;
import com.security.exception.UserAlreadyExistException;
import com.security.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;


 /*   @GetMapping("/handleLoginErrors")
    ModelAndView handleErrors(ModelAndView modelAndView){

        modelAndView.setViewName("account/login");

        return modelAndView;

    }*/

    @GetMapping("/register")
    public String showRegistrationForm(final Model model){
        UserDataDto user = new UserDataDto();

        model.addAttribute("userData", user);
        return "account/register";
    }

    @PostMapping("/register")
    public String userRegistration(final @Valid UserDataDto userData,
                                   final BindingResult bindingResult,
                                   final Model model){
        if(bindingResult.hasErrors()){
            return "account/register";
        }
        try {
            userService.register(userData);
        }catch (UserAlreadyExistException e){
            /*ошибки не отображаются*/
            bindingResult.rejectValue(
                    "email",
                    "userData.email",
                    "An account already exists for this email."
            );
            model.addAttribute("formreg", userData);
            return "account/register";
        }
        return "account/register";
    }
}
