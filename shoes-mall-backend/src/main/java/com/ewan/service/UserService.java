package com.ewan.service;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ewan.common.ErrorCode;
import com.ewan.exception.BusinessException;
import com.ewan.mapper.UserMapper;
import com.ewan.model.entity.User;
import com.ewan.model.enums.UserRoleEnum;
import com.ewan.model.vo.user.LoginUserVo;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {
    @Resource
    private RedisService redisService;

    /**
     * 获取当前登录用户
     *
     * @return userVo
     */
    public User getLoginUser(HttpServletRequest request) {
        // 先判断是否已登录
        String token = request.getHeader("Token");
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        String json = redisService.get(token);
        User currentUser = JSONUtil.toBean(json, User.class);
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }


    /**
     * 获取当前登录用户
     */
    public Long getLoginUserId(HttpServletRequest request) {
        // 先判断是否已登录
        String token = request.getHeader("Token");
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        String json = redisService.get(token);
        User currentUser = JSONUtil.toBean(json, User.class);
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser.getId();
    }


    /**
     * 获取当前登录用户（允许未登录）
     */
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        // 先判断是否已登录
        String token = request.getHeader("Token");
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String json = redisService.get(token);
        User currentUser = JSONUtil.toBean(json, User.class);
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getId();
        return this.getById(userId);
    }

    /**
     * 是否为管理员
     *
     * @return true-是 false-否
     */
    public boolean isAdmin(HttpServletRequest request) {

        // 先判断是否已登录
        String token = request.getHeader("Token");
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String json = redisService.get(token);
        User user = JSONUtil.toBean(json, User.class);
        return isAdmin(user);
    }

    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getRole());
    }


    /**
     * 用户注销
     */
    public boolean userLogout(HttpServletRequest request) {
        String token = request.getHeader("Token");
        if (StringUtils.isBlank(token)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        redisService.deleteKey(token);
        return true;
    }

    public LoginUserVo getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVo loginUserVO = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }
}
