package com.ewan.model.dto.product;

import lombok.Data;

import javax.swing.text.Style;
import java.util.List;

/**
 * @author Ewan
 * @date 2024/5/20
 */
@Data
public class ProductPageDto {

    /**
     * 标题模糊搜索
     */
    private String name;
    /**
     * 尺寸
     */
    private List<Long> sizeIdList;
    /**
     * 颜色
     */
    private List<Long> colorIdList;
    /**
     * 版型
     */
    private List<Long> typeIdList;
    /**
     * 品牌
     */
    private List<Long> brandIdList;
    /**
     * 是否排序
     */
    private Integer order;
}

    
    