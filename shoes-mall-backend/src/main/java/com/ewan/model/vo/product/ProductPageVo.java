package com.ewan.model.vo.product;

import lombok.Data;

import java.util.List;

/**
 * @author Ewan
 * @date 2024/5/9
 */

@Data
public class ProductPageVo {
    /**
     * 商品id
     */
    private Long id;
    /**
     * 系统表品牌id
     */
    private Long systemBaseSettingBrandId;

    /**
     * 系统表品牌名称
     */
    private String systemBaseSettingBrandLabel;

    /**
     * 版型
     */
    private Long systemBaseSettingTypeId;

    /**
     * 版型名称
     */
    private String systemBaseSettingTypeLabel;

    /**
     * 原价价格扩大100
     */
    private Integer usedPrice;

    /**
     * 现价扩大100
     */
    private Integer nowPrice;

    /**
     * 标题
     */
    private String title;
    /**
     * 是否上架
     */
    private Integer isOn;

    /**
     * 剩余货量
     */
    private Integer restNum;

    /**
     * 封面
     */
    private String cover;


    /**
     * 系统表尺寸id list
     */
    private List<Long> systemBaseSettingSizeIdList;

    /**
     * 系统表颜色名称list
     */
    private List<String> systemBaseSettingSizeLabelList;

    /**
     * 颜色idList
     */
    private List<Long> systemBaseSettingColorIdList;

    /**
     * 颜色label list
     */
    private List<String> systemBaseSettingColorLabelList;
}

    
    