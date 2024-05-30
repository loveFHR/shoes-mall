package com.ewan.model.vo.product;

import com.ewan.model.vo.comment.CommentVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Ewan
 * @date 2024/5/20
 */
@Data
public class ProductDetailVo {
    public ProductDetailVo() {
        this.systemBaseSettingColorLabelList = new ArrayList<>();
        this.systemBaseSettingSizeLabelList = new ArrayList<>();
        this.commonList = new ArrayList<>();
    }

    /**
     * id
     */
    private Long id;


    /**
     * 系统表品牌id
     */
    private Long systemBaseSettingBrandId;

    /**
     * 品牌名
     */
    private String systemBaseSettingBrandLabel;

    /**
     * 版型
     */
    private Long systemBaseSettingTypeId;


    /**
     * 版型名
     */
    private String systemBaseSettingTypeLabel;
    /**
     * 上下架 0-下架 1-上架 默认0
     */
    private Integer isOn;

    /**
     * 创建用户id
     */
    private Long createBy;

    /**
     * 创建用户名
     */
    private String createByName;
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
     * 描述
     */
    private String desc;

    /**
     * 总货存量
     */
    private Integer totalNum;

    /**
     * 剩余货量
     */
    private Integer restNum;

    /**
     * 封面
     */
    private String cover;

    /**
     * 详情主图片 ,分割
     */
    private String mainPicture;


    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date updateTime;


    /**
     * 尺寸idList
     */
    private List<Long> systemBaseSettingSizeList;

    /**
     * 尺寸名list
     */
    private List<String> systemBaseSettingSizeLabelList;

    /**
     * 颜色id list
     */
    private List<Long> systemBaseSettingColorList;
    /**
     * 颜色label list
     */
    private List<String> systemBaseSettingColorLabelList;


    /**
     * 评论list
     */
    private List<CommentVo> commonList;

}

    
    