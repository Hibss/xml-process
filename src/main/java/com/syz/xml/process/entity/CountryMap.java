package com.syz.xml.process.entity;

import com.syz.xml.process.utils.FieldUtil;
import com.syz.xml.process.utils.JaxbXmlAdapter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.*;

@XmlRootElement(name="CountryMap")
@XmlAccessorType(value = XmlAccessType.FIELD)
@Data
@NoArgsConstructor
public class CountryMap {

    private int count;
    @XmlAttribute
    private int page;
    @XmlAttribute
    private int pageSize;
    @XmlJavaTypeAdapter(JaxbXmlAdapter.class)
    private List<Map<String,String>> rows;

    public CountryMap(List<Country> countryList, Integer pageIndex, Integer pageSize){
        this.count = countryList.size();
        this.page = pageIndex;
        this.pageSize = pageSize;
        this.rows = getMap(countryList);
    }

    private List<Map<String, String>> getMap(List<Country> countryList) {
        Field[] fields = Country.class.getDeclaredFields();
        List<Map<String, String>> res = new ArrayList<>();
        Map<String, String> bean ;
        for (Country country : countryList) {
            bean = new HashMap<>();
            for (Field field : fields) {
                try {
                    bean.put(field.getName(),FieldUtil.getObjectValue(country,field));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            res.add(bean);
        }
        return res;
    }

    public String process() {
        JAXBContext context = null;    // 获取上下文对象
        try {
            context = JAXBContext.newInstance(CountryMap.class);
            Marshaller marshaller = context.createMarshaller(); // 根据上下文获取marshaller对象

            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");  // 设置编码字符集
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true); // 格式化XML输出，有分行和缩进

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(this, baos);
            String xmlObj = new String(baos.toByteArray());         // 生成XML字符串
            return xmlObj;
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) throws JAXBException {
        getData().process();

    }

    public static CountryMap getData() {
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

        CountryMap map = new CountryMap(countryList,1,10);


        return map;
    }
}
