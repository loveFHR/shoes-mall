package com.ewan.model.dto.address;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Ewan
 * @date 2024/5/14
 */
@Data
public class UserAddressAddDto {

    /**
     * 手机号
     */
    @Size(max = 16,message = "手机号最大长度要小于 16")
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 地址
     */
    @Size(max = 128,message = "地址最大长度要小于 128")
    @NotBlank(message = "地址不能为空")
    private String address;

    /**
     * 姓名
     */
    @Size(max = 16,message = "姓名最大长度要小于 16")
    @NotBlank(message = "姓名不能为空")
    private String name;

    /**
     * 省
     */
    @Size(max = 16,message = "省最大长度要小于 16")
    @NotBlank(message = "省不能为空")
    private String province;

    /**
     * 市
     */
    @Size(max = 16,message = "市最大长度要小于 16")
    @NotBlank(message = "市不能为空")
    private String city;

    /**
     * 区
     */
    @Size(max = 16,message = "区最大长度要小于 16")
    @NotBlank(message = "区不能为空")
    private String district;


    /**
     * 是否默认 0-否 1-是
     */
    @NotNull(message = "是否默认不能为空")
    private Integer isDefault;
}

    
    