package com.ewan.model.dto.product;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * @author Ewan
 * @date 2024/5/9
 */
@Data
public class ProductUpdateDto {

    /**
     * id
     */
    @NotNull(message = "id不能为null")
    private Long id;

    /**
     * 系统表品牌id
     */
    @NotNull(message = "品牌不能为null")
    private Long systemBaseSettingBrandId;

    /**
     * 版型
     */
    @NotNull(message = "版型不能为null")
    private Long systemBaseSettingTypeId;

    /**
     * 上下架 0-下架 1-上架 默认0
     */
//    @NotNull(message = "上下架 0-下架 1-上架 默认0不能为null")
    private Integer isOn;


    /**
     * 原价价格扩大100
     */
    private Integer usedPrice;

    /**
     * 现价扩大100
     */
    @NotNull(message = "现价扩大100不能为null")
    private Integer nowPrice;

    /**
     * 标题
     */
    @Size(max = 128, message = "标题最大长度要小于 128")
    @NotBlank(message = "标题不能为空")
    private String title;

    /**
     * 描述
     */
    @NotBlank(message = "描述不能为空")
    private String desc;

    /**
     * 总货存量
     */
    @NotNull(message = "总货存量不能为null")
    private Integer totalNum;
    /**
     * 剩余货量
     */
    @NotNull(message = "剩余货量不能为null")
    private Integer restNum;

    /**
     * 封面
     */
    @Size(max = 128, message = "封面最大长度要小于 32")
    @NotBlank(message = "封面不能为空")
    private String cover;

    /**
     * 详情主图片 ,分割
     */
    @Size(max = 1024, message = "详情主图片 ,分割最大长度要小于 1024")
    @NotBlank(message = "详情主图片 ,分割不能为空")
    private String mainPicture;



    /**
     * 系统基础设置 颜色
     */
    @NotNull(message = "系统基础设置 颜色 不能为空")
    private List<Long> systemBaseSettingColorList;

    /**
     * 系统基础设置 尺寸
     */
    @NotNull(message = "系统基础设置 尺寸 不能为空")
    private List<Long> systemBaseSettingSizeList;
}

    
    