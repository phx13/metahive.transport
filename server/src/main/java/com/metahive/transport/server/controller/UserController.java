package com.metahive.transport.server.controller;


import com.metahive.transport.server.entity.User;
import com.metahive.transport.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        userService.getById(user.getId());
        return "login";
    }

    @PostMapping("/register")
    public String register(@RequestBody User user) {
        return "login!";
    }

}

