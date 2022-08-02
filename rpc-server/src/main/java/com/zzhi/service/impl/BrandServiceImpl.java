package com.zzhi.service.impl;

import com.zzhi.entity.Brand;
import com.zzhi.service.BrandService;
import lombok.extern.slf4j.Slf4j;

/**
 * 品牌服务实现类
 *
 * @author zzhi
 * @date 2022/08/02
 */
@Slf4j
public class BrandServiceImpl implements BrandService {
    @Override
    public Brand getBrandById(Integer id) {
        log.info("调用getBrandById");
        log.info("查询userId ===> {}", id);
        Brand brand = new Brand();
        brand.setId(id);
        brand.setBrandName("1zp-plus");
        brand.setCompanyName("1z公司");
        brand.setDescription("这是一个手摇磨豆机");

        log.info("查询的user ===> {}", brand);

        return brand;
    }
}
