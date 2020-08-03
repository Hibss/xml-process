package com.syz.xml.process.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.syz.xml.process.entity.City;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author steven.sheng
 * @since 2020-08-03
 */
public interface CityService extends IService<City> {

    void createXml() throws Exception;
}
