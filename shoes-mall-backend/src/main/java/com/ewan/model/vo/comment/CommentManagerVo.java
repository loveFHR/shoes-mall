package com.ewan.model.vo.comment;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author Ewan
 * @date 2024/5/12
 */

@Data
public class CommentManagerVo {

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
     * 用户名字
     */
    private String commentUsername;

    /**
     * 评论用户头像
     */
    private String commentAvatar;


    /**
     * 博文id
     */
    private Long blogId;

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
    private Date createTime;

    /**
     * 帖子标题
     */
    private String title;


}

    
    