package com.ewan.model.dto.cooperation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author Ewan
 * @date 2024/5/14
 */

@Data
public class CooperationUpdateDto {

    /**
     * id
     */
    @NotNull(message = "id不能为null")
    private Long id;
    /**
     * 公司名
     */
    @Size(max = 16, message = "公司名最大长度要小于 16")
    @NotBlank(message = "公司名不能为空")
    private String companyName;

    /**
     * url
     */
    @Size(max = 32, message = "url最大长度要小于 32")
    @NotBlank(message = "url不能为空")
    private String url;

    /**
     * 图片
     */
    @Size(max = 128, message = "图片最大长度要小于 128")
    @NotBlank(message = "图片不能为空")
    private String cover;
}

    
    