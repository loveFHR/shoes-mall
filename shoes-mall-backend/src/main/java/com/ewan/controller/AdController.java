package com.ewan.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ewan.annotation.AuthCheck;
import com.ewan.common.BaseResponse;
import com.ewan.common.ErrorCode;
import com.ewan.common.ResultUtils;
import com.ewan.constant.UserConstant;
import com.ewan.exception.BusinessException;
import com.ewan.model.dto.ad.AdAddDto;
import com.ewan.model.dto.ad.AdUpdateDto;
import com.ewan.model.entity.Ad;
import com.ewan.model.entity.Product;
import com.ewan.model.entity.ProductColor;
import com.ewan.model.entity.ProductSize;
import com.ewan.service.AdService;
import com.ewan.utils.BeanUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
/**
 * 广告厂家
 *
 * @author cx
 * @date 2024/4/28
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/ad")
public class AdController {
    @Resource
    private AdService adService;
    /**
     * 添加广告厂家
     *
     * @param dto dto
     * @return 表主键id
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> add(@RequestBody @Validated AdAddDto dto) {
        Ad ad = adService.getOne(new QueryWrapper<Ad>()
                .eq(Ad.COL_COMPANY_NAME, dto.getCompanyName()).last("limit 1"));
        Assert.isNull(ad, "已有该公司的名称，请勿重复添加");
        Ad insertEntity = BeanUtils.copyProperties(dto, Ad.class);
        adService.save(insertEntity);
        return ResultUtils.success(insertEntity.getId());
    }
    /**
     * 修改广告厂家
     *
     * @param dto dto
     * @return 是否修改成功
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> update(@RequestBody @Validated AdUpdateDto dto) {
        Ad ad = adService.getById(dto.getId());
        Assert.isTrue(ad != null, "该id广告厂家不存在");
        Ad updateEntity = BeanUtils.copyProperties(dto, Ad.class);
        boolean update = adService.updateById(updateEntity);
        return  ResultUtils.success(update);
    }
    /**
     * 删除合作厂家
     *
     * @param id 广告厂家id
     * @return 是否修改成功
     */
    @PostMapping("/delete/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> delete(@PathVariable("id") Long id) {
        Ad ad = adService.getById(id);
        Assert.isTrue(ad != null, "该id广告厂家不存在");
        boolean delete = adService.removeById(id);
        return ResultUtils.success(delete);
    }
    /**
     * 分页查询广告厂家
     *
     * @param current     当前页
     * @param page        页大小
     * @param companyName 支持模糊搜索公司姓名（可为空）
     * @return 合作厂家ListVo
     */
    @PostMapping("/page/{current}/{page}")
    public BaseResponse<List<Ad>> page(@PathVariable("current") Long current,
                                       @PathVariable("page") Long page,
                                       String companyName) {
        QueryWrapper<Ad> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(companyName)) {
            wrapper.like(Ad.COL_COMPANY_NAME, companyName);
        }
        Page<Ad> pageRes = adService.page(new Page<>(current, page), wrapper);
        return ResultUtils.success(pageRes.getRecords(), pageRes.getTotal());
    }
    /**
     * 批量删除
     *
     * @param ids str id
     */
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> delete(@RequestParam("ids") String ids) {
        String[] arrays = ids.split(",");
        if (arrays.length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ids 非法");
        }
        Arrays.stream(arrays).forEach(r -> adService.removeById(Long.parseLong(r)));
        return ResultUtils.success(true);
    }
}

    
    