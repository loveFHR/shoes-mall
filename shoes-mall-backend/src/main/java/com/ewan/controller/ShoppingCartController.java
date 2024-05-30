package com.ewan.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.ewan.common.BaseResponse;
import com.ewan.common.ErrorCode;
import com.ewan.common.ResultUtils;
import com.ewan.constant.UserConstant;
import com.ewan.exception.BusinessException;
import com.ewan.model.dto.shoppingcart.ShoppingCartAddDto;
import com.ewan.model.dto.shoppingcart.ShoppingCartUpdateDto;
import com.ewan.model.entity.Product;
import com.ewan.model.entity.ShoppingCart;
import com.ewan.model.entity.User;
import com.ewan.model.vo.shoppingcart.ShoppingCartVo;
import com.ewan.service.ShoppingCartService;
import com.ewan.service.SystemBaseSettingService;
import com.ewan.service.UserService;
import com.ewan.utils.BeanUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 购物车
 *
 * @author Ewan
 * @date 2024/5/20
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {

    @Resource
    private UserService userService;

    @Resource
    private SystemBaseSettingService systemBaseSettingService;

    @Resource
    private ShoppingCartService shoppingCartService;


    /**
     * 添加商品到购物车
     *
     * @param dto dto
     * @return 购物车表的id
     */
    @PostMapping("/add")
    public BaseResponse<Long> add(@Validated @RequestBody ShoppingCartAddDto dto, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        Integer nowPrice = shoppingCartService.check(dto.getSystemBaseSettingColor(), dto.getSystemBaseSettingSize(), dto.getProductId()).getNowPrice();
        ShoppingCart sc = shoppingCartService.getOne(new QueryWrapper<ShoppingCart>().eq(ShoppingCart.COL_PRODUCT_ID, dto.getProductId())
                .eq(ShoppingCart.COL_SYSTEM_BASE_SETTING_COLOR, dto.getSystemBaseSettingColor())
                .eq(ShoppingCart.COL_SYSTEM_BASE_SETTING_SIZE, dto.getSystemBaseSettingSize()));

        if (sc != null) {
            sc.setPrice(sc.getPrice() + nowPrice * dto.getNum());
            sc.setNum(sc.getNum() + dto.getNum());
            return ResultUtils.success(sc.getId());
        }
        ShoppingCart shoppingCart = BeanUtils.copyProperties(dto, ShoppingCart.class);
        shoppingCart.setUserId(loginUser.getId());
        shoppingCart.setPrice(nowPrice * dto.getNum());
        shoppingCartService.save(shoppingCart);
        return ResultUtils.success(shoppingCart.getId());
    }


    /**
     * 修改购物车商品
     *
     * @param dto dto
     * @return 是否保存成功
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> update(@Validated @RequestBody ShoppingCartUpdateDto dto, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (shoppingCartService.getById(dto.getId()) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "所选购物车id不存在");
        }
        Integer nowPrice = shoppingCartService.check(dto.getSystemBaseSettingColor(), dto.getSystemBaseSettingSize(), dto.getProductId()).getNowPrice();
        ShoppingCart shoppingCart = BeanUtils.copyProperties(dto, ShoppingCart.class);
        shoppingCart.setUserId(loginUser.getId());
        shoppingCart.setPrice(nowPrice * dto.getNum());
        boolean save = shoppingCartService.updateById(shoppingCart);
        return ResultUtils.success(save);
    }


    /**
     * 修改购物车商品
     *
     * @param id 购物车表的id
     * @return 是否删除成功
     */
    @PostMapping("/delete/{id}")
    public BaseResponse<Boolean> delete(@PathVariable("id") Long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        ShoppingCart shoppingCart = shoppingCartService.getById(id);
        if (shoppingCart == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "所选购物车id不存在");
        }
        if (!shoppingCart.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您无权删除别人的购物车");
        }
        boolean removed = shoppingCartService.removeById(id);
        return ResultUtils.success(removed);
    }

    /**
     * 查询当前用户的购物车信息
     *
     * @return 购物车信息
     */
    @PostMapping("/query")
    public BaseResponse<List<ShoppingCartVo>> query(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        List<ShoppingCart> shoppingCartList = shoppingCartService.list(new QueryWrapper<ShoppingCart>().eq(ShoppingCart.COL_USER_ID, loginUser.getId()));
        if (shoppingCartList.size() == 0) {
            return ResultUtils.success(new ArrayList<>());
        }

        Set<Long> productIdSet = shoppingCartList.stream().map(ShoppingCart::getProductId).collect(Collectors.toSet());

        Map<Long, Product> productIdToProduct = SimpleQuery.keyMap(new LambdaQueryWrapper<Product>().in(Product::getId, productIdSet), Product::getId);

        Set<Long> sysIdSet = new HashSet<>();
        sysIdSet.addAll(shoppingCartList.stream().map(ShoppingCart::getSystemBaseSettingColor).collect(Collectors.toSet()));
        sysIdSet.addAll(shoppingCartList.stream().map(ShoppingCart::getSystemBaseSettingSize).collect(Collectors.toSet()));
        sysIdSet.addAll(productIdToProduct.values().stream().map(Product::getSystemBaseSettingBrandId).collect(Collectors.toSet()));
        sysIdSet.addAll(productIdToProduct.values().stream().map(Product::getSystemBaseSettingTypeId).collect(Collectors.toSet()));
        Map<Long, String> idToLabelMap = systemBaseSettingService.getIdToLabelMap(sysIdSet);

        List<ShoppingCartVo> voList = new ArrayList<>();
        for (ShoppingCart shoppingCart : shoppingCartList) {
            ShoppingCartVo vo = BeanUtils.copyProperties(shoppingCart, ShoppingCartVo.class);
            Optional.ofNullable(productIdToProduct.get(vo.getProductId())).ifPresent(r -> BeanUtils.copyProperties(r, vo));
            Optional.ofNullable(idToLabelMap.get(vo.getSystemBaseSettingTypeId())).ifPresent(vo::setSystemBaseSettingTypeLabel);
            Optional.ofNullable(idToLabelMap.get(vo.getSystemBaseSettingBrandId())).ifPresent(vo::setSystemBaseSettingBrandLabel);
            Optional.ofNullable(idToLabelMap.get(vo.getSystemBaseSettingSize())).ifPresent(vo::setSystemBaseSettingSizeLabel);
            Optional.ofNullable(idToLabelMap.get(vo.getSystemBaseSettingColor())).ifPresent(vo::setSystemBaseSettingColorLabel);
            vo.setId(shoppingCart.getId());
            voList.add(vo);
        }
        return ResultUtils.success(voList);
    }


    /**
     * 添加商品到购物车
     *
     * @param id id
     * @return 购物车表的id
     */
    @DeleteMapping("/delete/{id}")
    public BaseResponse<Boolean> add(@PathVariable("id") Long id, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        if (!UserConstant.ADMIN_ROLE.equals(loginUser.getRole())) {
            ShoppingCart shoppingCart = shoppingCartService.getById(id);
            if (!shoppingCart.getUserId().equals(loginUser.getId())) {
                throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "您无权删除别人的购物车");
            }
        }
        boolean res = shoppingCartService.removeById(id);
        return ResultUtils.success(res);
    }
}