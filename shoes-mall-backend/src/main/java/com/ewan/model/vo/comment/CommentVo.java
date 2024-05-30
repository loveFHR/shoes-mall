package com.ewan.model.vo.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Ewan
 * @date 2024/4/15
 */

@Data
public class CommentVo {

    public CommentVo() {
        this.pid = 0L;
        this.isAdmin = 1;
    }

    /**
     * 主键
     */
    private Long id;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名字
     */
    private String username;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 评论用户id
     */
    private Long commentUserId;

    /**
     * 评论用户名字
     */
    private String commentUsername;

    /**
     * 评论用户头像
     */
    private String commentAvatar;

    /**
     * 博文id
     */
    private Long productId;

    /**
     * 回复的评论id
     */
    private Long answerId;

    /**
     * 回复的内容
     */
    private String content;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 父级评论id
     */
    private Long pid;

    /**
     * 下级评论
     */
    private List<CommentVo> childCommentList;

    /**
     * 是否是商家（管理员）
     */
    private Integer isAdmin;

}

    
    