package com.ewan.model.dto.ad;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.ewan.model.entity.Ad;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author Ewan
 * @date 2024/5/14
 */

@Data
public class AdUpdateDto {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    @NotNull(message = "id不能为null")
    private Long id;

    /**
     * 公司名
     */
    @TableField(value = "company_name")
    @Size(max = 16,message = "公司名最大长度要小于 16")
    @NotBlank(message = "公司名不能为空")
    private String companyName;

    /**
     * *100价格
     */
    @TableField(value = "price")
    @NotNull(message = "*100价格不能为null")
    private Integer price;

    /**
     * 图片
     */
    @TableField(value = "cover")
    @Size(max = 128, message = "图片最大长度要小于 128")
    @NotBlank(message = "图片不能为空")
    private String cover;

    /**
     * url
     */
    @TableField(value = "url")
    @Size(max = 32,message = "url最大长度要小于 32")
    @NotBlank(message = "url不能为空")
    private String url;
    /**
     * 广告有效期
     */
    @TableField(value = "expire_time")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @NotNull(message = "广告有效期不能为null")
    private Date expireTime;
}

    
    