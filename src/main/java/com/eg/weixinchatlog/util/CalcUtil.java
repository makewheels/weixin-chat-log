package com.eg.weixinchatlog.util;

import com.eg.weixinchatlog.util.Constants;
import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @time 2020-02-15 16:09
 */
public class CalcUtil {
    public static List<String> getUinList() {
        Document document = null;
        try {
            document = (Document) new SAXReader().read(new File(
                    Constants.DATA_PATH + "/shared_prefs/app_brand_global_sp.xml"));
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Element root = document.getRootElement();
        List<Element> stringList = root.element("set").elements();
        List<String> uinList = new ArrayList<>();
        for (Element uinElement : stringList) {
            String uin = uinElement.getText();
            uinList.add(uin);
        }
        return uinList;
    }

    public static String getMmUinMd5(String uin) {
        return DigestUtils.md5Hex("mm" + uin);
    }

    public static String getSqlitePassword(String uin) {
        return DigestUtils.md5Hex(Constants.IMEI + uin).substring(0, 7);
    }

    public static void main(String[] args) {
        List<String> uinList = getUinList();
        for (String uin : uinList) {
            System.out.println(uin + " " + getMmUinMd5(uin) + " " + getSqlitePassword(uin));
        }
    }
}
