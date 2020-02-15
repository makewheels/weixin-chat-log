package com.eg.weixinchatlog.weixin.bean.enmicromsg;

import lombok.Data;

@Data
public class Chatroom {
    private String chatroomname;
    private long addtime;
    private String memberlist;
    private String displayname;
    private String chatroomnick;
    private int roomflag;
    private String roomowner;
    private byte[] roomdata;
    private int isShowname;
    private String selfDisplayName;
    private int style;
    private int chatroomdataflag;
    private String modifytime;
    private long chatroomnotice;
    private int chatroomVersion;
    private String chatroomnoticeEditor;
    private long chatroomnoticePublishTime;
    private long chatroomLocalVersion;
    private int memberCount;
    private int chatroomStatus;
}
