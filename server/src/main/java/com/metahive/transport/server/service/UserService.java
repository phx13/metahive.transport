package com.metahive.transport.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.metahive.transport.server.entity.User;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author phx
 * @since 2021-11-28
 */
public interface UserService extends IService<User> {
    /**
     * 注册功能
     * @param user
     * @return User
     */
    User register(User user);

    /**
     * 登录功能
     * @param username 用户名
     * @param password 密码
     * @return 生成的JWT的token
     */
    String login(String username, String password);

    /**
     * 登录功能
     * @param username 用户名
     * @return 查找到的User
     */
    User findUserByName(String username);

    /**
     * 登录功能
     * @param email 邮箱
     * @return 查找到的User
     */
    User findUserByEmail(String email);

    /**
     * 修改信息
     * @param user
     * @return 结果
     */
    String updateUser(User user);

    /**
     * 密码加密
     * @param newPassword
     */
    void encodePassword(String newPassword, User toUser);
}
