package com.ewan.model.dto.order;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

/**
 * @author Ewan
 * @date 2024/5/10
 */

@Data
public class OrderAddDto {


    /**
     * 商品id
     */
    @NotNull(message = "商品id不能为空")
    private Long productId;


    /**
     * 颜色id
     */
    @NotNull(message = "商品颜色不能为空")
    private Long systemBaseSettingColorId;

    /**
     * 尺码id
     */
    @NotNull(message = "商品尺码不能为空")
    private Long systemBaseSettingSizeId;

    /**
     * 价格
     */
    @NotNull(message = "价格不能为空")
    private Integer price;

    /**
     * 地址id
     */
    @NotNull(message = "地址不能为空")
    private Long addressId;

    /**
     * 购物车id 如果是通过购物车来生成的订单-那么必填 否则非必填
     */
    private Long shoppingCartId;
}

    
    