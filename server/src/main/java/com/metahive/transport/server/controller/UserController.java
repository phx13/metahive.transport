package com.metahive.transport.server.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.metahive.transport.server.common.JwtTokenUtil;
import com.metahive.transport.server.common.MailSenderHandler;
import com.metahive.transport.server.common.RandomGenerator;
import com.metahive.transport.server.entity.User;
import com.metahive.transport.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author phx
 * @since 2021-11-28
 */
@RestController
@CrossOrigin
@RequestMapping("/server/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailSenderHandler mailSenderHandler;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private RandomGenerator randomGenerator;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @PostMapping("/update")
    public Map<String, String> update(@RequestBody User user) {
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("result", userService.updateUser(user));
        return resultMap;
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody User user) {
        String id = userService.getOne(new LambdaQueryWrapper<User>().eq(User::getName, user.getName())).getId().toString();
        String token = userService.login(user.getName(), user.getPassword());
        Map<String, String> resultMap = new HashMap<>();
        if (StringUtils.isBlank(token)) {
            resultMap.put("result", "用户名或密码错误");
            return resultMap;
        }
        resultMap.put("token", token);
        resultMap.put("tokenHead", tokenHead);
        resultMap.put("username", jwtTokenUtil.getUsernameFromToken(token));
        resultMap.put("id", id);
        resultMap.put("result", "登录成功");
        return resultMap;
    }

    @PostMapping("/register")
    public Map<String, String> register(@RequestBody User user) {
        User newUser = userService.register(user);
        Map<String, String> resultMap = new HashMap<>();
        if (newUser == null) {
            resultMap.put("result", "用户已存在");
        } else {
            resultMap.put("result", "注册成功");
        }
        return resultMap;
    }

    @PostMapping("/forgot_password")
    public Map<String, String> resetPassword(@RequestBody User user) {
        Map<String, String> resultMap = new HashMap<>();
        try {
            User toUser = userService.findUserByEmail(user.getEmail());
            String newPassword = randomGenerator.randomPassword(6);
            userService.encodePassword(newPassword, toUser);
            userService.updateUser(toUser);
            String content = "您的密码重置为：" + newPassword + " 请您登录到个人中心修改新密码";
            mailSenderHandler.sendMail(user.getEmail(), content);
            resultMap.put("result", "邮件发送成功");
        } catch (Exception e) {
            resultMap.put("result", "邮件发送失败");
        }
        return resultMap;
    }

}

