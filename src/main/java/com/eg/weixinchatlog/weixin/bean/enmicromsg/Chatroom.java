package com.eg.weixinchatlog.weixin.bean.enmicromsg;

import lombok.Data;

@Data
public class Chatroom {
    private String chatroomname;
    private long addtime;
    private String memberlist;
    private String displayname;
    private String chatroomnick;
    private long roomflag;
    private String roomowner;
    private byte[] roomdata;
    private long isShowname;
    private String selfDisplayName;
    private long style;
    private long chatroomdataflag;
    private String modifytime;
    private long chatroomnotice;
    private long chatroomVersion;
    private String chatroomnoticeEditor;
    private long chatroomnoticePublishTime;
    private long chatroomLocalVersion;
    private long memberCount;
    private long chatroomStatus;
}
