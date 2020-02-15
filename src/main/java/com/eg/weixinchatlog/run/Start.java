package com.eg.weixinchatlog.run;

import com.eg.weixinchatlog.util.MessageType;
import com.eg.weixinchatlog.weixin.EnMicroMsgDao;
import com.eg.weixinchatlog.util.CalcUtil;
import com.eg.weixinchatlog.util.Constants;
import com.eg.weixinchatlog.weixin.WeixinService;
import com.eg.weixinchatlog.weixin.WeixinUser;
import com.eg.weixinchatlog.weixin.WxFileIndex2Dao;
import com.eg.weixinchatlog.weixin.bean.enmicromsg.Message;
import com.eg.weixinchatlog.weixin.bean.enmicromsg.Rcontact;
import com.eg.weixinchatlog.weixin.bean.wxfileindex.WxFileIndex2;
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
            //设置EnMicroMsg.db文件
            File enMicroMsg_decrypt_db_file = new File(Constants.DATA_PATH + "/MicroMsg/"
                    + mmUinMd5 + "/EnMicroMsg-decrypt.db");
            EnMicroMsgDao enMicroMsgDao = new EnMicroMsgDao();
            enMicroMsgDao.setWeixinUser(weixinUser);
            enMicroMsgDao.setDbFile(enMicroMsg_decrypt_db_file);
            enMicroMsgDao.initConnection();
            weixinUser.setEnMicroMsgDao(enMicroMsgDao);
            //设置WxFileIndex.db文件
            File wxFileIndex_decrypt_db_file = new File(Constants.DATA_PATH + "/MicroMsg/"
                    + mmUinMd5 + "/WxFileIndex-decrypt.db");
            WxFileIndex2Dao wxFileIndex2Dao = new WxFileIndex2Dao();
            wxFileIndex2Dao.setWeixinUser(weixinUser);
            wxFileIndex2Dao.setDbFile(wxFileIndex_decrypt_db_file);
            wxFileIndex2Dao.initConnection();
            weixinUser.setWxFileIndexDao(wxFileIndex2Dao);
            //初始化service
            WeixinService weixinService = new WeixinService();
            weixinService.setWeixinUser(weixinUser);
            weixinService.setEnMicroMsgDao(enMicroMsgDao);
            weixinService.setWxFileIndex2Dao(wxFileIndex2Dao);
            weixinUser.setWeixinService(weixinService);
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
                //在message表中查出消息
                String username = friend.getUsername();
                long messageCount = weixinUser.getWeixinService().getMessageCountByTalker(username);
                if (messageCount == 0)
                    continue;
                System.out.println(username);
                System.out.println(messageCount);
                List<Message> messageList = weixinUser.getWeixinService().getAllMessageByTalker(username);
                //遍历每一条消息
                for (Message message : messageList) {
                    long msgId = message.getMsgId();
                    //判断消息类型
                    long type = message.getType();
                    if (type == MessageType.TEXT) {
                        System.out.println(message.getContent());
                    } else if (type == MessageType.IMAGE) {
                        WxFileIndex2 wxfile = weixinUser.getWeixinService().getMaxSizeWeixinFileByMsgId(msgId);
                        String path = wxfile.getPath();
                        File file = new File(Constants.RESOURCE_PATH + "/" + path);
                        System.out.println(file.lastModified());
                    } else if (type == MessageType.VOICE) {
                        WxFileIndex2 wxfile = weixinUser.getWeixinService().getMaxSizeWeixinFileByMsgId(msgId);
                        String path = wxfile.getPath();
                        File file = new File(Constants.RESOURCE_PATH + "/" + path);
                        System.out.println(file.lastModified());
                    } else if ((type == MessageType.VIDEO)) {
                        WxFileIndex2 wxfile = weixinUser.getWeixinService().getMaxSizeWeixinFileByMsgId(msgId);
                        String path = wxfile.getPath();
                        File file = new File(Constants.RESOURCE_PATH + "/" + path);
                        System.out.println(file.lastModified());
                    } else if ((type == MessageType.SYSTEM)) {
                        System.out.println(message.getContent());
                    }
                }
            }
        }
    }
}
