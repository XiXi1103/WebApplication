package com.web.controller;

import com.web.entity.*;
import com.web.entity.ReturnResult.PersonalInfoResult;
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
    @GetMapping(value = {"/changePerInfo"})
    @ResponseBody
    public PersonalInfoResult changeInfo(@RequestParam int userId,
                                         @RequestParam String password,
                                         @RequestParam String email,
                                         @RequestParam String phoneNum,
                                         Model model, HttpSession session){
        PersonalInfoResult result = new PersonalInfoResult();
        User user = userRepository.findUserById(userId);
        user.password = password;
        user.email = email;
        user.phoneNumber = phoneNum;
        userRepository.save(user);

        result.success = true;

        return result;
    }
    @GetMapping(value = {"/personalInfo_1"})
    @ResponseBody
    public PersonalInfoResult personalInfo(@RequestParam int userId,
                                         Model model, HttpSession session){
//        System.out.println("Person:" + userId);
        PersonalInfoResult result = new PersonalInfoResult();
        User user = userRepository.findUserById(userId);
        if(user == null){
            result.success = false;
            result.msg = "查无此人";
            return result;
        }
        result.phoneNum = user.phoneNumber;
        result.password = user.password;
        result.email = user.email;
        result.create_time = user.createTime.toString();
        result.username = user.username;
        result.success = true;
        return result;
    }


    @GetMapping(value = {"/personalInfo"})
    @ResponseBody
    public PersonalInfoResult getInfo(@RequestParam int userId,
                                           @RequestParam int id,
                                           Model model, HttpSession session){
//        System.out.println("Person:" + userId);
        PersonalInfoResult result = new PersonalInfoResult();
        User looker = userRepository.findUserById(userId);
        User user = userRepository.findUserById(id);
        if(looker == null){
            result.success = false;
            result.msg = "当前用户不存在，请登录";
            return result;
        }
        if(user == null){
            result.success = false;
            result.msg = "您查看的用户已不存在";
            return result;
        }
        if(userId == id){
            result.isOther = false;
            result.password = user.password;
        }
        else{
            result.password = "您无法查看他人密码(所以你是怎么看到的?";
            result.isOther = true;
        }
        result.phoneNum = user.phoneNumber;
        result.email = user.email;
        result.create_time = user.createTime.toString();
        result.username = user.username;
        result.success = true;
        return result;
    }
}