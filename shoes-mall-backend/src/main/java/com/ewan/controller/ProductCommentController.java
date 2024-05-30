package com.ewan.controller;

import cn.hutool.extra.emoji.EmojiUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ewan.common.BaseResponse;
import com.ewan.common.ErrorCode;
import com.ewan.common.ResultUtils;
import com.ewan.constant.UserConstant;
import com.ewan.exception.BusinessException;
import com.ewan.model.dto.comment.DoCommentDto;
import com.ewan.model.entity.Comments;
import com.ewan.model.entity.Order;
import com.ewan.model.entity.User;
import com.ewan.service.CommentsService;
import com.ewan.service.OrderService;
import com.ewan.service.ProductService;
import com.ewan.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


/**
 * 商品评论
 *
 * @author Ewan
 */
@RestController
@RequestMapping("/comment")
@Validated
@Slf4j
public class ProductCommentController {
    @Resource
    private CommentsService commentsService;

    @Resource
    private ProductService productService;

    @Resource
    private UserService userService;
    @Resource
    private OrderService orderService;

    /**
     * 评论 支持emoji
     *
     * @param dto 请求参数
     * @return 是否评论成功
     */
    @PostMapping("/do")
    @Transactional
    public BaseResponse<Boolean> doComment(@Validated @RequestBody DoCommentDto dto, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (!UserConstant.ADMIN_ROLE.equals(loginUser.getRole())) {
            List<Order> list = orderService.list(new QueryWrapper<Order>().eq(Order.COL_USER_ID, loginUser.getId()).eq(Order.COL_PRODUCT_ID, dto.getProductId()));
            if (list.size() == 0) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "清先购买商品后进行评论");
            }
        }


        Long userId = loginUser.getId();
        Long blogId = dto.getProductId();
        String content = dto.getContent();
        content = EmojiUtil.toAlias(content);


        if (productService.getById(blogId) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id所指商品不存在");
        }

        Comments blogComments = new Comments();
        if (dto.getAnswerId() != null && dto.getPid() != null) {
            if (commentsService.getById(dto.getAnswerId()) == null || commentsService.getById(dto.getPid()) == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "answerId或pid的评论不存在");
            }
            blogComments.setAnswerId(dto.getAnswerId());
            blogComments.setPid(dto.getPid());
        }

        blogComments.setUserId(userId);
        blogComments.setProductId(blogId);
        blogComments.setContent(content);
        blogComments.setOrderId(dto.getOrderId());
        boolean save = commentsService.save(blogComments);
        userService.updateById(loginUser);
        Order order = new Order();
        order.setId(dto.getOrderId());
        order.setOrderStatus(5);
        boolean res = orderService.updateById(order);
        return ResultUtils.success(save && res);
    }


    /**
     * 删除评论
     *
     * @return 是否删除成功
     */
    @GetMapping("/delete/{id}")
    public BaseResponse<Boolean> deleteComment(@Validated @Positive(message = "评论id应>0") @PathVariable("id") Long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Comments comments = commentsService.getById(id);
        if (comments == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "此id评论不存在");
        }
        if (!Objects.equals(comments.getUserId(), loginUser.getId()) && !UserConstant.ADMIN_ROLE.equals(loginUser.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您无权删除别人的评论");
        }
        if (comments.getPid() == 0) {
            //删除下面所有评论
            commentsService.remove(new QueryWrapper<Comments>().eq(Comments.COL_PID, comments.getId()));
        } else {
            //删除当前评论 以及下面的子评论
            List<Long> commentsIdList = new ArrayList<>(Collections.singletonList(comments.getPid()));
            commentsService.findAllChildCommentId(commentsIdList, Collections.singletonList(comments.getId()));
            commentsService.removeBatchByIds(commentsIdList);
        }
        return ResultUtils.success(true);
    }
}
