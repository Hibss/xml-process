package com.syz.xml.process.entity;

import lombok.Data;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@XmlRootElement(name="countryPage")
@XmlAccessorType(value = XmlAccessType.FIELD)
@Data
public class CountryPage implements Serializable{

    private int count;
    @XmlAttribute
    private int page;
    @XmlAttribute
    private int pageSize;
    @XmlElement(name = "country")
    private Set<Country> rows;

    public void process() throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(CountryPage.class,Country.class);    // 获取上下文对象
        Marshaller marshaller = context.createMarshaller(); // 根据上下文获取marshaller对象

        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");  // 设置编码字符集
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 格式化XML输出，有分行和缩进

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        marshaller.marshal(this, baos);
        String xmlObj = new String(baos.toByteArray());         // 生成XML字符串
        System.out.println(xmlObj);
    }

    public static void main(String[] args) throws JAXBException {
        getSimpleDepartment().process();

    }

    public static CountryPage getSimpleDepartment() {
        CountryPage page = new CountryPage();
        List<Country> countryList = new ArrayList<>();

        Country country = new Country();
        country.setCode("123");
        country.setCode2("122");
        country.setName("aaa");
        countryList.add(country);

        country = new Country();
        country.setCode("123111");
        country.setCode2("1221111");
        country.setName("aaaa11111");
        countryList.add(country);

        page.setPage(1);
        page.setPageSize(10);
        page.setCount(countryList.size());
        page.setRows(new HashSet<>(countryList));


        return page;
    }
}
