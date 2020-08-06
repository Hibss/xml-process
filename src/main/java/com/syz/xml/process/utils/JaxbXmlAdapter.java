package com.syz.xml.process.utils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.*;
import java.util.Map.Entry;
public class JaxbXmlAdapter extends
        XmlAdapter<Object, List<Map<String, String>>> {

    /**
     * 把 JAVA转化成ELEMENT对象
     */
    @Override
    public Object marshal(List<Map<String, String>> rows) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();
        Element rootElement = document.createElement("root");// 这里名字随便,以外面为准,JAXB会自动替换名字的
        document.appendChild(rootElement);
        Entry<String, String> entry;
        Iterator<Entry<String, String>> iterator;
        for (Map<String, String> row : rows) {
            Element rowEle = document.createElement("row");
            iterator = row.entrySet().iterator();
            while (iterator.hasNext()) {
                entry = iterator.next();
                Element ele = document.createElement(entry.getKey());
                ele.setTextContent(entry.getValue());
                rowEle.appendChild(ele);
            }
            rootElement.appendChild(rowEle);
        }
        return rootElement;
    }

    /**
     * 把XML ELEMENT转化成JAVA对象
     */
    @Override
    public List<Map<String, String>> unmarshal(Object rowsElement)
            throws Exception {

        if (rowsElement == null) {
            return null;
        }

        Element rowsEle = (Element) rowsElement;

        NodeList rowNodes = rowsEle.getChildNodes();
        int rowCount = (rowNodes == null ? 0 : rowNodes.getLength());
        if (rowCount == 0) {
            return null;
        }
        List<Map<String, String>> result = new ArrayList<Map<String, String>>(
                rowCount);

        for (int i = 0; i < rowCount; i++) {
            Node rowNode = rowNodes.item(i);
            if (!"row".equals(rowNode.getNodeName())) {
                System.out.println("发现非法节点:" + rowNode.getNodeName()
                        + "忽略.");
                continue;
            }
            NodeList idNameNodes = rowNode.getChildNodes();
            int nodeSize = (idNameNodes == null ? 0 : idNameNodes
                    .getLength());
            if (nodeSize == 0) {
                continue;
            }
            Map<String, String> row = new HashMap<String, String>();
            String id = null;
            String name = null;
            for (int j = 0; j < nodeSize; j++) {
                Node node = idNameNodes.item(j);
                String nodeName = node.getNodeName();
                String nodeValue = node.getTextContent();
                if ("id".equals(nodeName)) {
                    id = nodeValue;
                } else if ("name".equals(nodeName)) {
                    name = nodeValue;
                }
                if (id != null && name != null) {
                    break;
                }
            }

            if (id != null) {
                row.put(id, name);
            } else {
                System.out.println("未在row节点里发现id节点,忽略.");
            }
            result.add(row);
        }

        return result;
    }

}
