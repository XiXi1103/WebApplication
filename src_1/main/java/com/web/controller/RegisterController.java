package com.web.controller;

import com.web.config.MailSender;
import com.web.entity.ReturnResult.EmailResult;
import com.web.entity.ReturnResult.RegisterResult;
import com.web.entity.User;
import com.web.entity.vue.User_vue;
import com.web.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@Controller
@CrossOrigin
public class RegisterController {
    @Autowired
    UserRepository userRepository;
    int s;
    String usernamePattern = "[a-zA-Z]*\\d+";
    String passwordPattern = "[a-zA-Z]*\\d+";
    String emailPattern;
    @PostMapping(value = "/register")
    @ResponseBody
    public RegisterResult register(@RequestBody User_vue user,
                        Map<String,Object> map)  {
        String username = user.username;
        String password = user.password;
        String email = user.email;
        s=userRepository.findByUsername(username).size()+userRepository.findByEmail(email).size();
        if (s!=0){
//            System.out.println("0");
            RegisterResult registerResult = new RegisterResult();
            registerResult.ID = 0;
            registerResult.success = false;
            registerResult.msg="用户名已被注册，请重试";
            return registerResult;
        }
        if (!user.code.equals("8547")){
            RegisterResult registerResult = new RegisterResult();
            registerResult.ID = 0;
            registerResult.success = false;
            registerResult.msg="验证码有误，请重试";
            return registerResult;
        }
        User tmp =new User();
        tmp.username=username;
        tmp.setPassword(password);
        tmp.email=email;
        tmp.createTime = new Date();
        userRepository.save(tmp);
        RegisterResult registerResult = new RegisterResult();
        registerResult.ID = tmp.id;
//        System.out.println(tmp.id);
//        registerResult.ID = userRepository.findByUsername(username).get(0).id;
        registerResult.success = true;
        registerResult.msg = "注册成功";
        return registerResult;
//        //构造json字符串传递回前端，\为java转义符号为了转义双引号
////        //注意这里格式要求很严格，不能用单引号，建议使用第三方框架自动生成json字符串
//        int success = 1;
//        String s = "{\"ID\":\"" + userRepository.findByUsername(username).get(0).id;
//        s +=   "\"}";
//        System.out.println(user.username);
//        System.out.println(userRepository.findByUsername(username).get(0).id);
//        response.setCharacterEncoding("utf-8");
//        response.setContentType("application/json; charset=utf-8");//返回的格式必须设置为application/json
//        response.getWriter().write(s);//写入到返回结果中
////        //完成，执行到这里就会返回数据给前端，前端结果为success，调用success里面的内容
    }
    @GetMapping(value = "/sendEmail")
    @ResponseBody
    public EmailResult sendEmail(@RequestParam String email){
        EmailResult emailResult = new EmailResult();
        emailResult.success=true;
        try {
            MailSender mSender = new MailSender();
            mSender.sendMail(email, "您的验证码为：8547，请尽快完成验证\n\n---------From BUAA开发团队");
        }catch (Exception e){
            emailResult.success=false;
            return emailResult;
        }
        return emailResult;

    }
}