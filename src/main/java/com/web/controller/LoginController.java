package com.web.controller;

import com.web.entity.LoginResult;
import com.web.entity.User;
import com.web.entity.User_vue;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@CrossOrigin
@Controller
public class LoginController {
    @Autowired
    UserRepository userRepository;
    @PostMapping(value = {"/login"})
    @ResponseBody
    public LoginResult login(@RequestBody User_vue user,
                        Model model, HttpSession session){
        String username = user.username;
        String password = user.password;
        LoginResult loginResult = new LoginResult();
        if(username == null){
            loginResult.success = false;
            loginResult.ID = 0 ;
            loginResult.msg = "请填写账号";
            return loginResult;
        }
        if(password == null ){
            loginResult.success = false;
            loginResult.ID = 0 ;
            loginResult.msg = "请填写密码";
            return loginResult;
        }
        if (userRepository.findByUsernameAndPassword(username, password).size() == 1){
            User tmp = userRepository.findByUsername(username).get(0);
            loginResult.success = true;
            loginResult.ID = tmp.id ;
            loginResult.msg = "登录成功";
        }
        else {
            loginResult.success = false;
            loginResult.ID = 0 ;
            loginResult.msg = "账号或密码错误";
        }
        return loginResult;
    }

    @GetMapping(value = {"/tourist_login"})
    public String tourist_login(Model model, HttpSession session){
        session.setAttribute("loginUser",null);
        session.setAttribute("userId", null);
        model.addAttribute("msg","欢迎游客！");
        return "redirect:/main";
    }
}