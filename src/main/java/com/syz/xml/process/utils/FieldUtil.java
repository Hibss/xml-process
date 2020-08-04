package com.syz.xml.process.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class FieldUtil {

    public static String getObjectValue(Object object, Field field) throws Exception {
        //我们项目的所有实体类都继承BaseDomain （所有实体基类：该类只是串行化一下）
        //不需要的自己去掉即可
        if (object != null) {//if (object!=null )  ----begin
            // 如果类型是String
            String fieldType = field.getGenericType().toString();
            if (fieldType.equals("class java.lang.String") ||
                    fieldType.equals("class java.lang.Integer") ||
                    fieldType.equals("class java.lang.Double") ||
                    fieldType.equals("class java.lang.Boolean") ||
                    fieldType.equals("class java.lang.Date") ||
                    fieldType.equals("class java.lang.Short") ||
                    fieldType.equals("class java.lang.Long") ||
                    fieldType.equals("class java.lang.BigDecimal")
                    ) {
                // 如果type是类类型，则前面包含"class "，后面跟类名
                // 拿到该属性的gettet方法
                /**
                 * 这里需要说明一下：他是根据拼凑的字符来找你写的getter方法的
                 * 在Boolean值的时候是isXXX（默认使用ide生成getter的都是isXXX）
                 * 如果出现NoSuchMethod异常 就说明它找不到那个gettet方法 需要做个规范
                 */
                Method m = object.getClass().getMethod(
                        "get" + getMethodName(field.getName()));
                return String.valueOf(m.invoke(object));// 调用getter方法获取属性值
            }
        }
        return null;
    }

    public static String getName(Field field){
            // 如果类型是String
            String fieldType = field.getGenericType().toString();
            if (fieldType.equals("class java.lang.String") ||
                    fieldType.equals("class java.lang.Integer") ||
                    fieldType.equals("class java.lang.Double") ||
                    fieldType.equals("class java.lang.Boolean") ||
                    fieldType.equals("class java.lang.Date") ||
                    fieldType.equals("class java.lang.Short") ||
                    fieldType.equals("class java.lang.Long") ||
                    fieldType.equals("class java.lang.BigDecimal")
                    ) {
                return field.getName();
//                String fieldName = field.getName();
//                return fieldName.substring(0,1).toLowerCase().concat(fieldName.substring(1));
            }
        return null;
    }

    // 把一个字符串的第一个字母大写、效率是最高的、
    private static String getMethodName(String fieldName) throws Exception {
        return fieldName.substring(0,1).toUpperCase().concat(fieldName.substring(1));
    }
}
