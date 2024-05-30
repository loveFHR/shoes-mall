package com.ewan.model.vo.shoppingcart;

import lombok.Data;

/**
 * @author Ewan
 * @date 2024/5/20
 */
@Data
public class ShoppingCartVo {
    /**
     * id
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 商品id
     */
    private Long productId;

    /**
     * 鞋码
     */
    private Long systemBaseSettingSize;

    /**
     * 鞋码名
     */
    private String systemBaseSettingSizeLabel;

    /**
     * 颜色
     */
    private Long systemBaseSettingColor;

    /**
     * 颜色名
     */
    private String systemBaseSettingColorLabel;

    /**
     * 数量
     */
    private Integer num;

    /**
     * *100 总价
     */
    private Integer price;

    /**
     * *100单价 原价
     */
    private Integer usedPrice;

    /**
     * 现价
     */
    private Integer nowPrice;

    /**
     * 剩余货量
     */
    private Integer restNum;

    /**
     * 版型
     */
    private Long systemBaseSettingTypeId;
    /**
     * 版型名
     */
    private String systemBaseSettingTypeLabel;

    /**
     * 系统表品牌id
     */
    private Long systemBaseSettingBrandId;

    /**
     * 系统表品牌名
     */
    private String systemBaseSettingBrandLabel;

    /**
     * 标题
     */
    private String title;
    /**
     * 封面
     */
    private String cover;
}

    
    