package com.eg.weixinchatlog.run;

import com.eg.weixinchatlog.util.MessageType;
import com.eg.weixinchatlog.weixin.WeixinService;
import com.eg.weixinchatlog.weixin.WeixinUser;
import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.Message;
import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.Rcontact;
import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * @time 2020-02-15 16:52
 */
@Data
public class Start {
    //select msgId,msgSvrId,type,status,isSend,createTime,talker,content,imgPath,reserved,talkerId,bizClientMsgId,msgSeq from message
    private static List<WeixinUser> weixinUserList;

    public static void main(String[] args) {
        weixinUserList = new WeixinService().initWeixinUserList();
        //遍历每一个手机登录的微信用户
        for (WeixinUser weixinUser : weixinUserList) {
            WeixinService weixinService = weixinUser.getWeixinService();
            //初始化联系人
            weixinService.initContacts(weixinUser);
            //遍历每一个朋友联系人
            List<Rcontact> friendList = weixinUser.getFriendList();
            for (Rcontact friend : friendList) {
                String username = friend.getUsername();
                String avatarUrl = friend.getAvatarUrl();
                if (avatarUrl != null) {
//                    System.out.println(avatarUrl);
                }
                long messageCount = weixinService.getMessageCountByTalker(username);
                if (messageCount == 0) {
                    continue;
                }
                System.out.println(username);
                System.out.println(messageCount);
                //在message表中查出消息
                List<Message> messageList = weixinService.getAllMessageByTalker(username);
                //遍历每一条消息
                for (Message message : messageList) {
                    int isSend = message.getIsSend();
                    System.out.print("是否是发送者" + isSend + " ");
                    long msgId = message.getMsgId();
                    //判断消息类型
                    long type = message.getType();
                    if (type == MessageType.TEXT) {
                        System.out.println(message.getContent());
                    } else if (type == MessageType.IMAGE) {
                        System.out.print("【图片消息】 ");
                        File file = weixinService.getMaxSizeLocalFileByMsgId(msgId);
                        if (file != null) {
                            System.out.println(file.getAbsolutePath());
                        } else {
                            System.err.println("image not exist");
                        }
                    } else if (type == MessageType.VOICE) {
                        System.out.print("【语音消息】 " + message.getContent() + " ");
                        File file = weixinService.getMaxSizeLocalFileByMsgId(msgId);
                        if (file != null) {
                            System.out.println(file.getAbsolutePath());
                        } else {
                            System.err.println("voice not exist");
                        }
                    } else if ((type == MessageType.VIDEO)) {
                        System.out.print("【视频消息】 " + message.getContent() + " ");
                        File file = weixinService.getMaxSizeLocalFileByMsgId(msgId);
                        if (file != null) {
                            System.out.println(file.getAbsolutePath());
                        }
                    } else if ((type == MessageType.SYSTEM)) {
                        System.out.print("【系统消息】 " + message.getContent() + " ");
                        System.out.println(message.getContent());
                    }
                }
                //消息遍历结束
                messageList.clear();
            }
        }
    }
}
