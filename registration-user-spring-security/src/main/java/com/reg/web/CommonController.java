package com.reg.web;

import com.reg.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class CommonController {

	private final UserService userService;

/*	// Login form
	@RequestMapping("/login")
	public String login() {
		return "account/login";
	}

	// Login form with error
	@RequestMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "account/login";
	}*/

/*	@GetMapping("/login")
	public String login() {
		return "account/login";
	}*/

	@GetMapping("/login-error")
	public String loginError(Model model) {
		model.addAttribute("loginError", true);
		return "account/login";
	}

	@GetMapping("login")
	public String login(@RequestParam(value = "error", defaultValue = "false") boolean loginError,
						final Model model,
						Principal principal){
		String userName = "";
		if(principal != null){
			userName = principal.getName();
		}

		if(loginError){
			if(StringUtils.isNotEmpty(userName) && userService.checkIfUserExist(userName)){
				model.addAttribute("accountLocked", Boolean.TRUE);
				return "account/login";
			}
		}

	//	model.addAttribute("accountLocked", Boolean.FALSE);
		return "account/login";
	}
}
