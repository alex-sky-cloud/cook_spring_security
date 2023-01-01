package com.security.dao.web.controller.register;


import com.security.dao.dto.UserDto;
import com.security.dao.exception.UserAlreadyExistException;
import com.security.dao.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private static final String REDIRECT_LOGIN= "redirect:/login";

    private final UserService userService;

    private MessageSource messageSource;

    @GetMapping
    public String register(final Model model){
        model.addAttribute("userData", new UserDto());
        return "account/register";
    }

    @PostMapping
    public String userRegistration(final @Valid UserDto userData, final BindingResult bindingResult, final Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("registrationForm", userData);
            return "account/register";
        }
        try {
            userService.register(userData);

        }catch (UserAlreadyExistException e){
            bindingResult.rejectValue("email", "userData.email","An account already exists for this email.");
            model.addAttribute("registrationForm", userData);
            return "account/register";
        }
        model.addAttribute("registrationMsg", messageSource.getMessage("user.registration.verification.email.msg", null, LocaleContextHolder.getLocale()));
        return "account/register";
    }

}
