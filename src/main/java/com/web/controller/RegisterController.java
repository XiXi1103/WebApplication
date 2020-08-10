package com.web.controller;

import com.web.entity.User;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;
import java.util.Map;

@Controller

public class RegisterController {
    @Autowired
    UserRepository userRepository;
    int s;
    String usernamePattern = "[a-zA-Z]*\\d+";
    String passwordPattern = "[a-zA-Z]*\\d+";
    String emailPattern;
    @PostMapping(value = "/register_apply")
    public void login(@RequestParam("username") String username,
                        @RequestParam("password1") String password1,
                        @RequestParam("password2") String password2,
                        @RequestParam("email") String email,
                        Map<String,Object>map)
    {
        s=userRepository.findByUsername(username).size()+userRepository.findByEmail(email).size();
        if (s!=0){
            map.put("msg", "用户名或邮箱已存在");
            //return "register";
        }
        if (password1.compareTo(password2)!=0){
            map.put("msg", "密码不一致");
            //return "register";
        }
        User tmp =new User();
        tmp.username=username;
        tmp.setPassword(password1);
        tmp.email=email;
        tmp.createTime = new Date();
        userRepository.save(tmp);
        System.out.println("1");
    }
}