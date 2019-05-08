package com.demo.api.service;

import com.demo.api.commons.service.IBaseService;
import com.demo.api.entity.User;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author wanghw
 * @since 2019-02-21
 */
public interface IUserService extends IBaseService<User> {
    User get(Long id);

    void register(User user);
    void login(User user);
    void logout(String token);
    String generateToken(User user);
    User validateToken(String token);

    boolean matchPassword(User user,String password);
}
