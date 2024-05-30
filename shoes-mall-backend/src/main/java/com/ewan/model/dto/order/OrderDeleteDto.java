package com.ewan.model.dto.order;

import lombok.Data;

import java.util.List;

/**
 * @author Ewan
 * @date 2024/5/25
 */
@Data
public class OrderDeleteDto {
    private List<Long> orderIdList;
}

    
    