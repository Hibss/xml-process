package com.syz.xml.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syz.xml.process.entity.City;
import com.syz.xml.process.entity.Country;
import com.syz.xml.process.mapper.CityMapper;
import com.syz.xml.process.service.CityService;
import com.syz.xml.process.utils.SAX2XmlUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author steven.sheng
 * @since 2020-08-03
 */
@Service
@Slf4j
@Transactional
public class CityServiceImpl extends ServiceImpl<CityMapper, City> implements CityService {

    @Autowired
    private CityMapper cityMapper;

    @Value("${file.path}")
    private String filePath;

    @Override
    public void createXml() throws Exception {
        String path = filePath + File.separator + City.class.getSimpleName()+System.currentTimeMillis() + ".xml";
        Integer pageIndex = 1 , pageSize =10;
        QueryWrapper<City> queryWrapper = new QueryWrapper<>();
        IPage page = new Page<>(pageIndex, pageSize);
        queryWrapper.eq("countryCode","CHN");
        IPage selectPage = cityMapper.selectPage(page,queryWrapper);
        List<City> cityList = selectPage.getRecords();
        if(CollectionUtils.isEmpty(cityList)){
            log.info("无城市列表");
            return;
        }
        SAX2XmlUtil.createSAX(path,cityList);
    }
}
