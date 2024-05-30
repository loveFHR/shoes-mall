package com.ewan.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ewan.annotation.AuthCheck;
import com.ewan.common.BaseResponse;
import com.ewan.common.ErrorCode;
import com.ewan.common.ResultUtils;
import com.ewan.constant.UserConstant;
import com.ewan.exception.BusinessException;
import com.ewan.model.dto.cooperation.CooperationAddDto;
import com.ewan.model.dto.cooperation.CooperationUpdateDto;
import com.ewan.model.entity.Cooperation;
import com.ewan.service.CooperationService;
import com.ewan.utils.BeanUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;
/**
 * 合作厂家
 *
 * @author cx
 * @date 2024/4/28
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/cooperation")
public class CooperationController {
    @Resource
    private CooperationService cooperationService;
    /**
     * 添加合作厂家
     *
     * @param dto dto
     * @return 表主键id
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> add(@RequestBody @Validated CooperationAddDto dto) {
        Cooperation cooperation = cooperationService.getOne(new QueryWrapper<Cooperation>()
                .eq(Cooperation.COL_COMPANY_NAME, dto.getCompanyName()).last("limit 1"));
        Assert.isNull(cooperation, "已有该公司的名称，请勿重复添加");
        Cooperation insertEntity = BeanUtils.copyProperties(dto, Cooperation.class);
        cooperationService.save(insertEntity);
        return ResultUtils.success(insertEntity.getId());
    }
    /**
     * 修改合作厂家
     *
     * @param dto dto
     * @return 是否修改成功
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> update(@RequestBody @Validated CooperationUpdateDto dto) {
        Cooperation cooperation = cooperationService.getById(dto.getId());
        Assert.isTrue(cooperation != null, "该id合作厂家不存在");
        Cooperation updateEntity = BeanUtils.copyProperties(dto, Cooperation.class);
        boolean update = cooperationService.updateById(updateEntity);
        return ResultUtils.success(update);
    }
    /**
     * 删除合作厂家
     *
     * @param id 合作厂家id
     * @return 是否修改成功
     */
    @PostMapping("/delete/{id}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> delete(@PathVariable("id") Long id) {
        Cooperation cooperation = cooperationService.getById(id);
        Assert.isTrue(cooperation != null, "该id合作厂家不存在");
        boolean delete = cooperationService.removeById(id);
        return ResultUtils.success(delete);
    }
    /**
     * 分页查询合作厂家
     *
     * @param current     当前页
     * @param page        页大小
     * @param companyName 支持模糊搜索公司姓名（可为空）
     * @return 合作厂家ListVo
     */
    @PostMapping("/page/{current}/{page}")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<Cooperation>> page(@PathVariable("current") Long current,
                                                @PathVariable("page") Long page,
                                                String companyName) {
        QueryWrapper<Cooperation> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(companyName)) {
            wrapper.like(Cooperation.COL_COMPANY_NAME, companyName);
        }
        Page<Cooperation> pageRes = cooperationService.page(new Page<>(current, page), wrapper);
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
        Arrays.stream(arrays).forEach(r -> cooperationService.removeById(Long.parseLong(r)));
        return ResultUtils.success(true);
    }
}