package com.ewan.controller;

import cn.hutool.extra.emoji.EmojiUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.toolkit.SimpleQuery;
import com.ewan.annotation.AuthCheck;
import com.ewan.common.BaseResponse;
import com.ewan.common.ErrorCode;
import com.ewan.common.ResultUtils;
import com.ewan.constant.UserConstant;
import com.ewan.exception.BusinessException;
import com.ewan.model.dto.product.ProductAddDto;
import com.ewan.model.dto.product.ProductPageDto;
import com.ewan.model.dto.product.ProductUpdateDto;
import com.ewan.model.entity.*;
import com.ewan.model.vo.comment.CommentVo;
import com.ewan.model.vo.product.ProductDetailVo;
import com.ewan.model.vo.product.ProductPageVo;
import com.ewan.service.*;
import com.ewan.utils.BeanUtils;
import com.ewan.utils.ListUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 商品
 *
 * @author Ewan
 * @date 2024/5/14
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    private ProductService productService;

    @Resource
    private UserService userService;

    @Resource
    private ProductColorService productColorService;

    @Resource
    private ProductSizeService productSizeService;

    @Resource
    private SystemBaseSettingService systemBaseSettingService;

    @Resource
    private CommentsService commentsService;

    /**
     * 添加商品 (仅管理员)
     *
     * @param dto dto
     * @return 商品的id
     */
    @Transactional
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> add(@Validated @RequestBody ProductAddDto dto, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        //systemBaseSetting校验
        checkSystemBaseSettingValidated(dto.getSystemBaseSettingBrandId(), dto.getSystemBaseSettingTypeId(), dto.getSystemBaseSettingColorList(), dto.getSystemBaseSettingSizeList());

        Product product = BeanUtils.copyProperties(dto, Product.class);
        product.setRestNum(dto.getTotalNum());
        product.setCreateBy(loginUser.getId());
        productService.save(product);

        //插入颜色 尺寸
        this.insertColorSizeList(dto.getSystemBaseSettingColorList(), dto.getSystemBaseSettingSizeList(), product.getId());
        return ResultUtils.success(product.getId());
    }


    /**
     * 修改商品 (仅管理员)
     *
     * @param dto dto
     * @return 修改航拍的id
     */
    @Transactional
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> update(@Validated @RequestBody ProductUpdateDto dto) {
        Product product = productService.getById(dto.getId());
        if (product == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id所指商品不存在");
        }
        checkSystemBaseSettingValidated(dto.getSystemBaseSettingBrandId(), dto.getSystemBaseSettingTypeId(), dto.getSystemBaseSettingColorList(), dto.getSystemBaseSettingSizeList());
        Product update = BeanUtils.copyProperties(dto, Product.class);
        productService.updateById(update);
        //删除
        productColorService.remove(new QueryWrapper<ProductColor>().eq(ProductColor.COL_PRODUCTID, dto.getId()));
        productSizeService.remove(new QueryWrapper<ProductSize>().eq(ProductSize.COL_PRODUCTID, dto.getId()));
        //插入颜色 尺寸
        this.insertColorSizeList(dto.getSystemBaseSettingColorList(), dto.getSystemBaseSettingSizeList(), product.getId());
        return ResultUtils.success(product.getId());
    }

    private void insertColorSizeList(List<Long> systemBaseSettingColorList, List<Long> systemBaseSettingSizeList, Long productId) {
        //插入颜色
        List<ProductColor> colorList = new ArrayList<>();
        for (Long sysId : systemBaseSettingColorList) {
            ProductColor entity = new ProductColor();
            entity.setSystemBaseSettingId(sysId);
            entity.setProductId(productId);
            colorList.add(entity);
        }
        productColorService.saveBatch(colorList);
        //插入尺寸
        List<ProductSize> sizeList = new ArrayList<>();
        for (Long sysId : systemBaseSettingSizeList) {
            ProductSize entity = new ProductSize();
            entity.setSystemBaseSettingId(sysId);
            entity.setProductId(productId);
            sizeList.add(entity);
        }
        productSizeService.saveBatch(sizeList);
    }

    private void checkSystemBaseSettingValidated(Long systemBaseSettingBrandId, Long systemBaseSettingTypeId, List<Long> systemBaseSettingColorList, List<Long> systemBaseSettingSizeList) {
        if (systemBaseSettingColorList.size() == 0 || systemBaseSettingSizeList.size() == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "系统设置list的size不能为0");
        }
        List<Long> idList = new ArrayList<>();
        idList.add(systemBaseSettingBrandId);
        idList.add(systemBaseSettingTypeId);
        idList.addAll(systemBaseSettingColorList);
        idList.addAll(systemBaseSettingSizeList);
        List<SystemBaseSetting> list = systemBaseSettingService.list(new QueryWrapper<SystemBaseSetting>().in(SystemBaseSetting.COL_ID, idList));
        if (list.size() != idList.size()) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "系统设置id有不存在的请输入合法数据");
        }
    }

    /**
     * 批量上下架 (仅管理员)
     *
     * @param ids 商品ids
     * @return 是否成功
     */
    @PostMapping("/onDown")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> onDown(@RequestParam("ids") String ids) {
        String[] arrays = ids.split(",");
        if (arrays.length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ids 非法");
        }
        List<Product> updates = new ArrayList<>();
        Arrays.stream(arrays).forEach(r -> {
            Long id = Long.parseLong(r);
            Product product = productService.getById(id);
            if (product == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "id所指商品不存在");
            }
            product.setIsOn(product.getIsOn() == 0 ? 1 : 0);
            updates.add(product);
        });
        boolean res = productService.updateBatchById(updates);
        return ResultUtils.success(res);
    }


    /**
     * 批量删除
     *
     * @param ids str id
     */
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    @Transactional
    public BaseResponse<Boolean> delete(@RequestParam("ids") String ids) {
        String[] arrays = ids.split(",");
        if (arrays.length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ids 非法");
        }
        Set<Long> productIdList = new HashSet<>();
        Set<Long> colorList = new HashSet<>();
        Set<Long> sizeList = new HashSet<>();
        Arrays.stream(arrays).forEach(r -> {
            Long id = Long.parseLong(r);
            Product product = productService.getById(id);
            Assert.notNull(product, "需修改的商品不存在");
            colorList.addAll(productColorService.list(new QueryWrapper<ProductColor>().eq(ProductColor.COL_PRODUCTID, id)).stream().map(ProductColor::getId).toList());
            sizeList.addAll(productSizeService.list(new QueryWrapper<ProductSize>().eq(ProductSize.COL_PRODUCTID, id)).stream().map(ProductSize::getId).toList());
            productIdList.add(id);
        });
        boolean a = productService.removeBatchByIds(productIdList);
        boolean b = productColorService.removeBatchByIds(colorList);
        boolean c = productSizeService.removeBatchByIds(sizeList);
        return ResultUtils.success(a && b && c);
    }


    /**
     * 分页查看商品
     *
     * @param current 当前页
     * @param page    页大小
     * @param dto     dto
     * @return 分页查询结果
     */
    @PostMapping("/query/{current}/{page}")
    public BaseResponse<List<ProductPageVo>> page(@PathVariable("current") Long current, @PathVariable("page") Long page,
                                                  @RequestBody ProductPageDto dto, HttpServletRequest request) {
        int flag = 1;
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(dto.getName())) {
            queryWrapper.like(Product.COL_TITLE, dto.getName());
        }

        Set<Long> productQueryIdSet = new HashSet<>();
        Set<Long> systemBaseSettingIdSet = new HashSet<>();
        //查询size
        if (ListUtil.nonEmpty(dto.getSizeIdList())) {
            List<ProductSize> sizeList = productSizeService.list(new QueryWrapper<ProductSize>().in(ProductSize.COL_SYSTEM_BASE_SETTING_ID, dto.getSizeIdList()));
            productQueryIdSet.addAll(sizeList.stream().map(ProductSize::getProductId).collect(Collectors.toSet()));
            flag = -1;
        }

        //查询color
        if (ListUtil.nonEmpty(dto.getColorIdList())) {
            List<ProductColor> colorList = productColorService.list(new QueryWrapper<ProductColor>().in(ProductColor.COL_SYSTEM_BASE_SETTING_ID, dto.getColorIdList()));
            productQueryIdSet.addAll(colorList.stream().map(ProductColor::getProductId).collect(Collectors.toSet()));
            flag = -1;
        }

        //版型
        if (ListUtil.nonEmpty(dto.getTypeIdList())) {
            queryWrapper.in(Product.COL_SYSTEM_BASE_SETTING_TYPE_ID, dto.getTypeIdList());
        }
        //品牌
        if (ListUtil.nonEmpty(dto.getBrandIdList())) {
            queryWrapper.in(Product.COL_SYSTEM_BASE_SETTING_BRAND_ID, dto.getBrandIdList());
        }

        if (productQueryIdSet.size() == 0 && flag != 1) {
            return ResultUtils.success(new ArrayList<>());
        }

        if (productQueryIdSet.size() != 0) {
            queryWrapper.in(Product.COL_ID, productQueryIdSet);
        }
        //排序
        Integer order = dto.getOrder();
        queryWrapper.orderBy(order != null, order != null && order == 1, Product.COL_NOW_PRICE);
        //剩余货量 > 0
        queryWrapper.gt(Product.COL_REST_NUM, 0);
        //上架
        User user = userService.getLoginUserPermitNull(request);
        if (user == null || UserConstant.DEFAULT_ROLE.equals(user.getRole())) {
            queryWrapper.eq(Product.COL_IS_ON, 1);
        }
        Page<Product> pageRes = productService.page(new Page<>(current, page), queryWrapper);
        List<Product> records = pageRes.getRecords();
        if (records.size() == 0) {
            return ResultUtils.success(new ArrayList<>(), 0L);
        }
        List<Long> productIdList = records.stream().map(Product::getId).toList();
        //标签
        systemBaseSettingIdSet.addAll(records.stream().map(Product::getSystemBaseSettingBrandId).toList());
        systemBaseSettingIdSet.addAll(records.stream().map(Product::getSystemBaseSettingTypeId).toList());
        //颜色
        List<ProductColor> colorList = productColorService.list(new QueryWrapper<ProductColor>().in(ProductColor.COL_PRODUCTID, productIdList));
        Map<Long, List<ProductColor>> productColorMap = colorList.stream().collect(Collectors.groupingBy(ProductColor::getProductId));
        systemBaseSettingIdSet.addAll(colorList.stream().map(ProductColor::getSystemBaseSettingId).toList());
        //尺寸
        List<ProductSize> sizeList = productSizeService.list(new QueryWrapper<ProductSize>().in(ProductSize.COL_PRODUCTID, productIdList));
        Map<Long, List<ProductSize>> productSizeMap = sizeList.stream().collect(Collectors.groupingBy(ProductSize::getProductId));
        systemBaseSettingIdSet.addAll(sizeList.stream().map(ProductSize::getSystemBaseSettingId).toList());

        Map<Long, String> systemBaseSettingIdToLabel = systemBaseSettingService.getIdToLabelMap(systemBaseSettingIdSet);

        List<ProductPageVo> voList = new ArrayList<>();
        for (Product product : records) {
            ProductPageVo vo = BeanUtils.copyProperties(product, ProductPageVo.class);
            Optional.ofNullable(systemBaseSettingIdToLabel.get(vo.getSystemBaseSettingBrandId())).ifPresent(vo::setSystemBaseSettingBrandLabel);
            Optional.ofNullable(systemBaseSettingIdToLabel.get(vo.getSystemBaseSettingTypeId())).ifPresent(vo::setSystemBaseSettingTypeLabel);
            Optional.ofNullable(productColorMap.get(vo.getId())).ifPresent(list -> {
                List<String> res = new ArrayList<>();
                list.forEach(r -> Optional.ofNullable(systemBaseSettingIdToLabel.get(r.getSystemBaseSettingId())).ifPresent(res::add));
                vo.setSystemBaseSettingColorLabelList(res);
            });
            Optional.ofNullable(productSizeMap.get(vo.getId())).ifPresent(list -> {
                List<String> res = new ArrayList<>();
                list.forEach(r -> Optional.ofNullable(systemBaseSettingIdToLabel.get(r.getSystemBaseSettingId())).ifPresent(res::add));
                vo.setSystemBaseSettingSizeLabelList(res);
            });
            voList.add(vo);
        }
        return ResultUtils.success(voList, pageRes.getTotal());
    }


    /**
     * 商品详情
     *
     * @param id 商品id
     * @return 商品详情
     */
    @GetMapping("/detail/{id}")
    public BaseResponse<ProductDetailVo> detail(@PathVariable("id") Long id) {
        Product product = productService.getById(id);
        if (product == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "id所指商品不存在");
        }
        ProductDetailVo vo = BeanUtils.copyProperties(product, ProductDetailVo.class);
        Optional.ofNullable(userService.getById(vo.getCreateBy())).ifPresent(r -> vo.setCreateByName(r.getUsername()));
        Set<Long> systemBaseSettingIdSet = new HashSet<>();
        if (vo.getSystemBaseSettingBrandId() != null) {
            systemBaseSettingIdSet.add(vo.getSystemBaseSettingBrandId());
        }
        if (vo.getSystemBaseSettingTypeId() != null) {
            systemBaseSettingIdSet.add(vo.getSystemBaseSettingTypeId());
        }
        List<Long> productColorList = productColorService.list(new QueryWrapper<ProductColor>().eq(ProductColor.COL_PRODUCTID, id))
                .stream().map(ProductColor::getSystemBaseSettingId).toList();
        systemBaseSettingIdSet.addAll(productColorList);
        vo.setSystemBaseSettingColorList(productColorList);

        List<Long> productSizeList = productSizeService.list(new QueryWrapper<ProductSize>().eq(ProductSize.COL_PRODUCTID, id))
                .stream().map(ProductSize::getSystemBaseSettingId).toList();
        systemBaseSettingIdSet.addAll(productSizeList);
        vo.setSystemBaseSettingSizeList(productSizeList);

        //标签
        Map<Long, String> systemBaseSettingIdToLabel = systemBaseSettingService.getIdToLabelMap(systemBaseSettingIdSet);
        Optional.ofNullable(systemBaseSettingIdToLabel.get(vo.getSystemBaseSettingBrandId())).ifPresent(vo::setSystemBaseSettingBrandLabel);
        Optional.ofNullable(systemBaseSettingIdToLabel.get(vo.getSystemBaseSettingTypeId())).ifPresent(vo::setSystemBaseSettingTypeLabel);
        for (Long sid : vo.getSystemBaseSettingColorList()) {
            Optional.ofNullable(systemBaseSettingIdToLabel.get(sid)).ifPresent(r -> vo.getSystemBaseSettingColorLabelList().add(r));
        }

        for (Long sid : vo.getSystemBaseSettingSizeList()) {
            Optional.ofNullable(systemBaseSettingIdToLabel.get(sid)).ifPresent(r -> vo.getSystemBaseSettingSizeLabelList().add(r));
        }


        //评论
        List<CommentVo> commonList = new ArrayList<>();
        List<Comments> list = commentsService.list(new QueryWrapper<Comments>().eq(Comments.COL_PRODUCT_ID, id).orderByAsc(Comments.COL_CREATE_TIME));
        if (list.size() > 0) {
            Map<Long, Long> answerIdToUserId = list.stream().collect(Collectors.toMap(Comments::getId, Comments::getUserId));
            Set<Long> userIdSet = list.stream().filter(r -> r.getUserId() != null && r.getUserId() > 0).map(Comments::getUserId).collect(Collectors.toSet());
            userIdSet.addAll(list.stream().map(Comments::getUserId).collect(Collectors.toSet()));
            Map<Long, List<Comments>> pidMap = list.stream().filter(r -> r.getPid() > 0).collect(Collectors.groupingBy(Comments::getPid));

            Map<Long, User> userMap = new HashMap<>();
            if (userIdSet.size() > 0) {
                LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>().select(User::getId, User::getUsername, User::getAvatar).in(User::getId, userIdSet);
                userMap.putAll(SimpleQuery.keyMap(wrapper, User::getId));
            }
            list.forEach(comment -> {
                if (comment.getAnswerId() == 0) {
                    CommentVo commentVo = new CommentVo();
                    commentVo.setId(comment.getId());
                    commentVo.setProductId(comment.getProductId());
                    commentVo.setContent(EmojiUtil.toUnicode(comment.getContent()));
                    commentVo.setCreateTime(comment.getCreateTime());

                    Optional.ofNullable(userMap.get(comment.getUserId())).ifPresent(r -> {
                        commentVo.setUserId(r.getId());
                        commentVo.setUsername(r.getUsername());
                        commentVo.setUserAvatar(r.getAvatar());
                    });

                    //回复评论
                    List<Comments> comments = pidMap.get(comment.getId());
                    if (comments != null && comments.size() > 0) {
                        List<CommentVo> childVoList = new ArrayList<>();
                        for (Comments com : comments) {
                            CommentVo childVo = new CommentVo();

                            childVo.setId(com.getId());
                            childVo.setProductId(com.getProductId());
                            childVo.setContent(EmojiUtil.toUnicode(com.getContent()));
                            childVo.setCreateTime(com.getCreateTime());
                            if (UserConstant.ADMIN_ROLE.equals(userService.getById(com.getUserId()).getRole())){
                                childVo.setIsAdmin(1);
                            }
                            childVo.setPid(commentVo.getId());
                            Optional.ofNullable(userMap.get(com.getUserId())).ifPresent(r -> {
                                childVo.setUserId(r.getId());
                                childVo.setUsername(r.getUsername());
                                childVo.setUserAvatar(r.getAvatar());
                            });

                            childVo.setAnswerId(com.getAnswerId());
                            Long answerId = com.getAnswerId();
                            Optional.ofNullable(answerIdToUserId.get(answerId)).flatMap(r -> Optional.ofNullable(userMap.get(r))).ifPresent(p -> {
                                childVo.setCommentUserId(p.getId());
                                childVo.setCommentUsername(p.getUsername());
                                childVo.setCommentAvatar(p.getAvatar());
                            });
                            childVoList.add(childVo);
                        }
                        commentVo.setChildCommentList(childVoList);
                    }
                    commonList.add(commentVo);
                }
            });
        }
        vo.setCommonList(commonList);
        return ResultUtils.success(vo);
    }
}

    
    