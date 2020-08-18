package com.mercari.controller;

import java.util.Objects;

import javax.servlet.http.HttpSession;

import com.mercari.form.LoginForm;
import com.mercari.security.LoginUserDetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class LoginController {

    @Autowired
    private HttpSession session;
    
    @ModelAttribute
	public LoginForm setUpLoginForm() {
		return new LoginForm();
	}
    
    @RequestMapping("/toLogin")
    public String toLogin(Model model,String error){
        if(Objects.nonNull(error)){
            model.addAttribute("errorMessage", "メールアドレスまたはパスワードが違います");
        }
        return "register";
    }

    @RequestMapping("/login/success")
    public String login(Model model,@AuthenticationPrincipal LoginUserDetails userDetails){
        session.setAttribute("user", userDetails.getUser());
		return "redirect:/item-list";
    }

    @RequestMapping("/logout/success")
	public String logout() {
		session.removeAttribute("user");
		return "redirect:/item-list";
	}
}