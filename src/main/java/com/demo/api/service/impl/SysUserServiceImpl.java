package com.demo.api.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.api.commons.security.JwtTokenProvider;
import com.demo.api.commons.service.BaseServiceImpl;
import com.demo.api.entity.SysPermission;
import com.demo.api.entity.SysUser;
import com.demo.api.entity.SysUserRole;
import com.demo.api.mapper.SysUserMapper;
import com.demo.api.service.ISysRoleService;
import com.demo.api.service.ISysUserRoleService;
import com.demo.api.service.ISysUserService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.api.map.event.EntryExpiredListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 系统用户表 服务实现类
 * </p>
 *
 * @author wanghw
 * @since 2019-03-21
 */
@Slf4j
@Service
public class SysUserServiceImpl extends BaseServiceImpl<SysUserMapper, SysUser> implements ISysUserService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Resource
    private ISysUserService self;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private JwtTokenProvider jwtTokenProvider;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private ISysRoleService roleService;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private ISysUserRoleService sysUserRoleService;

    @PostConstruct
    public void init(){
        getAuthCache().addListener((EntryExpiredListener<Integer, Integer>) event -> self.logout(event.getKey()+""));
    }

    private RMapCache<String,Integer> getAuthCache(){
        return redissonClient.getMapCache("sys_user_login");
    }

    @Cacheable(value = "user_auth", key = "#loginName")
    public SysUser getUserByLoginName(String loginName){
        SysUser sysUser = this.lambdaQuery()
                .eq(SysUser::getLoginName,loginName)
                .eq(SysUser::getStatus, 1)
                .one();

        if(null == sysUser)return null;

        SysPermission sysPermission = new SysPermission();
        sysPermission.setUserId(sysUser.getId());
        List<SysPermission> roleList = roleService.findRolePermissionsByUser(sysPermission);
        Set<String> authorities = new HashSet<>();
        for(SysPermission permission:roleList){
            authorities.add(permission.getPermission());
        }
        sysUser.setRoles(authorities);

        return sysUser;
    }

    @CacheEvict(value = "user_auth", key = "#loginName")
    public String login(String loginName,String password,int ip){
        SysUser sysUser = getUserByLoginName(loginName);
        if(null == sysUser)return null;

        if(!passwordEncoder.matches(password,sysUser.getPassword())){
            return null;
        }

        SysUser updateUser = new SysUser();
        updateUser.setLoginIp(ip);
        updateUser.setLoginTime(LocalDateTime.now());
        updateUser.setId(sysUser.getId());
        this.updateById(updateUser);

        return jwtTokenProvider.createToken(loginName);
    }

    public void save(SysUser user,SysUser manager){

        //if (!userRepository.existsByUsername(user.getUsername()))
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        this.save(user);
        //return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

    }

    @CacheEvict(value = "user_auth", key = "#loginName")
    public void logout(String loginName) {
        //todo do something
    }

    public Page<SysUser> findSysUserPage(Page<SysUser> sysUserPage,SysUser sysUser) {
        return sysUserMapper.findSysUserPage(sysUserPage,sysUser);
    }

    @CacheEvict(value = "user_auth", key = "#loginName")
    public void updateUser(SysUser update, String loginName) {
        updateById(update);
    }

    @CacheEvict(value = "user_auth", key = "#loginName")
    @Transactional
    public void updateStatus(Long sysUserId, Integer status,String loginName,SysUser sysUser) {
        SysUser update = new SysUser();
        update.setId(sysUserId);
        update.setStatus(status);
        updateById(update);
        List<SysUserRole> list = sysUserRoleService.lambdaQuery().eq(SysUserRole::getUserId, sysUserId).list();
        List<SysUserRole> roleList = Lists.newArrayList();
        list.forEach(sysUserRole -> {
            if (status.equals(sysUserRole.getStatus())) return;
            SysUserRole userRole = new SysUserRole();
            userRole.setId(sysUserRole.getId());
            userRole.setStatus(status);
            userRole.setUpdateTime(LocalDateTime.now());
            userRole.setOperationId(sysUser.getId());
            roleList.add(userRole);
        });
        if (!CollectionUtils.isEmpty(roleList)) {
            sysUserRoleService.updateBatchById(roleList);
        }
    }

    @Override
    public List<SysUserRole> userRoleByUser(Long sysUerId) {
        return sysUserMapper.userRolesByUser(sysUerId);
    }

    @Override
    @CacheEvict(value = "user_auth", key = "#loginName")
    public void evictUserCache(String loginName){}
}
