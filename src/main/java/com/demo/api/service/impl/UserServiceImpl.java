package com.demo.api.service.impl;

import com.demo.api.commons.service.BaseServiceImpl;
import com.demo.api.entity.User;
import com.demo.api.mapper.UserMapper;
import com.demo.api.service.IUserService;
import com.google.common.base.Strings;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.event.EntryExpiredListener;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author wanghw
 * @since 2019-02-21
 */
@Slf4j
@Service
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements IUserService {
    private final static String USER_LOGIN_CACHE_KEY = "user_login";
    private final static String USER_TOKEN_CACHE_KEY = "user_token";
    private final static String SECRET = "123456";
    private final static Integer EXPIRATION = 86400;

    @Resource
    private RedissonClient redissonClient;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private IUserService self;


    @PostConstruct
    public void init(){
        getLoginCache().addListener((EntryExpiredListener<Integer, Integer>) event -> self.logout(event.getKey()+""));
    }
    private RMapCache<Long,String> getTokenCache(){
        return redissonClient.getMapCache(USER_TOKEN_CACHE_KEY);
    }
    private RMapCache<String,Integer> getLoginCache(){
        return redissonClient.getMapCache(USER_LOGIN_CACHE_KEY);
    }

    private void flush(String token,Long id){
        String lastToken = getTokenCache().get(id);
        if (!Strings.isNullOrEmpty(lastToken)){
            self.logout(lastToken);
        }
        getTokenCache().put(id,token);
    }

    @Cacheable(value = "user", key = "#id")
    public User get(Long id){
        return getById(id);
    }


    @Transactional
    public void register(User user){

        user.setStatus(1);
        user.setRegisterTime(LocalDateTime.now());
        user.setLoginTime(LocalDateTime.now());
        user.setLoginIp(user.getRegisterIp());
        this.save(user);
    }

    public String generateToken(User user){

        Date expirationDate = new Date(System.currentTimeMillis()  + EXPIRATION * 1000);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());

        String token =  Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();

        flush(token,user.getId());

        getLoginCache().put(token,1,EXPIRATION, TimeUnit.SECONDS);

        return token;
    }

    @CacheEvict(value = "user_token", key = "#token")
    public void logout(String token) {
        getLoginCache().remove(token);
    }

    @Cacheable(value = "user_token", key = "#token")
    public User validateToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            log.debug(e.getMessage());
            return null;
        }
        Date expiration = claims.getExpiration();
        if (expiration.before(new Date())) {
            return null;
        }
        Integer login = getLoginCache().get(token);
        if (login == null )return null;


        long cacheExpiration = (expiration.getTime() - System.currentTimeMillis())/1000;
        if (cacheExpiration <= 0)return null;

        getLoginCache().put(token,1, cacheExpiration, TimeUnit.SECONDS);

        Long id = (Long)claims.get("userId");
        return this.getById(id);
    }

    @Transactional
    @CacheEvict(value = "user",key = "#user.id")
    public void login(User user){
        User updateUser = new User();
        updateUser.setLoginIp(user.getLoginIp());
        updateUser.setLoginTime(LocalDateTime.now());
        updateUser.setId(user.getId());

        this.updateById(updateUser);
    }

    public boolean matchPassword(User user,String password){
        return passwordEncoder.matches(password,user.getPassword());
    }
}
