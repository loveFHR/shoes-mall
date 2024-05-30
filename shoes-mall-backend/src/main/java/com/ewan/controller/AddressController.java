package com.ewan.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ewan.common.BaseResponse;
import com.ewan.common.ErrorCode;
import com.ewan.common.ResultUtils;
import com.ewan.constant.UserConstant;
import com.ewan.exception.BusinessException;
import com.ewan.model.dto.address.UserAddressAddDto;
import com.ewan.model.dto.address.UserAddressUpdateDto;
import com.ewan.model.entity.User;
import com.ewan.model.entity.UserAddress;
import com.ewan.model.vo.address.UserAddressVo;
import com.ewan.service.UserAddressService;
import com.ewan.service.UserService;
import com.ewan.utils.BeanUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
 /**
  * 地址
  *
  * @author cx
  * @date 2024/5/27
  */

 @Slf4j
 @Validated
 @RestController
 @RequestMapping("/user")
 public class AddressController {

     @Resource
     private  UserAddressService userAddressService;

     @Resource
     private UserService userService;

     /**
      * 添加地址
      *
      * @param dto dto
      * @return 表主键id
      */
  @PostMapping("/add")
  public BaseResponse<Long> add(@RequestBody @Validated UserAddressAddDto dto, HttpServletRequest request) {
      User loginUser = userService.getLoginUser(request);
      UserAddress  userAddress = BeanUtils.copyProperties(dto, UserAddress.class);
      userAddress.setUserId(loginUser.getId());
      userAddressService.save(userAddress);
      if (dto.getIsDefault()==1){
          User user = new User();
          user.setId(loginUser.getId());
          user.setDefaultAddressId(userAddress.getId());
          userService.updateById(user);
      }
      return ResultUtils.success(userAddress.getUserId());
  }


  /**
   * 修改地址
   *
   * @param dto dto
   * @return 表主键id
   */
  @PostMapping("/update")
  public BaseResponse<Boolean> update(@RequestBody @Validated UserAddressUpdateDto dto, HttpServletRequest request) {
      UserAddress entity = userAddressService.getById(dto.getId());
      Assert.isTrue(entity != null, "id所指地址不存在");
      User loginUser = userService.getLoginUser(request);
      if (!UserConstant.ADMIN_ROLE.equals(loginUser.getRole()) && !Objects.equals(loginUser.getId(), entity.getUserId())) {
          throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限操作别人的地址");
      }
      UserAddress userAddress = BeanUtils.copyProperties(dto, UserAddress.class);
      boolean res = userAddressService.updateById(userAddress);

      if (dto.getIsDefault()==1){
          User user = new User();
          user.setId(loginUser.getId());
          user.setDefaultAddressId(userAddress.getId());
          userService.updateById(user);
      }
      return  ResultUtils.success(res);
  }

  /**
   * 设置为默认地址
   *
   * @param id 地址id
   * @return 是否成功
   */
  @PostMapping("/update/{id}")
  public BaseResponse<Boolean> defaultAddress(@PathVariable("id") Long id, HttpServletRequest request) {
      UserAddress entity =  userAddressService.getById(id);
      Assert.isTrue(entity != null, "id所指地址不存在");
      User loginUser = userService.getLoginUser(request);
      if (!UserConstant.ADMIN_ROLE.equals(loginUser.getRole()) && !Objects.equals(loginUser.getId(), entity.getUserId())) {
          throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您无权限操作别人的地址");
      }
      loginUser.setDefaultAddressId(id);
      boolean res = userService.updateById(loginUser);
      return ResultUtils.success(res);
  }


  /**
   * 删除地址
   *
   * @param id 地址表id
   * @return 是否成功
   */
  @PostMapping("/delete/{id}")
  public BaseResponse<Boolean> delete(@PathVariable("id") Long id, HttpServletRequest request) {
      UserAddress entity = userAddressService.getById(id);
      Assert.isTrue(entity != null, "id所指地址不存在");
      User loginUser = userService.getLoginUser(request);
      if (!UserConstant.ADMIN_ROLE.equals(loginUser.getRole()) && !Objects.equals(loginUser.getId(), entity.getUserId())) {
          throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您无权限操作别人的地址");
      }
      boolean  res = userAddressService.removeById(id);
      return  ResultUtils.success(res);
  }


  /**
   * 地址查询(无分页）
   *
   * @return 地址ListVO
   */
  @PostMapping("/query")
  public BaseResponse<List<UserAddressVo>> query(HttpServletRequest request) {
      User loginUser = userService.getLoginUser(request);
      List<UserAddress> userAddressList = userAddressService.list(new QueryWrapper<UserAddress>().eq(UserAddress.COL_USER_ID, loginUser.getId()));
      List<UserAddressVo> res = new ArrayList<>();
      userAddressList.forEach(r -> {
          UserAddressVo vo = BeanUtils.copyProperties(r, UserAddressVo.class);
          if (loginUser.getDefaultAddressId() != null && loginUser.getDefaultAddressId().equals(r.getId())) {
              vo.setIsDefault(1);
          }
          vo.setPcdStr(r.getProvince() + r.getCity() + r.getDistrict());
          res.add(vo);
      });
      return  ResultUtils.success(res);
  }

  /**
   * 获取当前用户的默认地址
   *
   * @return 地址vo
   */
  @PostMapping("/query/default")
  public BaseResponse<UserAddressVo> getDefaultAddress(HttpServletRequest request) {
      User loginUser = userService.getLoginUser(request);
      Long  defaultAddressId = loginUser.getDefaultAddressId();
      if (defaultAddressId == null) {
          return ResultUtils.success(null);
      }
      UserAddress userAddress = userAddressService.getById(defaultAddressId);
      if (userAddress == null) {
          return ResultUtils.success(null);
      }
      UserAddressVo  vo = BeanUtils.copyProperties(userAddress, UserAddressVo.class);
      vo.setIsDefault(1);
      vo.setPcdStr(userAddress.getProvince() + userAddress.getCity() + userAddress.getDistrict());
      return ResultUtils.success(vo);
  }


  /**
   * 地址查询ById
   *
   * @return 地址addressVO
   */
  @GetMapping("/query/{id}")
  public BaseResponse<UserAddressVo> queryById(@PathVariable("id") Long id, HttpServletRequest request) {
      return  ResultUtils.success(userAddressService.queryById(id, request));
  }
 }


