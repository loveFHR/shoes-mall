package com.ewan.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ewan.common.BaseResponse;
import com.ewan.common.ResultUtils;
import com.ewan.constant.SystemBaseSettingTypeConstant;
import com.ewan.mapper.*;
import com.ewan.model.dto.systembasesetting.QuerySystemBaseSettingVo;
import com.ewan.model.dto.systembasesetting.SystemBaseSettingDTO;
import com.ewan.model.entity.*;
import com.ewan.utils.BeanUtils;
import com.ewan.utils.ListUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ewan
 */
@Service
public class SystemBaseSettingService extends ServiceImpl<SystemBaseSettingMapper, SystemBaseSetting> {

    @Resource
    private SystemBaseSettingMapper systemBaseSettingMapper;

    @Resource
    private ProductMapper productMapper;
    @Resource
    private UserMapper userMapper;

    @Resource
    private ProductColorMapper productColorMapper;


    @Resource
    private ProductSizeMapper productSizeMapper;

    /**
     * 添加标签
     *
     * @param dto 添加系统基础设置参数
     * @param id  登录用户id
     */
    public boolean add(SystemBaseSettingDTO dto, Long id) {
        SystemBaseSetting systemBaseSetting = BeanUtils.copyProperties(dto, SystemBaseSetting.class);
        systemBaseSetting.setCreateBy(id);
        int insert = baseMapper.insert(systemBaseSetting);
        return insert > 0;
    }


    /**
     * 删除设置
     *
     * @param list 需删除设置
     */
    public boolean delete(List<SystemBaseSetting> list) {
        list.forEach(r -> {
            Integer type = r.getType();
            Long id = r.getId();

            //品牌
            if (type.equals(SystemBaseSettingTypeConstant.BRAND)) {
                List<Product> productList = productMapper.selectList(new QueryWrapper<Product>().eq(Product.COL_SYSTEM_BASE_SETTING_BRAND_ID, id));
                Assert.isTrue(productList.size() == 0, "现有该品牌的商品，请先删除该品牌的所有商品，再进行次操作");
            }
            //鞋类型
            if (type.equals(SystemBaseSettingTypeConstant.TYPE)) {
                List<Product> productList = productMapper.selectList(new QueryWrapper<Product>().eq(Product.COL_SYSTEM_BASE_SETTING_BRAND_ID, id));
                Assert.isTrue(productList.size() == 0, "现有该鞋类型的商品，请先删除该鞋版型所有商品，再进行次操作");
            }

            //鞋码
            if (type.equals(SystemBaseSettingTypeConstant.SIZE)) {
                List<ProductSize> productSizeList = productSizeMapper.selectList(new QueryWrapper<ProductSize>().eq(ProductSize.COL_SYSTEM_BASE_SETTING_ID, id));
                Assert.isTrue(productSizeList.size() == 0, "现有该鞋码的商品，请先删除该鞋码的所有商品，再进行次操作");
            }

            //颜色
            if (type.equals(SystemBaseSettingTypeConstant.COLOR)) {
                List<ProductColor> productSizeList = productColorMapper.selectList(new QueryWrapper<ProductColor>().eq(ProductColor.COL_SYSTEM_BASE_SETTING_ID, id));
                Assert.isTrue(productSizeList.size() == 0, "现有该鞋码的商品，请先删除该鞋码的所有商品，再进行次操作");
            }
        });

        //类型 1-鞋码 2-颜色 3-品牌 4-鞋类型
        int del = systemBaseSettingMapper.deleteBatchIds(list);
        return del > 0;
    }

    public BaseResponse<List<QuerySystemBaseSettingVo>> queryPage(Long type, Long current, Long page, String label) {
        QueryWrapper<SystemBaseSetting> systemBaseSettingQueryWrapper = new QueryWrapper<>();
        if (type != -1) {
            systemBaseSettingQueryWrapper.eq(SystemBaseSetting.COL_TYPE, type);
        }
        if (StringUtils.isNotBlank(label)) {
            systemBaseSettingQueryWrapper.like(SystemBaseSetting.COL_LABEL, label);
        }
        Page<SystemBaseSetting> pageRes = this.page(new Page<>(current, page), systemBaseSettingQueryWrapper);
        List<SystemBaseSetting> systemBaseSettingList = pageRes.getRecords();
        if (systemBaseSettingList.size() == 0) {
            return ResultUtils.success(new ArrayList<>(), 0L);
        }
        List<QuerySystemBaseSettingVo> voList = BeanUtils.copyToList(systemBaseSettingList, QuerySystemBaseSettingVo.class);
        List<Long> userIdList = systemBaseSettingList.stream().map(SystemBaseSetting::getCreateBy).toList();
        if (userIdList.size() > 0) {
            Map<Long, String> map = userMapper.selectList(new QueryWrapper<User>().in(User.COL_ID, userIdList)).stream().collect(Collectors.toMap(User::getId, User::getUsername));
            voList.forEach(r -> Optional.ofNullable(map.get(r.getCreateBy())).ifPresent(r::setNickName));
        }
        return ResultUtils.success(voList, pageRes.getTotal());
    }

    public Map<Long, String> getIdToLabelMap(Collection<?> collection) {
        Map<Long, String> systemBaseSettingIdToLabel = new HashMap<>();
        if (ListUtil.nonEmpty(collection)) {
            systemBaseSettingIdToLabel.putAll(this.baseMapper.selectList(new QueryWrapper<SystemBaseSetting>().in(SystemBaseSetting.COL_ID, collection))
                    .stream().collect(Collectors.toMap(SystemBaseSetting::getId, SystemBaseSetting::getLabel)));
        }
        return systemBaseSettingIdToLabel;
    }
}
