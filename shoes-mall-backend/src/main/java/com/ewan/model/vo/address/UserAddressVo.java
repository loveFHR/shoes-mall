package com.ewan.model.vo.address;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ewan.model.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * @author Ewan
 * @date 2024/5/14
 */

@Data
public class UserAddressVo {
    public UserAddressVo() {
        this.isDefault = 0;
    }

    /**
     * 是否默认地址 0-否 1-是
     */
    private Integer isDefault;
    /**
     * 收货地址id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 地址
     */
    private String address;

    /**
     * 姓名
     */
    private String name;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String district;


    /**
     * 省市区
     */
    private String pcdStr;
}

    
    