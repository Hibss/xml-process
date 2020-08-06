package com.syz.xml.process.utils;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.Format.TextMode;
import org.jdom2.output.XMLOutputter;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;


public class JDOM2XmlUtil {

    public static void list2Xml(List list,String file){
        try {
            if(CollectionUtils.isEmpty(list)){
                return;
            }
            Object object = list.get(0);
            String name = object.getClass().getSimpleName();
            String fullName = object.getClass().getName();
            // 创建一个根节点
            Element rootElement = new Element(name +"List");
            Document doc = new Document(rootElement);
            Element classElement = new Element("class");
            classElement.setText(fullName);
            doc.getRootElement().addContent(classElement);
            Field[] declaredFields = object.getClass().getDeclaredFields();
            Element element,child;
            for (Object o : list) {
                element = new Element(name);
                for (Field field : declaredFields) {
                    try {
                        String value = FieldUtil.getObjectValue(o, field);
                        if(StringUtils.isEmpty(value)){
                            continue;
                        }
                        child = new Element(field.getName());
                        child.setText(value);
                        element.addContent(child);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                doc.getRootElement().addContent(element);
            }

            // 创建xml输出流操作类
            XMLOutputter xmlOutput = new XMLOutputter();

            // 设置xml格式化的属性
            Format f = Format.getRawFormat();
            f.setIndent("  "); // 文本缩进
            f.setTextMode(TextMode.TRIM_FULL_WHITE);
            xmlOutput.setFormat(f);

            // 把xml文件输出到指定的位置
            xmlOutput.output(doc, new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
