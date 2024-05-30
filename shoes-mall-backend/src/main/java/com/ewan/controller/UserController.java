package com.ewan.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ewan.annotation.AuthCheck;
import com.ewan.common.BaseResponse;
import com.ewan.common.ErrorCode;
import com.ewan.common.ResultUtils;
import com.ewan.constant.RedisExpireTimeConstant;
import com.ewan.constant.UserConstant;
import com.ewan.exception.BusinessException;
import com.ewan.exception.ThrowUtils;
import com.ewan.model.dto.user.*;
import com.ewan.model.entity.User;
import com.ewan.model.vo.user.LoginUserVo;
import com.ewan.service.RedisService;
import com.ewan.service.UserService;
import com.ewan.utils.MailUtil;
import com.ewan.utils.RandomCharUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.ewan.constant.UserConstant.SALT;
import static com.ewan.constant.UserConstant.VERIFY_CODE;

/**
 * 用户
 *
 * @author Ewan
 * @date 2024/4/28
 */


@Slf4j
@Validated
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private MailUtil mailUtil;

    @Resource
    private RedisService redisService;

    /**
     * 注册
     *
     * @param dto 参数
     */
    @PostMapping("/register")
    public BaseResponse<LoginUserVo> register(@Validated @NotNull @RequestBody RegisterDto dto) {
        //验证码
        String verifyCode = redisService.get(VERIFY_CODE + ":" + dto.getToken());
        if (verifyCode == null || !verifyCode.equals(dto.getCode())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }

        //邮箱唯一
        String email = dto.getEmail();
        User findUser = userService.getOne(new QueryWrapper<User>().eq(User.COL_EMAIL, email));
        if (findUser != null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前邮箱已存在");
        }
        //密码校验
        if (!dto.getPassword().equals(dto.getCheckPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "前后两次密码不一致");
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + dto.getPassword()).getBytes());
        String encryptSecurityAnswer = DigestUtils.md5DigestAsHex((SALT + dto.getSecurityAnswer()).getBytes());

        User user = new User();
        user.setAvatar(dto.getAvatar());
        BeanUtils.copyProperties(dto, user);
        user.setPassword(encryptPassword);
        user.setSecurityAnswer(encryptSecurityAnswer);
        userService.save(user);
        UserLoginDto userLoginDto = new UserLoginDto();
        userLoginDto.setEmail(email);
        userLoginDto.setPassword(dto.getPassword());
        userLoginDto.setAdmin(0);
        return userLogin(userLoginDto);
    }


    /**
     * 更新个人信息
     *
     * @param dto dto
     */
    @PostMapping("/update/my")
    public BaseResponse<Boolean> updateMyUser(@Validated @NotNull @RequestBody UserUpdateDto dto,
                                              HttpServletRequest request) {
        if (dto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        BeanUtils.copyProperties(dto, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 发送验证码
     *
     * @param email 邮箱
     */
    @PostMapping("verifyCode")
    public BaseResponse<String> getVerifyCode(@RequestParam("email") String email, Integer isRegister, HttpServletRequest request) {
        if (!MailUtil.isEmailAddress(email)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入正确的邮箱地址");
        }
        if (isRegister != null && isRegister == 1) {
            User user = userService.getOne(new QueryWrapper<User>().eq(User.COL_EMAIL, email).last("limit 1"));
            if (user != null) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "次邮箱已注册，请勿重复注册");
            }
        }
        String code = MailUtil.getVerifyCode();
        request.getSession().setAttribute(UserConstant.VERIFY_CODE, code);
        String token = UUID.randomUUID().toString();
        redisService.set(VERIFY_CODE + ":" + token, code, RedisExpireTimeConstant.EMAIL_EXPIRE_TIME);
        CompletableFuture.runAsync(() -> mailUtil.sendMail(email, UserConstant.VERIFY_CODE_EMAIL_CONTENT + code, UserConstant.VERIFY_CODE_EMAIL_TITLE));
        return ResultUtils.success(token);
    }


    /**
     * 上传头像
     *
     * @param avatar 头像
     * @return 是否成功
     */
    @PostMapping("/avatar/upload")
    public BaseResponse<Boolean> uploadAvatar(@RequestParam("avatar") String avatar, HttpServletRequest request) {
        if (StringUtils.isBlank(avatar)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请输入正确的头像地址");
        }
        User loginUser = userService.getLoginUser(request);
        User user = new User();
        user.setId(loginUser.getId());
        user.setAvatar(avatar);
        userService.updateById(user);
        return ResultUtils.success(true);
    }


    /**
     * 用户登录
     *
     * @param dto dto
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVo> userLogin(@Validated @NotNull @RequestBody UserLoginDto dto) {
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + dto.getPassword()).getBytes());
        System.out.println(encryptPassword);
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(User.COL_EMAIL, dto.getEmail());
        queryWrapper.eq(User.COL_PASSWORD, encryptPassword);
        User user = userService.getOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }

        if (dto.getAdmin() == 1 && !user.getRole().equals(UserConstant.ADMIN_ROLE)) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "仅管理员才能登录");

        }

        // 用户不存在
        if (user.getBan() == 1) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "你上ban位了");
        }

        userService.updateById(user);
        // 3. 记录用户的登录态
        String token = UUID.randomUUID().toString();
        redisService.set(token, JSONUtil.toJsonStr(user), RedisExpireTimeConstant.LOGIN_EXPIRE_TIME);
        LoginUserVo loginUserVO = userService.getLoginUserVO(user);
        loginUserVO.setToken(token);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户注销
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVo> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        LoginUserVo loginUserVO = userService.getLoginUserVO(user);
        return ResultUtils.success(loginUserVO);
    }


    /**
     * 用户分页查询
     *
     * @param current 当前页数
     * @param page    页面大小
     * @return 用户信息
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/query/{current}/{page}")
    public BaseResponse<List<User>> page(@PathVariable("current") Long current, @PathVariable("page") Long page, String name) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like(User.COL_USERNAME, name);
        }
        Page<User> pageRes = userService.page(new Page<>(current, page), queryWrapper);
        List<User> records = pageRes.getRecords();
        return ResultUtils.success(records, pageRes.getTotal());
    }


    /**
     * 禁用或解禁用户 (批量)
     *
     * @param userIds 用户id
     * @return 是否成功
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/changeStatus")
    public BaseResponse<Boolean> changeStatus(@RequestParam("userIds") String userIds) {
        String[] split = userIds.split(",");
        if (split.length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "无此用户");
        }
        Set<Long> userIdSet = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toSet());
        List<User> userList = userService.list(new QueryWrapper<User>().in(User.COL_ID, userIdSet));
        for (User user : userList) {
            user.setBan(user.getBan() == 0 ? 1 : 0);
        }
        boolean res = userService.updateBatchById(userList);
        return ResultUtils.success(res);
    }

    /**
     * 重置密码
     *
     * @return 新密码
     */
    @AuthCheck(mustRole = "admin")
    @PostMapping("/reset/{userId}")
    public BaseResponse<String> reset(@PathVariable("userId") Long userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "该id用户不存在");
        }
        String pwd = RandomCharUtil.randomPwd8();
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + pwd).getBytes());
        user.setPassword(encryptPassword);
        userService.updateById(user);
        return ResultUtils.success(pwd);
    }


    /**
     * 密码修改
     *
     * @return 密码修改是否成功
     */
    @PostMapping("/resetPwd")
    public BaseResponse<Boolean> resetPwd(@RequestBody @Validated ResetPwdDto dto, HttpServletRequest request) {
        Long userId = userService.getLoginUserId(request);
        User user = userService.getById(userId);
        String securityAnswer = dto.getSecurityAnswer();
        String encryptSecurityAnswer = DigestUtils.md5DigestAsHex((SALT + securityAnswer).getBytes());

        //验证密保
        if (!encryptSecurityAnswer.equals(user.getSecurityAnswer())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密保错误");
        }

        if (!dto.getResetPwd().equals(dto.getCheckPwd())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次新密码不一样");
        }
        //更新
        user.setPassword(DigestUtils.md5DigestAsHex((SALT + dto.getResetPwd()).getBytes()));
        boolean res = userService.updateById(user);
        return ResultUtils.success(res);
    }

    /**
     * 通过邮箱修改密保
     *
     * @return 通过邮箱修改密保是否成功
     */
    @PostMapping("/resetSecurity")
    public BaseResponse<Boolean> resetSecurity(@RequestBody @Validated ResetSecurityResetDto dto, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);

        //验证码
        String verifyCode = redisService.get(VERIFY_CODE + ":" + dto.getToken());
        if (verifyCode == null || !verifyCode.equals(dto.getCode())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
        }

        String encryptSecurityAnswer = DigestUtils.md5DigestAsHex((SALT + dto.getSecurityAnswer()).getBytes());
        loginUser.setSecurityQuestion(dto.getSecurityQuestion());
        loginUser.setSecurityAnswer(encryptSecurityAnswer);
        boolean res = userService.updateById(loginUser);
        return ResultUtils.success(res);
    }
}

