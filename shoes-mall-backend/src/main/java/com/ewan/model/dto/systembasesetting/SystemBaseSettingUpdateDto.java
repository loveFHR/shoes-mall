package com.ewan.model.dto.systembasesetting;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Ewan
 * @date 2024/5/22
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SystemBaseSettingUpdateDto {
    /**
     *  id
     */
    @NotNull(message = "id不能为空")
    private Long id;

    /**
     *  类型 1-鞋码 2-颜色 3-品牌 4-鞋类型
     */
    @NotNull(message = "类型不能为空")
    private Integer type;

    /**
     * 标签名/类型名
     */
    @Size(max = 8, message = "标签名/类型名最大长度为 8")
    @NotBlank(message = "标签名/类型名不能为空")
    private String label;
}

    
    