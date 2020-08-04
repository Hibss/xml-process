package com.syz.xml.process.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.ibatis.annotations.Insert;
import org.springframework.beans.SimpleTypeConverter;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SAX2BeanUtil extends DefaultHandler {

    private final static String ROOT = "root";
    private final static  String returnS = "\n";

    private final static String CLASS_TAG = "class";

    private static String fileName;
    private static String className;
    private static String currentField;
    private static List data;
    private Class bean;
    private boolean classInit;
    private Field[] fields;

    private Map<String,Object> infoMap;
    private Map<String,Class> typeMap;
    private SimpleTypeConverter typeConverter;

    public SAX2BeanUtil(String fileName){
        this.fileName = fileName;
        data = new ArrayList();
        classInit = true;
        typeConverter = new SimpleTypeConverter();
        typeConverter.useConfigValueEditors();
    }

    public static List getList(String fileName) throws IOException, SAXException, ParserConfigurationException {
        //1.获取sax解析器的工厂对象
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //2) 从工厂对象获取SAX解析器
        SAXParser parser = factory.newSAXParser();
        //3) 使用sax解析器来解析xml文件
        File file = new File(fileName);
        SAX2BeanUtil dh = new SAX2BeanUtil(fileName);
        //第一个参数是文件
        //第二个参数：提供事件处理方法
        parser.parse(file, dh);
        return data;
    }

    private static Class getClass(String fullName) throws ClassNotFoundException {
        return Class.forName(fullName);
    }
    //开始解析
    public void startDocument ()
            throws SAXException
    {
        log.info("开始解析文件：{}",fileName);
    }


    //完成解析
    public void endDocument ()
            throws SAXException
    {
        log.info("完成解析文件：{}",fileName);
    }

    //开始标签
    @SneakyThrows
    public void startElement (String uri, String localName,
                              String qName, Attributes attributes)
            throws SAXException
    {
        if(qName.equals(ROOT) || qName.equals(className) || qName.equals(CLASS_TAG)){
            return;
        }
        if(infoMap.containsKey(qName)){
            currentField = qName;
        }
    }


    //完成标签
    @SneakyThrows
    public void endElement (String uri, String localName, String qName)
            throws SAXException
    {
        if(className.equals(qName)){
            Object o = bean.newInstance();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }

                field.setAccessible(true);
                field.set(o, infoMap.get(field.getName()));
            }
//            map2Bean(infoMap, o);
            data.add(o);
        }
    }


    //解析内容
    @SneakyThrows
    public void characters (char ch[], int start, int length)
            throws SAXException
    {
        String value = new String(ch,start,length);
        if(returnS.equals(value) || value.contains(returnS)){
            return;
        }
        if(classInit) {
            //System.out.println(attributes.getValue(0));
            bean = getClass(value);
            className = bean.getSimpleName();
            log.info("处理bean:{}",bean.getName());
            fields = bean.getDeclaredFields();
            processInfoMap(fields);
            classInit = false;
        }else{
            infoMap.put(currentField,typeConverter.convertIfNecessary(value, typeMap.get(currentField)));
        }
    }

    private void processInfoMap(Field[] fields) {
        infoMap = new HashMap<>();
        typeMap = new HashMap<>();
        String fieldName ;
        for (Field field : fields) {
            fieldName = FieldUtil.getName(field);
            if(fieldName != null){
                infoMap.put(fieldName,"");
                typeMap.put(fieldName,field.getType());
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(SAX2BeanUtil.getList("E:\\xmlTest\\City1596520231226.xml"));
//        Map<String,String> map = new HashMap<>();
//        map.put("countryCode","123");
//        Class bean = SAX2BeanUtil.getClass("com.syz.xml.process.entity.City");
//        Object o = bean.newInstance();
//        SAX2BeanUtil.map2Bean(map,o);
////        System.out.println(o);
//        SimpleTypeConverter typeConverter = new SimpleTypeConverter();
//        typeConverter.useConfigValueEditors();
//        Type type = Integer.TYPE;
//        System.out.println(typeConverter.convertIfNecessary("123", type.getClass()));
    }

    public Object bean2Map(Class bean){
        Map<String,Object> keyValues = new HashMap<>();
        Method[] methods=bean.getMethods();
        try {
            for(Method method: methods){

                String methodName=method.getName();
                //反射获取属性与属性值的方法很多，以下是其一；也可以直接获得属性，不过获取的时候需要用过设置属性私有可见
                if (methodName.contains("get")){
                    //invoke 执行get方法获取属性值
                    Object value=method.invoke(bean);
                    //根据setXXXX 通过以下算法取得属性名称
                    String key=methodName.substring(methodName.indexOf("get")+3);
                    Object temp=key.substring(0,1).toString().toLowerCase();
                    key=key.substring(1);
                    //最终得到属性名称
                    key=temp+key;
                    keyValues.put(key,value);
                }
            }
        }catch (Exception e){
            log.error("错误信息：",e);
        }
        log.info("auto bean covert to map:{}", JSONObject.toJSON(keyValues).toString());
        return keyValues;
    }

    public static void map2Bean(Map<String,Object> keyValues,Object bean) throws Exception {
        BeanUtils.populate(bean,keyValues);
        log.info("map covert to bean:{}", bean);
    }

}
