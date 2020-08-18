package com.mercari.controller;

import java.util.Objects;

import com.mercari.domain.User;
import com.mercari.form.InsertUserForm;
import com.mercari.service.InsertUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Validated
@RequestMapping("/toInsert")
public class InsertUserController {

    @Autowired
    private InsertUserService insertUserService;

    @ModelAttribute
	public InsertUserForm setUpInsertUserForm() {
		return new InsertUserForm();
	}
    
    @RequestMapping("")
    public String toInsert(){
        return "register_user";
    }

    @RequestMapping("/insert")
    public String insert(@Validated InsertUserForm form,BindingResult result,Model model) {
        if(result.hasErrors()){
            return "register_user";
        }

        User user=new User();
        user.setName(form.getName());
        user.setEmail(form.getEmail());
        user.setPassword(form.getPassword());

        // メールアドレスの重複確認
		if (!(Objects.isNull(insertUserService.findByMailAddress(form.getEmail())))) {
			model.addAttribute("emailError", "そのメールアドレスはすでに使われています");
			return toInsert();
        }
        // 確認用パスワード
		if (form.getPassword().equals(form.getCheckpassword())) {
			insertUserService.insert(user);
		} else {
			model.addAttribute("passwordError", "パスワードと確認用パスワードが不一致です");
			return toInsert();
        }
        return "forward:/toLogin";
    }
}