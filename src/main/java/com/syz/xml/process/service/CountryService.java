package com.syz.xml.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syz.xml.process.entity.Country;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author steven.sheng
 * @since 2020-08-03
 */
public interface CountryService extends IService<Country> {

    void createXml();
}
