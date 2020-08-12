package com.web.controller;

import com.web.entity.*;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@CrossOrigin
@Controller
public class UserController {
    @Autowired
    UserRepository userRepository;
    @GetMapping(value = {"/personalInfo"})
    @ResponseBody
    public PersonalInfoResult changeInfo(@RequestBody String username,
                                         @RequestParam String password,
                                         @RequestParam String email,
                                         @RequestParam String phone_num,
                                         Model model, HttpSession session){
        PersonalInfoResult result = new PersonalInfoResult();
        User user = userRepository.findByUsername(username).get(0);
        user.password = password;
        user.email = email;
        user.phoneNumber = phone_num;
        userRepository.save(user);

        result.username = username;
        result.create_time = user.createTime.toString();
        result.password = password;
        result.email = email;
        result.phone_num = phone_num;
        result.success = true;

        return result;
    }
}