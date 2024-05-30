package com.ewan.model.dto.systembasesetting;

/**
 * @author Ewan
 * @date 2024/5/10
 */

import lombok.Data;

import java.util.Date;

/**
 * 系统设置
 */
@Data
public class QuerySystemBaseSettingVo  {
    /**
     * 主键
     */
    private Long id;

    /**
     * 类型 1帖子分区 2商品分区
     */
    private Integer type;

    /**
     * 标签名/类型名
     */
    private String label;

    /**
     * 已删除(0否 1是)
     */
    private Boolean deleted;

    /**
     * 操作人
     */
    private Long createBy;

    /**
     * 状态 0正常 1ban
     */
    private Integer ban;

    /**
     * 操作人姓名
     */
    private String nickName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

}