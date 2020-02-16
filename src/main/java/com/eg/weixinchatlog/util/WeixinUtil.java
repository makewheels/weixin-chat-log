package com.eg.weixinchatlog.util;

import org.apache.commons.codec.digest.DigestUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @time 2020-02-15 16:09
 */
public class WeixinUtil {
    /**
     * uin列表
     *
     * @return
     */
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

    /**
     * 用户文件夹名
     *
     * @param uin
     * @return
     */
    public static String getMmUinMd5(String uin) {
        return DigestUtils.md5Hex("mm" + uin);
    }

    /**
     * 数据库密码
     *
     * @param uin
     * @return
     */
    public static String getSqlitePassword(String uin) {
        return DigestUtils.md5Hex(Constants.IMEI + uin).substring(0, 7);
    }

    /**
     * 获取imei
     */
    public static String getImei() {
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(
                    new FileInputStream(Constants.DATA_PATH + "/MicroMsg/CompatibleInfo.cfg"));
            Map<Integer, Object> map = (Map<Integer, Object>) objectInputStream.readObject();
            return map.get(258).toString();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        Constants.IMEI = getImei();
        List<String> uinList = getUinList();
        for (String uin : uinList) {
            System.out.println(uin + " " + getMmUinMd5(uin) + " " + getSqlitePassword(uin));
        }
    }
}
