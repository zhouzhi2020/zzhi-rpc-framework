package com.zzhi.service;

import com.zzhi.entity.Brand;
import com.zzhi.entity.User;

/**
 * 品牌服务
 *
 * @author zzhi
 * @date 2022/08/02
 */
public interface BrandService {
    /**
     * 通过id获取品牌
     *
     * @param id id
     * @return {@link Brand}
     */
    public Brand getBrandById(Integer id);
}
