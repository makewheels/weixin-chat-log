package com.eg.weixinchatlog.run;

import com.eg.weixinchatlog.util.CalcUtil;
import com.eg.weixinchatlog.util.Constants;
import com.eg.weixinchatlog.util.MessageType;
import com.eg.weixinchatlog.weixin.dao.EnMicroMsgDao;
import com.eg.weixinchatlog.weixin.WeixinService;
import com.eg.weixinchatlog.weixin.WeixinUser;
import com.eg.weixinchatlog.weixin.dao.WxFileIndex2Dao;
import com.eg.weixinchatlog.weixin.bean.enmicromsg.Message;
import com.eg.weixinchatlog.weixin.bean.enmicromsg.Rcontact;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @time 2020-02-15 16:52
 */
@Data
public class Start {
    //select msgId,msgSvrId,type,status,isSend,createTime,talker,content,imgPath,reserved,talkerId,bizClientMsgId,msgSeq from message
    private List<WeixinUser> weixinUserList = new ArrayList<>();

    /**
     * 设置EnMicroMsg.db文件
     *
     * @param weixinUser
     */
    private void initEnMicroMsgDb(WeixinUser weixinUser) {
        File enMicroMsg_decrypt_db_file = new File(Constants.DATA_PATH + "/MicroMsg/"
                + weixinUser.getMmuinMd5() + "/EnMicroMsg-decrypt.db");
        EnMicroMsgDao enMicroMsgDao = new EnMicroMsgDao();
        enMicroMsgDao.setWeixinUser(weixinUser);
        enMicroMsgDao.setDbFile(enMicroMsg_decrypt_db_file);
        enMicroMsgDao.initConnection();
        weixinUser.getWeixinService().setEnMicroMsgDao(enMicroMsgDao);
    }

    /**
     * 设置WxFileIndex.db文件
     *
     * @param weixinUser
     */
    private void initWxFileIndexDb(WeixinUser weixinUser) {
        File wxFileIndex_decrypt_db_file = new File(Constants.DATA_PATH + "/MicroMsg/"
                + weixinUser.getMmuinMd5() + "/WxFileIndex-decrypt.db");
        WxFileIndex2Dao wxFileIndex2Dao = new WxFileIndex2Dao();
        wxFileIndex2Dao.setWeixinUser(weixinUser);
        wxFileIndex2Dao.setDbFile(wxFileIndex_decrypt_db_file);
        wxFileIndex2Dao.initConnection();
        weixinUser.setWxFileIndexDao(wxFileIndex2Dao);
        weixinUser.getWeixinService().setWxFileIndex2Dao(wxFileIndex2Dao);
    }

    /**
     * 初始化微信用户列表
     */
    private void initWeixinUserList() {
        //拿到uin列表
        List<String> uinList = CalcUtil.getUinList();
        //遍历每一个uin
        for (String uin : uinList) {
            WeixinUser weixinUser = new WeixinUser();
            weixinUser.setUin(uin);
            String mmUinMd5 = CalcUtil.getMmUinMd5(uin);
            weixinUser.setMmuinMd5(mmUinMd5);
            weixinUser.setSqlitePassword(CalcUtil.getSqlitePassword(uin));
            weixinUser.setMmFolder(new File(Constants.DATA_PATH + "/MicroMsg/" + mmUinMd5));
            //初始化service
            WeixinService weixinService = new WeixinService();
            weixinService.setWeixinUser(weixinUser);
            weixinUser.setWeixinService(weixinService);
            //初始化db文件
            initEnMicroMsgDb(weixinUser);
            initWxFileIndexDb(weixinUser);
            weixinUserList.add(weixinUser);
        }
    }

    public static void main(String[] args) {
        Start start = new Start();
        start.initWeixinUserList();
        List<WeixinUser> weixinUserList = start.getWeixinUserList();
        //遍历每一个手机登录的微信用户
        for (WeixinUser weixinUser : weixinUserList) {
            //初始化联系人
            weixinUser.getWeixinService().initRcontactList();
            weixinUser.getWeixinService().initFriendList();
            List<Rcontact> friendList = weixinUser.getFriendList();
            //遍历每一个联系人
            for (Rcontact friend : friendList) {
                WeixinService service = weixinUser.getWeixinService();
                //在message表中查出消息
                String username = friend.getUsername();
                long messageCount = service.getMessageCountByTalker(username);
                if (messageCount == 0)
                    continue;
                System.out.println(username);
                System.out.println(messageCount);
                List<Message> messageList = service.getAllMessageByTalker(username);
                //遍历每一条消息
                for (Message message : messageList) {
                    long msgId = message.getMsgId();
                    //判断消息类型
                    long type = message.getType();
                    if (type == MessageType.TEXT) {
                        System.out.println(message.getContent());
                    } else if (type == MessageType.IMAGE) {
                        File file = service.getMaxSizeLocalFileByMsgId(msgId);
                        if (file.exists()) {
                            System.out.println(file.getAbsolutePath());
                        } else {
                            System.err.println("not exist");
                        }
                    } else if (type == MessageType.VOICE) {
                        File file = service.getMaxSizeLocalFileByMsgId(msgId);
                        if (file.exists()) {
                            System.out.println(file.getAbsolutePath());
                        }
                    } else if ((type == MessageType.VIDEO)) {
                        File file = service.getMaxSizeLocalFileByMsgId(msgId);
                        if (file.exists()) {
                            System.out.println(file.getAbsolutePath());
                        }
                    } else if ((type == MessageType.SYSTEM)) {
                        System.out.println(message.getContent());
                    }
                }
            }
        }
    }
}
