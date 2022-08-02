package com.zzhi.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 品牌
 *
 * @author zzhi
 * @date 2022/08/01
 */
@Data
@NoArgsConstructor
public class Brand implements Serializable {
    /**
     * id
     */
    private Integer id;
    /**
     * 品牌名称
     */
    private String brandName;
    /**
     * 公司名称
     */
    private String companyName;
    /**
     * 描述
     */
    private String description;
}
