package com.ewan.model.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * @author Ewan
 * @date 2024/5/25
 */
@Data
public class RefundDto {
    @NotNull(message = "退款订单不能为空")
    private List<String> orderNoList;
}

    
    