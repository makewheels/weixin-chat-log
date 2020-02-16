package com.eg.weixinchatlog.run;

import com.eg.weixinchatlog.util.WeixinUtil;
import com.eg.weixinchatlog.util.Constants;
import com.eg.weixinchatlog.util.MessageType;
import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.dao.EnMicroMsgDao;
import com.eg.weixinchatlog.weixin.WeixinService;
import com.eg.weixinchatlog.weixin.WeixinUser;
import com.eg.weixinchatlog.weixin.sqlite.wxfileindex.dao.WxFileIndex2Dao;
import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.Message;
import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.Rcontact;
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
    private static List<WeixinUser> weixinUserList;

    public static void main(String[] args) {
        WeixinService service = new WeixinService();
        weixinUserList = service.initWeixinUserList();
        //遍历每一个手机登录的微信用户
        for (WeixinUser weixinUser : weixinUserList) {
            WeixinService weixinService = weixinUser.getWeixinService();
            //初始化联系人
            weixinService.initContacts(weixinUser);
            //遍历每一个联系人
            List<Rcontact> friendList = weixinUser.getFriendList();
            for (Rcontact friend : friendList) {
                //在message表中查出消息
                String username = friend.getUsername();
                String avatarUrl = friend.getAvatarUrl();
                if (avatarUrl != null) {
                    System.out.println(avatarUrl);
                }
                long messageCount = weixinService.getMessageCountByTalker(username);
                if (messageCount == 0)
                    continue;
                System.out.println(username);
                System.out.println(messageCount);
                List<Message> messageList = weixinService.getAllMessageByTalker(username);
                //遍历每一条消息
                for (Message message : messageList) {
                    long msgId = message.getMsgId();
                    //判断消息类型
                    long type = message.getType();
                    if (type == MessageType.TEXT) {
                        System.out.println(message.getContent());
                    } else if (type == MessageType.IMAGE) {
                        File file = weixinService.getMaxSizeLocalFileByMsgId(msgId);
                        if (file.exists()) {
                            System.out.println(file.getAbsolutePath());
                        } else {
                            System.err.println("not exist");
                        }
                    } else if (type == MessageType.VOICE) {
                        File file = weixinService.getMaxSizeLocalFileByMsgId(msgId);
                        if (file.exists()) {
                            System.out.println(file.getAbsolutePath());
                        }
                    } else if ((type == MessageType.VIDEO)) {
                        File file = weixinService.getMaxSizeLocalFileByMsgId(msgId);
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
