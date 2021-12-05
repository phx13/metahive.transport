package com.metahive.transport.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metahive.transport.server.common.JwtTokenUtil;
import com.metahive.transport.server.entity.User;
import com.metahive.transport.server.mapper.UserMapper;
import com.metahive.transport.server.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author phx
 * @since 2021-11-28
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    @Lazy
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public User register(User user) {
        User newUser = new User();
        BeanUtils.copyProperties(user, newUser);
        //查询是否有相同用户名的用户
        List<User> selectedUsers = this.baseMapper.selectList(new LambdaQueryWrapper<User>().eq(User::getName, newUser.getName()));
        if (CollectionUtils.isNotEmpty(selectedUsers)) {
            return null;
        }
        //将密码进行加密操作
        encodePassword(user.getPassword(), newUser);
        this.baseMapper.insert(newUser);
        return newUser;
    }

    @Override
    public String login(String name, String password) {
        String token = null;
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(name);
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            if (!passwordEncoder.matches(password, userDetails.getPassword())) {
                throw new BadCredentialsException("密码不正确");
            }
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            token = jwtTokenUtil.generateToken(userDetails);
        } catch (AuthenticationException e) {
            log.warn(e.getMessage());
        }
        return token;
    }

    @Override
    public User findUserByName(String username) {
        //查询是否有相同用户名的用户
        List<User> selectedUsers = this.baseMapper.selectList(new LambdaQueryWrapper<User>().eq(User::getName, username));
        if (CollectionUtils.isNotEmpty(selectedUsers)) {
            return selectedUsers.get(0);
        } else {
            return null;
        }
    }

    @Override
    public User findUserByEmail(String email) {
        //查询是否有相同用户名的用户
        List<User> selectedUsers = this.baseMapper.selectList(new LambdaQueryWrapper<User>().eq(User::getEmail, email));
        if (CollectionUtils.isNotEmpty(selectedUsers)) {
            return selectedUsers.get(0);
        } else {
            return null;
        }
    }

    @Override
    public String updateUser(User user) {
        try {
            User originalUser = this.getById(user.getId());
            originalUser.setName(user.getName());
            originalUser.setEmail(user.getEmail());
            //将密码进行加密操作
            encodePassword(user.getPassword(), originalUser);
            this.updateById(originalUser);
            return "更新成功";
        } catch (Exception e) {
            e.printStackTrace();
            return "更新失败";
        }
    }

    @Override
    public void encodePassword(String newPassword, User toUser) {
        //将密码进行加密操作
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodePassword = passwordEncoder.encode(newPassword);
        toUser.setPassword(encodePassword);
    }
}
