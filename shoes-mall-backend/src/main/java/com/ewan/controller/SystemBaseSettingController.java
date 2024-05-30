package com.ewan.controller;


import com.ewan.annotation.AuthCheck;
import com.ewan.common.BaseResponse;
import com.ewan.common.ErrorCode;
import com.ewan.common.ResultUtils;
import com.ewan.constant.UserConstant;
import com.ewan.exception.BusinessException;
import com.ewan.model.dto.systembasesetting.QuerySystemBaseSettingVo;
import com.ewan.model.dto.systembasesetting.SystemBaseSettingDTO;
import com.ewan.model.dto.systembasesetting.SystemBaseSettingUpdateDto;
import com.ewan.model.entity.SystemBaseSetting;
import com.ewan.service.SystemBaseSettingService;
import com.ewan.service.UserService;
import com.ewan.utils.BeanUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 系统基础设置
 *
 * @author Ewan
 * @date 2024/3/9
 */
@Validated
@RestController
@RequestMapping("systemBaseSetting")
public class SystemBaseSettingController {

    @Resource
    private SystemBaseSettingService systemBaseSettingService;

    @Resource
    private UserService userService;


    /**
     * 添加
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> add(@RequestBody @Validated SystemBaseSettingDTO dto, HttpServletRequest request) {
        Integer type = dto.getType();
        String label = dto.getLabel();

        //判断同一个类型中是否有重名的
        SystemBaseSetting typeAndName = systemBaseSettingService.lambdaQuery()
                .eq(SystemBaseSetting::getType, type)
                .eq(SystemBaseSetting::getLabel, label).last("limit 1").one();
        Assert.isNull(typeAndName, "该标签已存在");

        return ResultUtils.success(systemBaseSettingService.add(dto, userService.getLoginUserId(request)));
    }


    /**
     * 批量删除
     *
     * @param ids id字符串
     */
    @DeleteMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> delete(@RequestParam("ids") String ids) {
        String[] arrays = ids.split(",");
        if (arrays.length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ids 非法");
        }
        List<SystemBaseSetting> list = new ArrayList<>();
        Arrays.stream(arrays).forEach(r -> {
            Long id = Long.parseLong(r);
            SystemBaseSetting systemBaseSetting = systemBaseSettingService.getById(id);
            Assert.notNull(systemBaseSetting, "需删除的设置不存在");
            list.add(systemBaseSetting);
        });
        systemBaseSettingService.delete(list);
        return ResultUtils.success(true);
    }


    /**
     * 修改
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> update(@RequestBody SystemBaseSettingUpdateDto dto) {
        SystemBaseSetting systemBaseSetting = systemBaseSettingService.getById(dto.getId());
        Assert.notNull(systemBaseSetting, "需修改的设置不存在");
        SystemBaseSetting update = BeanUtils.copyProperties(dto, SystemBaseSetting.class);
        boolean res = systemBaseSettingService.updateById(update);
        return ResultUtils.success(res);
    }


    /**
     * 查询
     *
     * @param type    1-鞋码 2-颜色 3-品牌 4-鞋类型
     * @param current 当前页数
     * @param page    页大小
     */
    @GetMapping("/{type}")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<List<QuerySystemBaseSettingVo>> queryPage(@PathVariable("type") Long type,
                                                                  @RequestParam("current") Long current,
                                                                  @RequestParam("page") Long page,
                                                                  String label) {
        return systemBaseSettingService.queryPage(type, current, page, label);
    }


    /**
     * 禁用启用
     *
     * @param ids str id
     */
    @PostMapping("/ban")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> ban(@RequestParam("ids") String ids) {
        String[] arrays = ids.split(",");
        if (arrays.length == 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "ids 非法");
        }
        List<SystemBaseSetting> updates = new ArrayList<>();
        Arrays.stream(arrays).forEach(r -> {
            Long id = Long.parseLong(r);
            SystemBaseSetting systemBaseSetting = systemBaseSettingService.getById(id);
            Assert.notNull(systemBaseSetting, "需启用禁用的设置不存在");
            systemBaseSetting.setBan(systemBaseSetting.getBan() == 0 ? 1 : 0);
            updates.add(systemBaseSetting);
        });
        boolean res = systemBaseSettingService.updateBatchById(updates);
        return ResultUtils.success(res);
    }
}

    
    