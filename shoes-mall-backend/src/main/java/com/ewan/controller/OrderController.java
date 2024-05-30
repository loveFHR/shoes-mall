package com.ewan.controller;

import cn.hutool.extra.emoji.EmojiUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ewan.annotation.AuthCheck;
import com.ewan.common.BaseResponse;
import com.ewan.common.ErrorCode;
import com.ewan.common.ResultUtils;
import com.ewan.constant.UserConstant;
import com.ewan.exception.BusinessException;
import com.ewan.model.dto.order.OrderAddDto;
import com.ewan.model.dto.order.OrderDeleteDto;
import com.ewan.model.entity.*;
import com.ewan.model.enums.OrderStatusEnum;
import com.ewan.model.vo.address.UserAddressVo;
import com.ewan.model.vo.order.MyOrderNumVo;
import com.ewan.model.vo.order.OrderDetail;
import com.ewan.model.vo.order.OrderPageVo;
import com.ewan.model.vo.order.WaitReplyProduct;
import com.ewan.service.*;
import com.ewan.utils.BeanUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 订单
 *
 * @author Ewan
 * @date 2024/5/10
 */


@Slf4j
@Validated
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @Resource
    private ProductService productService;

    @Resource
    private ShoppingCartService shoppingCartService;

    @Resource
    private UserService userService;

    @Resource
    private UserAddressService userAddressService;

    @Resource
    private SystemBaseSettingService systemBaseSettingService;

    @Resource
    private CommentsService commentsService;

    /**
     * 生成订单(商品页面)
     *
     * @param dtoList 批量添加参数
     * @return 生成的订单id（表中主键）
     */
    @PostMapping("/add")
    @Transactional
    public BaseResponse<String> add(@RequestBody @Validated List<OrderAddDto> dtoList, HttpServletRequest request) {
        // 创建SimpleDateFormat对象，指定日期时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // 获取当前日期时间
        Date now = new Date();

        // 格式化日期时间为字符串
        String formattedDateTime = sdf.format(now);
        String orderNo = formattedDateTime + System.currentTimeMillis();
        User loginUser = userService.getLoginUser(request);


        List<Long> shoppingCartList = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();
        for (OrderAddDto dto : dtoList) {
            Product product = shoppingCartService.check(dto.getSystemBaseSettingColorId(), dto.getSystemBaseSettingSizeId(), dto.getProductId());
            if (product.getRestNum() < 1) {
                throw new BusinessException(ErrorCode.OPERATION_ERROR, product.getTitle() + "---小子手速慢了，没货了奥");
            }
            //订单数量-1
            product.setRestNum(product.getRestNum() - 1);
            boolean res = productService.updateById(product);
            if (!res) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "锁单失败，请重试");
            }

            UserAddress userAddress = userAddressService.getById(dto.getAddressId());
            if (userAddress == null || !Objects.equals(userAddress.getUserId(), loginUser.getId())) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "地址不存在");
            }
            Order orders = new Order();
            orders.setProductId(dto.getProductId());
            orders.setSystemBaseSettingColorId(dto.getSystemBaseSettingColorId());
            orders.setSystemBaseSettingSizeId(dto.getSystemBaseSettingSizeId());
            orders.setUserId(loginUser.getId());
            orders.setOrderNo(orderNo);
            orders.setTotalPrice(dto.getPrice());
            orders.setAddressId(dto.getAddressId());
            orderList.add(orders);
            //删除购物车
            if (dto.getShoppingCartId() != null && dto.getShoppingCartId() > 0) {
                shoppingCartList.add(dto.getShoppingCartId());
            }
        }
        boolean res = orderService.saveBatch(orderList);
        if (!res) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "保存订单失败");
        }
        //删除购物车
        if (shoppingCartList.size() > 0) {
            shoppingCartService.removeBatchByIds(shoppingCartList);
        }
        return ResultUtils.success(orderNo);
    }

    @GetMapping("/gen")
    public void gen() {
        List<Order> list = orderService.list();
        // 创建SimpleDateFormat对象，指定日期时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // 获取当前日期时间
        Date now = new Date();
        // 格式化日期时间为字符串
        String formattedDateTime = sdf.format(now);
        String orderNo = formattedDateTime + System.currentTimeMillis();
        LocalDateTime localDateTime = LocalDateTime.now();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            for (Order order : list) {
                order.setOrderNo(orderNo);
                order.setId(null);
                int num = random.nextInt(110);
                Date date = Date.from(localDateTime.plusDays(-num).atZone(ZoneId.systemDefault()).toInstant());
                order.setCreateTime(date);
            }
            orderService.saveBatch(list);
        }
    }

    /*
      正常流程
      1 客户生成订单-0 待支付
      2 客户付款成功-2 待发货
      3 商家点击发货-3 待收货
      4 客户点击收货-4 待评价
      5 客户完成评价-5 交易成功
      退款关闭流程
      1 客户未支付前 手动点击关闭 -1 手动关闭 结束
      2 客户支付后点击申请退款 -2 申请退款
      3 管理同意退款 -3 退货订单 结束
     */


    /**
     * 订单分页查询
     *
     * @param findMyOrder 1查自己的 0查全部（需要管理员权限）
     * @param type        订单状态:0.待支付  2.待发货 3.待收货 4.待评价 5.交易成功 -1.手动关闭 -2.申请退款 -3.退货订单 13 全部 14待回复订单
     * @param current     当前页
     * @param page        页大小
     */
    @PostMapping("/query/{current}/{page}")
    public BaseResponse<List<OrderPageVo>> queryOrderPage(@PathVariable("current") Long current,
                                                          @PathVariable("page") Long page,
                                                          @RequestParam("type") Integer type,
                                                          @RequestParam("findMyOrder") Integer findMyOrder,
                                                          String name,
                                                          HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<Order> wrapper = new QueryWrapper<>();
        if (findMyOrder == 0 && !UserConstant.ADMIN_ROLE.equals(loginUser.getRole())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您无权查看所有人权限");
        }
        if (findMyOrder == 1) {
            wrapper.eq(Order.COL_USER_ID, loginUser.getId());
        }
        if (StringUtils.isNotBlank(name)) {
            wrapper.like(Order.COL_ORDER_NO, name);
        }
        if (type != null && type != 13 && type != 14) {
            wrapper.eq(Order.COL_ORDER_STATUS, type);
        }

        if (type != null && type == 14) {
            List<Long> waitReplyOrderIdList = orderService.getBaseMapper().getWaitReplyOrderIdList();
            if (waitReplyOrderIdList.size() == 0) {
                return ResultUtils.success(new ArrayList<>());
            }
            wrapper.in(Order.COL_ID, waitReplyOrderIdList);
        }
        Page<Order> pageRes = orderService.page(new Page<>(current, page), wrapper);
        List<Order> records = pageRes.getRecords();
        if (records.size() == 0) {
            return ResultUtils.success(new ArrayList<>(), pageRes.getTotal());
        }
        Set<Long> productIdSet = records.stream().map(Order::getProductId).collect(Collectors.toSet());
        List<Product> productList = productService.list(new QueryWrapper<Product>().in(Product.COL_ID, productIdSet));
        Set<Long> systemBaseSettingIdSet = records.stream().map(Order::getSystemBaseSettingColorId).collect(Collectors.toSet());
        systemBaseSettingIdSet.addAll(records.stream().map(Order::getSystemBaseSettingSizeId).collect(Collectors.toSet()));

        Map<Long, String> idToLabelMap = systemBaseSettingService.getIdToLabelMap(systemBaseSettingIdSet);
        Map<Long, String> idToTitleMap = productList.stream().collect(Collectors.toMap(Product::getId, Product::getTitle));
        Map<Long, String> idToCoverMap = productList.stream().collect(Collectors.toMap(Product::getId, Product::getCover));

        List<OrderPageVo> res = new ArrayList<>();
        for (Order record : records) {
            OrderPageVo orderPageVo = BeanUtils.copyProperties(record, OrderPageVo.class);
            orderPageVo.setPrice(record.getTotalPrice());
            Optional.ofNullable(idToTitleMap.get(record.getProductId())).ifPresent(orderPageVo::setProductName);
            Optional.ofNullable(idToCoverMap.get(record.getProductId())).ifPresent(orderPageVo::setCover);
            UserAddressVo addressVo = userAddressService.queryById(record.getAddressId(), request);
            orderPageVo.setAddressVo(addressVo);

            Optional.ofNullable(idToLabelMap.get(orderPageVo.getSystemBaseSettingSizeId())).ifPresent(orderPageVo::setSystemBaseSettingSizeLabel);
            Optional.ofNullable(idToLabelMap.get(orderPageVo.getSystemBaseSettingColorId())).ifPresent(orderPageVo::setSystemBaseSettingColorLabel);

            if (type != null && type == 14) {
                Comments comment = commentsService.getOne(new QueryWrapper<Comments>().eq(Comments.COL_ORDER_ID, record.getId()).eq(Comments.COL_PID, 0)
                        .orderByDesc(Comments.COL_CREATE_TIME).last("limit 1"));
                orderPageVo.setCommentId(comment.getId());
                orderPageVo.setCommentTime(comment.getCreateTime());
                orderPageVo.setContent(EmojiUtil.toUnicode(comment.getContent()));
            }
            if (findMyOrder == 1) {
                List<Comments> commentsList = commentsService.list(new QueryWrapper<Comments>().eq(Comments.COL_USER_ID, loginUser.getId())
                        .eq(Comments.COL_PID, 0).eq(Comments.COL_ORDER_ID, orderPageVo.getId()));
                if (commentsList.size() > 0) {
                    orderPageVo.setIsComment(1);
                }
            }
            res.add(orderPageVo);
        }
        return ResultUtils.success(res, pageRes.getTotal());
    }


    /**
     * 批量发货
     *
     * @param ids str id-order表
     */
    @PostMapping("/send")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> send(@RequestParam("ids") String ids) {
        return orderService.updateStatus(ids, OrderStatusEnum.WAIT_RECEIVE);
    }

    /**
     * 批量收货
     *
     * @param ids str id-order表
     */
    @PostMapping("/receive")
    public BaseResponse<Boolean> receive(@RequestParam("ids") String ids) {
        return orderService.updateStatus(ids, OrderStatusEnum.WAIT_COMMENT);
    }

    /**
     * 批量取消订单（直接取消）
     *
     * @param ids str id-order表
     */
    @PostMapping("/cancel")
    public BaseResponse<Boolean> cancel(@RequestParam("ids") String ids) {
        return orderService.updateStatus(ids, OrderStatusEnum.CANCEL);
    }


    /**
     * 批量申请退款
     *
     * @param ids str id-order表
     */
    @PostMapping("/applyRefund")
    public BaseResponse<Boolean> applyRefund(@RequestParam("ids") String ids) {
        return orderService.updateStatus(ids, OrderStatusEnum.APPLY_REFUND);
    }


    /**
     * 统计订单
     */
    @GetMapping("/myOrderNum")
    public BaseResponse<MyOrderNumVo> myOrderNum(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        MyOrderNumVo vo = new MyOrderNumVo();
        vo.setWaitComment(orderService.count(new QueryWrapper<Order>()
                .eq(Order.COL_USER_ID, loginUser.getId()).eq(Order.COL_ORDER_STATUS, OrderStatusEnum.WAIT_COMMENT.getValue())));
        vo.setWaitReceive(orderService.count(new QueryWrapper<Order>()
                .eq(Order.COL_USER_ID, loginUser.getId()).eq(Order.COL_ORDER_STATUS, OrderStatusEnum.WAIT_RECEIVE.getValue())));
        vo.setWaitPay(orderService.count(new QueryWrapper<Order>()
                .eq(Order.COL_USER_ID, loginUser.getId()).eq(Order.COL_ORDER_STATUS, OrderStatusEnum.WAIT_PAY.getValue())));
        vo.setWaitSend(orderService.count(new QueryWrapper<Order>()
                .eq(Order.COL_USER_ID, loginUser.getId()).eq(Order.COL_ORDER_STATUS, OrderStatusEnum.WAIT_SEND.getValue())));
        return ResultUtils.success(vo);
    }


    /**
     * 删除订单
     *
     * @param dto 参数
     * @return 是否成功
     */
    @DeleteMapping("/delete")
    public BaseResponse<Boolean> delete(@RequestBody OrderDeleteDto dto, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<Long> deleteList = new ArrayList<>();
        for (Long id : dto.getOrderIdList()) {
            Order order = orderService.getById(id);
            if (!order.getUserId().equals(loginUser.getId()) && !UserConstant.ADMIN_ROLE.equals(loginUser.getRole())) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权删除");
            }
            deleteList.add(id);
        }
        boolean res = orderService.removeBatchByIds(deleteList);
        return ResultUtils.success(res);
    }

    /**
     * 订单详情
     *
     * @param id 订单id
     */
    @GetMapping("/orderDetail/{id}")
    public BaseResponse<OrderDetail> orderDetail(@PathVariable("id") String id) {
        Order order = orderService.getById(id);
        if (order == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "当前订单id不存在");
        }
        OrderDetail vo = BeanUtils.copyProperties(order, OrderDetail.class);
        User user = userService.getById(order.getUserId());
        BeanUtils.copyProperties(user, vo);
        UserAddress ud = userAddressService.getById(order.getAddressId());
        BeanUtils.copyProperties(ud, vo);
        vo.setAddress(ud.getProvince() + ud.getCity() + ud.getDistrict() + ud.getAddress());
        Product product = productService.getById(order.getProductId());
        BeanUtils.copyProperties(product, vo);
        vo.setOrderId(order.getId());
        Optional.ofNullable(systemBaseSettingService.getById(order.getSystemBaseSettingColorId())).ifPresent(r -> vo.setColorLabel(r.getLabel()));
        Optional.ofNullable(systemBaseSettingService.getById(order.getSystemBaseSettingSizeId())).ifPresent(r -> vo.setSizeLabel(r.getLabel()));
        return ResultUtils.success(vo);
    }
}

    
    