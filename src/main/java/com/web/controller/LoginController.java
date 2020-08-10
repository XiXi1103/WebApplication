package com.web.controller;

import com.web.entity.User;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpSession;

@CrossOrigin
@Controller
public class LoginController {
    @Autowired
    UserRepository userRepository;
    @PostMapping(value = {"/index_login"})
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        Model model, HttpSession session){
        if(username == null){
            model.addAttribute("msg", "请填写账号");
            return "register";
        }
        if(password == null ){
            model.addAttribute("msg", "请填写密码");
            return "register";
        }
        if (userRepository.findByUsernameAndPassword(username, password).size() == 1){
            User tmp = userRepository.findByUsername(username).get(0);
            session.setAttribute("loginUser",username);
            session.setAttribute("userId", tmp.id);
            return "redirect:/main";
        }
        else {
            model.addAttribute("msg", "用户名或密码错误");
            return "index";
        }
    }

    @GetMapping(value = {"/tourist_login"})
    public String tourist_login(Model model, HttpSession session){
        session.setAttribute("loginUser",null);
        session.setAttribute("userId", null);
        model.addAttribute("msg","欢迎游客！");
        return "redirect:/main";
    }
}