package com.reg.web;

import com.reg.dto.UserRegistrationDto;
import com.reg.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

	private UserService userService;

	public RegistrationController(UserService userService) {
		super();
		this.userService = userService;
	}

/*
	@ModelAttribute("user")
	public UserRegistrationDto userRegistrationDto() {
		return new UserRegistrationDto();
	}

		@GetMapping
	public String showRegistrationForm() {
		return "registration";
	}

	@PostMapping
	public String registerUserAccount(@ModelAttribute("user") UserRegistrationDto registrationDto) {

		userService.save(registrationDto);
		return "redirect:/registration?success";
	}
*/


	/**
	 * Выше можно увидеть альтернативный способ предоставления страницы Регистрации
	 * @param model
	 * @return
	 */

	@GetMapping
	public String showRegistrationForm(Model model) {
		UserRegistrationDto userRegistrationDto = new UserRegistrationDto();
		model.addAttribute("user", userRegistrationDto);
		return "registration";
	}

	@PostMapping
	public String registerUserAccount(UserRegistrationDto user) {

		userService.save(user);
		return "redirect:/registration?success";
	}

}
