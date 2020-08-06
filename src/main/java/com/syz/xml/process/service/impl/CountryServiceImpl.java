package com.syz.xml.process.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.syz.xml.process.entity.Country;
import com.syz.xml.process.entity.CountryMap;
import com.syz.xml.process.mapper.CountryMapper;
import com.syz.xml.process.service.CountryService;
import com.syz.xml.process.entity.CountryPage;
import com.syz.xml.process.utils.JaxbUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.HashSet;
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
public class CountryServiceImpl extends ServiceImpl<CountryMapper, Country> implements CountryService {

    @Autowired
    private CountryMapper countryMapper;

    @Value("${file.path}")
    private String filePath;

    @Override
    public void createXml() {
        Integer pageIndex = 1 , pageSize =10;
        QueryWrapper<Country> queryWrapper = new QueryWrapper<>();
        IPage page = new Page<>(pageIndex, pageSize);
        IPage countryPage = countryMapper.selectPage(page,queryWrapper);
        List<Country> countryList = countryPage.getRecords();
        if(countryList.isEmpty()){
            return ;
        }else{
//            String path = filePath + File.separator + Country.class.getSimpleName()+System.currentTimeMillis() + ".xml";
//            //1.循环处理
//            StringBuffer sb = new StringBuffer();
//            countryList.forEach(country -> sb.append(JaxbUtil.convertToXml(country)));
//            log.info("xml:{}",sb);

            //2.封装处理
//           CountryPage obj = new CountryPage();
//            obj.setRows(new HashSet<>(countryList));
//            obj.setPage(1);
//            obj.setPageSize(10);
//            obj.setCount(countryList.size());
//            try {
//                obj.process();
//            } catch (JAXBException e) {
//                e.printStackTrace();
//            }
            //3.转map处理
            CountryMap map = new CountryMap(countryList,pageIndex,pageSize);
            String convertToXml = map.process();
            System.out.println(convertToXml);

        }
    }
}
