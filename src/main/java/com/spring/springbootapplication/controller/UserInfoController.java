package com.spring.springbootapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.spring.springbootapplication.dto.UserAddRequest;
import com.spring.springbootapplication.service.UserInfoService;

@Controller
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/signup")
    public String displaySignup(Model model) {
        model.addAttribute("userAddRequest", new UserAddRequest());
        return "signup";
    }

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute UserAddRequest userAddRequest, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "signup";
        }
        
        userInfoService.save(userAddRequest);
        return "redirect:/"; 
    }
}