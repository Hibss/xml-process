package com.syz.xml.process.utils;

import com.alibaba.fastjson.JSON;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;
import org.springframework.beans.SimpleTypeConverter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDOM2BeanUtil {
    private static String CLASS_TAG = "class";
    private static Class beanClass;
    private static String className = "";

    private static Map<Field, Object> infoMap;

    private static SimpleTypeConverter typeConverter;

    public static void main(String[] args) {
//        JDOM2BeanUtil.read("E:\\xmlTest\\City1596695276384.xml");

        List parse = JDOM2BeanUtil.parse("E:\\xmlTest\\City1596695696619.xml");
        System.out.println(JSON.toJSON(parse));
//        ;City1596695696619.xml
    }

    private static List parse(String file) {
        try {
            List res = new ArrayList();
            typeConverter = new SimpleTypeConverter();
            typeConverter.useConfigValueEditors();
            // 创建一个sax解析器
            SAXBuilder builder = new SAXBuilder();
            // 根据xml结构转换成一个Document对象
            Document doc = builder.build(new File(file));
            /* 打印xml信息 */
            parse(doc.getContent(), res);
            return res;
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void processInfoMap(Field[] fields) {
        infoMap = new HashMap<>();
        String fieldName;
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            fieldName = FieldUtil.getName(field);
            if (fieldName != null) {
                infoMap.put(field, "");
            }
        }
    }

    private static Class getClass(String fullName) throws ClassNotFoundException {
        return Class.forName(fullName);
    }

    private static void parse(List<Content> contentList, List res) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (Content temp : contentList) {
            if (temp instanceof Element) { //获取的内容是元素
                Element elt = (Element) temp;
                //暂无attribute设置
//                List<Attribute> attrs = elt.getAttributes();
                if (CLASS_TAG.equals(elt.getName())) {
                    Text text = (Text) elt.getContent().get(0);
                    beanClass = getClass(text.getText());
                    className = beanClass.getSimpleName();
                    processInfoMap(beanClass.getDeclaredFields());
                } else if (className.equals(elt.getName())) {
                    processClassInfo(elt.getContent());
                    Object o = beanClass.newInstance();
                    for (Map.Entry<Field, Object> entry : infoMap.entrySet()) {
                        entry.getKey().setAccessible(true);
                        entry.getKey().set(o, entry.getValue());
                    }
                    res.add(o);
                }else{
                    parse(elt.getContent(),res);
                }
            }
        }
    }

    private static void processClassInfo(List<Content> contentList) {
        Element element;
        String value;
        for (Content content : contentList) {
            if(content instanceof Element){

                element = (Element) content;
                value = ((Text) element.getContent().get(0)).getText();
                for (Map.Entry<Field, Object> entry : infoMap.entrySet()) {
                    if (entry.getKey().getName().equals(element.getName())) {
                        entry.setValue(typeConverter.convertIfNecessary(value, entry.getKey().getType()));
                    }
                }
            }
        }
    }

    public static void read(String file) {
        try {
            // 创建一个sax解析器
            SAXBuilder builder = new SAXBuilder();
            // 根据xml结构转换成一个Document对象
            Document doc = builder.build(new File(file));
            // 打印xml信息
            read(doc.getContent());
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void read(List<Content> list) {
        for (Content temp : list) {
            if (temp instanceof Comment) { //获取的内容是注释
                Comment com = (Comment) temp;
                System.out.println("<--" + com.getText() + "-->");
            } else if (temp instanceof Element) { //获取的内容是元素
                Element elt = (Element) temp;
                List<Attribute> attrs = elt.getAttributes();
                System.out.print("<" + elt.getName() + "");
                for (Attribute t : attrs) {
                    System.out.print(" " + t.getName() + " : " + t.getValue() + "");
                }
                System.out.print(">");
                read(elt.getContent());
                System.out.print("</" + elt.getName() + ">");
            } else if (temp instanceof ProcessingInstruction) { // 获取的内容是处理指令
                ProcessingInstruction pi = (ProcessingInstruction) temp;
                System.out.println("<?" + pi.getTarget() + "" + pi.getData() + "?>");
            } else if (temp instanceof EntityRef) {
                EntityRef ref = (EntityRef) temp;
                System.out.println("<--" + ref.getName() + "-->");
            } else if (temp instanceof Text) { //获取的内容是文本
                Text text = (Text) temp;
                if (!text.getText().trim().equals("")) {
                    System.out.print(text.getText());
                } else {
                    System.out.println();
                }
            } else if (temp instanceof CDATA) { // 获取的内容是CDATA
                CDATA cdata = (CDATA) temp;
                System.out.println("<![CDATA[" + cdata.getText() + "]]>");
            } else if (temp instanceof DocType) {
                DocType docType = (DocType) temp;
                System.out.println("<--" + docType.getCType() + "-->");
            }
        }

    }
}
