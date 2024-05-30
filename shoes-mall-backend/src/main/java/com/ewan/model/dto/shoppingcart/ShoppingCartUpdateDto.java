package com.ewan.model.dto.shoppingcart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Ewan
 * @date 2024/5/20
 */
@Data
public class ShoppingCartUpdateDto {
    /**
     * id
     */
    @NotNull(message = "id不能为null")
    private Long id;

    /**
     * 商品id
     */
    @NotNull(message = "商品id不能为空")
    private Long productId;

    /**
     * 鞋码
     */
    @NotNull(message = "鞋码不能为null")
    private Long systemBaseSettingSize;

    /**
     * 颜色
     */
    @NotNull(message = "颜色不能为null")
    private Long systemBaseSettingColor;

    /**
     * 数量
     */
    @NotNull(message = "数量不能为null")
    private Integer num;

//    /**
//     * *100单价
//     */
//    @NotNull(message = "*100单价不能为null")
//    private Integer price;
}

    
    