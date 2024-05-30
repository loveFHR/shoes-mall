package com.ewan.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ewan.common.ErrorCode;
import com.ewan.exception.BusinessException;
import com.ewan.mapper.UserAddressMapper;
import com.ewan.model.entity.User;
import com.ewan.model.entity.UserAddress;
import com.ewan.model.vo.address.UserAddressVo;
import com.ewan.utils.BeanUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class UserAddressService extends ServiceImpl<UserAddressMapper, UserAddress> {

    @Resource
    private  UserService userService;
    public UserAddressVo queryById(Long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        UserAddress address = this.getById(id);
        if (address == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id所指地址不存在");
        }
        UserAddressVo vo = BeanUtils.copyProperties(address, UserAddressVo.class);
        vo.setPcdStr(vo.getProvince() + vo.getCity() + vo.getDistrict());
        if (loginUser.getDefaultAddressId() != null && loginUser.getDefaultAddressId().equals(address.getId())) {
            vo.setIsDefault(1);
        }
        return vo;
    }
}
