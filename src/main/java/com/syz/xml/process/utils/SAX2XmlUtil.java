package com.syz.xml.process.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

@Slf4j
public class SAX2XmlUtil extends DefaultHandler {
    
    private final static String ROOT = "root";

    public static void createSAX(String filePath,List<?> list) throws Exception {
        try {
            if(CollectionUtils.isEmpty(list)){
                log.info("createSAX exception,no input data");
                return;
            }
            SAXTransformerFactory factory = (SAXTransformerFactory) SAXTransformerFactory.newInstance();
            TransformerHandler handler = factory.newTransformerHandler();
            Transformer transformer = handler.getTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes"); // 换行
            transformer.setOutputProperty(OutputKeys.ENCODING, "utf-8"); // 字符集
            Result result = new StreamResult(new FileOutputStream(filePath));
            handler.setResult(result);
            AttributesImpl attr = new AttributesImpl();
            // 打开doc对象
            handler.startDocument();
            // 创建元素: handler.startElement(uri, 命名空间, 元素名, 属性列表);
            String beanName = list.get(0).getClass().getSimpleName();
            //根节点，添加换行，强行换行
            String returnS = "\n";
            handler.characters(returnS.toCharArray(),0,returnS.length());

            //开始转xml
            handler.startElement("", "", ROOT, attr);

            //设置类名
            String fullName = list.get(0).getClass().getName();
            handler.startElement("", "", "class", attr);
            handler.characters(fullName.toCharArray(),0,fullName.length());
            handler.endElement("", "", "class");

            Field[] fields = list.get(0).getClass().getDeclaredFields();
            Object o;
            String value ;
            String four = "\n    ";
            String eight = "\n        ";
            for (int i = 0; i < list.size(); i++) {
                o = list.get(i);
                attr.clear();
                attr.addAttribute("", "",  "index", "",i+"");
                handler.characters(four.toCharArray(),0,four.length());
                handler.startElement("", "", beanName , attr);
                for (Field field : fields) {
                    attr.clear();
                    value = FieldUtil.getObjectValue(o,field);
                    if(StringUtils.isEmpty(value)){
                        continue;
                    }
                    handler.characters(eight.toCharArray(),0,eight.length());
                    handler.startElement("", "", field.getName(), attr);
                    handler.characters(value.toCharArray(), 0, value.length());
                    handler.endElement("", "", field.getName());
                }
                handler.characters(four.toCharArray(),0,four.length());
                handler.endElement("", "", beanName );
            }
            handler.endElement("", "", ROOT);
            // 关闭doc对象
            handler.endDocument();
            System.out.println("SAX CreateSAX success!");
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e)

        {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Receive notification of the start of an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the start of
     * each element (such as allocating a new tree node or writing
     * output to a file).</p>
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @param attributes The attributes attached to the element.  If
     *        there are no attributes, it shall be an empty
     *        Attributes object.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#startElement
     */
    @Override
    public void startElement (String uri, String localName,
                              String qName, Attributes attributes)
            throws SAXException
    {
        System.out.println("< "+qName+" >");
    }


    /**
     * Receive notification of the end of an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the end of
     * each element (such as finalising a tree node or writing
     * output to a file).</p>
     *
     * @param uri The Namespace URI, or the empty string if the
     *        element has no Namespace URI or if Namespace
     *        processing is not being performed.
     * @param localName The local name (without prefix), or the
     *        empty string if Namespace processing is not being
     *        performed.
     * @param qName The qualified name (with prefix), or the
     *        empty string if qualified names are not available.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#endElement
     */
    @Override
    public void endElement (String uri, String localName, String qName)
            throws SAXException
    {
        System.out.println("</ "+qName+" >");
    }


    /**
     * Receive notification of character data inside an element.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method to take specific actions for each chunk of character data
     * (such as adding the data to a node or buffer, or printing it to
     * a file).</p>
     *
     * @param ch The characters.
     * @param start The start position in the character array.
     * @param length The number of characters to use from the
     *               character array.
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#characters
     */
    @Override
    public void characters (char ch[], int start, int length)
            throws SAXException {
        String str = new String(ch, start, length);
        if (!"\n".equals(str)) {
            System.out.print(str);
        }
    }



    /**
     * Receive notification of the beginning of the document.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the beginning
     * of a document (such as allocating the root node of a tree or
     * creating an output file).</p>
     *
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#startDocument
     */
    public void startDocument ()
            throws SAXException
    {
        System.out.println("开始xml解析");
    }


    /**
     * Receive notification of the end of the document.
     *
     * <p>By default, do nothing.  Application writers may override this
     * method in a subclass to take specific actions at the end
     * of a document (such as finalising a tree or closing an output
     * file).</p>
     *
     * @exception org.xml.sax.SAXException Any SAX exception, possibly
     *            wrapping another exception.
     * @see org.xml.sax.ContentHandler#endDocument
     */
    public void endDocument ()
            throws SAXException
    {
        System.out.println("完成xml解析");
    }

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        SAX2XmlUtil util = new SAX2XmlUtil();
        File inputFile = new File("E:\\xmlTest\\a.xml");

        // 创建一个SAX解析工厂
        SAXParserFactory factory = SAXParserFactory.newInstance();
        // 创建一个SAX转换工具
        SAXParser saxParser = factory.newSAXParser();
        // 解析XML
        saxParser.parse(inputFile, util);
    }

}
