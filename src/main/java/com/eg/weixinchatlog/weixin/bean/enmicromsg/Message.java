package com.eg.weixinchatlog.weixin.bean.enmicromsg;

import lombok.Data;

@Data
public class Message {
    private long msgId;
    private long msgSvrId;
    private long type;
    private int status;
    private int isSend;
    private int isShowTimer;
    private long createTime;
    private String talker;
    private String content;
    private String imgPath;
    private String reserved;
    private byte[] lvbuffer;
    private String transContent;
    private String transBrandWording;
    private int talkerId;
    private String bizClientMsgId;
    private int bizChatId;
    private String bizChatUserId;
    private long msgSeq;
    private int flag;
}