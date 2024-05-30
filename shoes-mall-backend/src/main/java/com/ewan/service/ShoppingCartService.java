package com.ewan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ewan.common.ErrorCode;
import com.ewan.exception.BusinessException;
import com.ewan.model.entity.Product;
import com.ewan.model.entity.ProductColor;
import com.ewan.model.entity.ProductSize;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ewan.mapper.ShoppingCartMapper;
import com.ewan.model.entity.ShoppingCart;

@Service
public class ShoppingCartService extends ServiceImpl<ShoppingCartMapper, ShoppingCart> {
    @Resource
    private ProductService productService;
    @Resource
    private ProductColorService productColorService;
    @Resource
    private ProductSizeService productSizeService;


    public Product check(Long sysColorId, Long sysSize, Long productId) {
        Product product = productService.getById(productId);
        if (product == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "所选商品不存在");
        }
        if (product.getIsOn() != 1) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "所选商品未上架");

        }
        if (productSizeService.getOne(new QueryWrapper<ProductSize>().eq(ProductSize.COL_SYSTEM_BASE_SETTING_ID, sysSize)
                .eq(ProductSize.COL_PRODUCTID, productId)) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "所选商品此尺码不存在");
        }
        if (productColorService.getOne(new QueryWrapper<ProductColor>().eq(ProductColor.COL_SYSTEM_BASE_SETTING_ID, sysColorId)
                .eq(ProductColor.COL_PRODUCTID, productId)) == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "所选商品此尺码不存在");
        }

        return product;
    }
}
