package com.ewan.model.dto.comment;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Ewan
 * @date 2024/4/15
 */

@Data
public class DoCommentDto {

    /**
     * 博文id
     */
    @NotNull(message = "商品id不能为空")
    private Long productId;
    /**
     * 回复的内容
     */
    @NotEmpty(message = "评论内容不能为空")
    private String content;


    /**
     * 回复的评论id 为空表示 回复帖子
     */
    private Long answerId;

    /**
     * 评论pid 即 帖子下一级评论
     */
    private Long pid;
    /**
     * 订单id
     */
    @NotNull(message = "订单id不能为空")
    private Long orderId;
}

    
    