package com.metahive.transport.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.metahive.transport.server.entity.User;
import com.metahive.transport.server.mapper.UserMapper;
import com.metahive.transport.server.service.UserService;
import org.springframework.stereotype.Service;

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

}
