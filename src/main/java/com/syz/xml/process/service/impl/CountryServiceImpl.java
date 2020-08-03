package com.syz.xml.process.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syz.xml.process.entity.Country;
import com.syz.xml.process.mapper.CountryMapper;
import com.syz.xml.process.service.CountryService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author steven.sheng
 * @since 2020-08-03
 */
@Service
public class CountryServiceImpl extends ServiceImpl<CountryMapper, Country> implements CountryService {

    @Override
    public void createXml() {

    }
}
