package com.eg.weixinchatlog.weixin;

import com.eg.weixinchatlog.util.Constants;
import com.eg.weixinchatlog.util.WeixinUtil;
import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.ImgFlag;
import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.Message;
import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.Rcontact;
import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.dao.EnMicroMsgDao;
import com.eg.weixinchatlog.weixin.sqlite.wxfileindex.WxFileIndex2;
import com.eg.weixinchatlog.weixin.sqlite.wxfileindex.dao.WxFileIndex2Dao;
import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @time 2020-02-15 18:21
 */
@Data
public class WeixinService {
    private EnMicroMsgDao enMicroMsgDao;
    private WxFileIndex2Dao wxFileIndex2Dao;

    /**
     * 初始化微信用户列表
     */
    public List<WeixinUser> initWeixinUserList() {
        List<WeixinUser> weixinUserList = new ArrayList<>();
        //拿到uin列表
        List<String> uinList = WeixinUtil.getUinList();
        //遍历每一个uin
        for (String uin : uinList) {
            WeixinUser weixinUser = new WeixinUser();
            weixinUser.setUin(uin);
            String mmUinMd5 = WeixinUtil.getMmUinMd5(uin);
            weixinUser.setMmUinMd5(mmUinMd5);
            weixinUser.setSqlitePassword(WeixinUtil.getSqlitePassword(uin));
            weixinUser.setMmFolder(new File(Constants.DATA_PATH + "/MicroMsg/" + mmUinMd5));
            //初始化service
            WeixinService weixinService = new WeixinService();
            weixinUser.setWeixinService(weixinService);
            //初始化db文件
            initEnMicroMsgDb(weixinUser);
            initWxFileIndexDb(weixinUser);
            weixinUserList.add(weixinUser);
        }
        return weixinUserList;
    }

    /**
     * 初始化EnMicroMsg.db文件
     *
     * @param weixinUser
     */
    public void initEnMicroMsgDb(WeixinUser weixinUser) {
        File enMicroMsg_decrypt_db_file = new File(Constants.DATA_PATH + "/MicroMsg/"
                + weixinUser.getMmUinMd5() + "/EnMicroMsg-decrypt.db");
        EnMicroMsgDao enMicroMsgDao = new EnMicroMsgDao();
        enMicroMsgDao.setDbFile(enMicroMsg_decrypt_db_file);
        enMicroMsgDao.initConnection();
        weixinUser.getWeixinService().setEnMicroMsgDao(enMicroMsgDao);
    }

    /**
     * 初始化WxFileIndex.db文件
     *
     * @param weixinUser
     */
    public void initWxFileIndexDb(WeixinUser weixinUser) {
        File wxFileIndex_decrypt_db_file = new File(Constants.DATA_PATH + "/MicroMsg/"
                + weixinUser.getMmUinMd5() + "/WxFileIndex-decrypt.db");
        WxFileIndex2Dao wxFileIndex2Dao = new WxFileIndex2Dao();
        wxFileIndex2Dao.setDbFile(wxFileIndex_decrypt_db_file);
        wxFileIndex2Dao.initConnection();
        weixinUser.getWeixinService().setWxFileIndex2Dao(wxFileIndex2Dao);
    }

    /**
     * 初始化各种联系人总方法
     *
     * @param weixinUser
     */
    public void initContacts(WeixinUser weixinUser) {
        //初始化所有联系人，包括公众号，小程序
        initRcontactList(weixinUser);
        //只有我添加的朋友
        initFriendList(weixinUser);
        //群
        initChatRoomList(weixinUser);
        //公众号
        initOfficalList(weixinUser);
        //小程序
        initAppList(weixinUser);
    }

    /**
     * 初始化所有联系人
     */
    public void initRcontactList(WeixinUser weixinUser) {
        //拿到所有联系人
        List<Rcontact> rcontactList = enMicroMsgDao.getRcontactList();
        //查询头像url
        for (Rcontact rcontact : rcontactList) {
            String username = rcontact.getUsername();
            //设置username md5
            rcontact.setUsernameMd5(DigestUtils.md5Hex(username));
            //设置头像
            ImgFlag imgFlag = enMicroMsgDao.getImgFlagByUsername(username);
            //这个头像是有可能查不到的，比如漂流瓶，系统联系人，已删除联系人，或者应该叫冻结的
            if (imgFlag != null) {
                String reserved1 = imgFlag.getReserved1();
                String reserved2 = imgFlag.getReserved2();
                //先看第一个，如果没有再用第二个。群没有第一个，只有第二个
                if (StringUtils.isNotEmpty(reserved1)) {
                    rcontact.setAvatarUrl(reserved1);
                } else if (StringUtils.isNotEmpty(reserved2)) {
                    rcontact.setAvatarUrl(reserved2);
                }
            }
        }
        //设置本地头像文件
        for (Rcontact rcontact : rcontactList) {
            String usernameMd5 = rcontact.getUsernameMd5();
            File avatarLocalFile = new File(weixinUser.getMmFolder() + "/avatar/"
                    + usernameMd5.substring(0, 2) + "/" + usernameMd5.substring(2, 4)
                    + "/user_" + usernameMd5 + ".png");
            System.out.println(avatarLocalFile);
            //判断本地文件是否存在
            if (avatarLocalFile.exists()) {
                rcontact.setAvatarLocalFile(avatarLocalFile);
            } else {
                rcontact.setAvatarLocalFile(null);
            }
        }
        weixinUser.setRcontactList(rcontactList);
    }


    /**
     * 初始化朋友联系人
     */
    public void initFriendList(WeixinUser weixinUser) {
        List<Rcontact> friendList = new ArrayList<>();
        for (Rcontact rcontact : weixinUser.getRcontactList()) {
            String username = rcontact.getUsername();
            if (rcontact.getVerifyFlag() != 0)
                continue;
            if (username.contains("@qqim"))
                continue;
            if (username.contains("@app"))
                continue;
            if (username.contains("@chatroom"))
                continue;
            if (rcontact.getType() == 33)
                continue;
            if (username.equals("filehelper"))
                continue;
            if (rcontact.getType() == 65536)
                continue;
            String alias = rcontact.getAlias();
            if (StringUtils.isEmpty(username) && StringUtils.isEmpty(alias))
                continue;
            friendList.add(rcontact);
        }
        weixinUser.setFriendList(friendList);
    }

    /**
     * 初始化联系人中的群
     */
    public void initChatRoomList(WeixinUser weixinUser) {
        List<Rcontact> chatRoomList = new ArrayList<>();
        for (Rcontact rcontact : weixinUser.getRcontactList()) {
            String username = rcontact.getUsername();
            if (username.endsWith("@chatroom")) {
                chatRoomList.add(rcontact);
            }
        }
        weixinUser.setChatRoomList(chatRoomList);
    }

    /**
     * 初始化联系人中的公众号
     */
    public void initOfficalList(WeixinUser weixinUser) {
        List<Rcontact> chatRoomList = new ArrayList<>();
        for (Rcontact rcontact : weixinUser.getRcontactList()) {
            String username = rcontact.getUsername();
            if (username.startsWith("gh_") && username.endsWith("@app") == false) {
                chatRoomList.add(rcontact);
            }
        }
        weixinUser.setOfficalList(chatRoomList);
    }

    /**
     * 初始化联系人中的小程序
     */
    public void initAppList(WeixinUser weixinUser) {
        List<Rcontact> chatRoomList = new ArrayList<>();
        for (Rcontact rcontact : weixinUser.getRcontactList()) {
            String username = rcontact.getUsername();
            if (username.startsWith("gh_") && username.endsWith("@app")) {
                chatRoomList.add(rcontact);
            }
        }
        weixinUser.setAppList(chatRoomList);
    }

    /**
     * 据talker获取，与改联系人的聊天记录总数
     *
     * @param username
     * @return
     */
    public long getMessageCountByTalker(String username) {
        return enMicroMsgDao.getMessageCountByTalker(username);
    }

    /**
     * 根据talker获取，与改联系人所有聊天记录
     *
     * @param username
     * @return
     */
    public List<Message> getAllMessageByTalker(String username) {
        return enMicroMsgDao.getAllMessageByTalker(username);
    }

    /**
     * 通过msgId找所有文件列表
     *
     * @param msgId
     * @return
     */
    private List<WxFileIndex2> getAllWxFileIndex2ByMsgId(long msgId) {
        return wxFileIndex2Dao.getAllWxFileIndex2ByMsgId(msgId);
    }

    /**
     * 通过msgId找，size最大文件
     *
     * @param msgId
     * @return
     */
    public WxFileIndex2 getMaxSizeWxFileIndex2ByMsgId(long msgId) {
        List<WxFileIndex2> wxFileIndex2List = getAllWxFileIndex2ByMsgId(msgId);
        //如果没找到，返回null
        if (CollectionUtils.isEmpty(wxFileIndex2List)) {
            return null;
        }
        if (wxFileIndex2List.size() == 1) {
            return wxFileIndex2List.get(0);
        }
        int maxIndex = 0;
        long maxSize = wxFileIndex2List.get(0).getSize();
        for (int i = 1; i < wxFileIndex2List.size(); i++) {
            WxFileIndex2 wxFileIndex2 = wxFileIndex2List.get(i);
            long eachSize = wxFileIndex2.getSize();
            if (eachSize > maxSize) {
                maxSize = eachSize;
                maxIndex = i;
            }
        }
        return wxFileIndex2List.get(maxIndex);
    }

    /**
     * 通过msgId找，size最大文件，直接返回本地文件File对象
     *
     * @param msgId
     * @return
     */
    public File getMaxSizeLocalFileByMsgId(long msgId) {
        WxFileIndex2 wxFileIndex2 = getMaxSizeWxFileIndex2ByMsgId(msgId);
        if (wxFileIndex2 == null) {
            return null;
        }
        File file = new File(Constants.RESOURCE_PATH + "/" + wxFileIndex2.getPath());
        if (file.exists() == false) {
            return null;
        } else {
            return file;
        }
    }

}
