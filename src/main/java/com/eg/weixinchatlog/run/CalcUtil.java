package com.eg.weixinchatlog.run;

import com.eg.weixinchatlog.util.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @time 2020-02-15 16:09
 */
public class CalcUtil {
    public static List<String> getUinList() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Document document = null;
        try {
            File app_brand_global_sp = new File(Constants.DATA_PATH + "/shared_prefs/app_brand_global_sp.xml");
            document = builder.parse(app_brand_global_sp);
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element root = document.get();
        root.ele
        NodeList childNodes1 = root.getChildNodes();
        System.out.println(childNodes1.getLength());
        for (int i = 0; i < childNodes1.getLength(); i++) {
            System.out.println(childNodes1.item(i).getTextContent());

        }
//        Node set = root.getElementsByTagName("set").item(0);
//        NodeList childNodes = set.getChildNodes();
//        for (int i = 0; i < childNodes.getLength(); i++) {
//            System.out.println(childNodes.item(i).getNodeName());
//        }
//        List<String> uinList = new ArrayList<>();
//        for (int i = 0; i < childNodes.getLength(); i++) {
//            String uin = childNodes.item(i).getTextContent();
//            uinList.add(uin);
//        }
//        return uinList;
        return null;
    }

    public static String getUinHash() {
        return null;
    }

    public static void main(String[] args) {
        List<String> uinList = getUinList();
//        for (String uin : uinList) {
//            System.out.print(uin);
//        }
    }
}
