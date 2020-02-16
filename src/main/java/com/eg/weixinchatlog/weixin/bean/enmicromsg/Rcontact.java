package com.eg.weixinchatlog.weixin.bean.enmicromsg;

import lombok.Data;

@Data
public class Rcontact {
    private String username;
    private String alias;
    private String conRemark;
    private String domainList;
    private String nickname;
    private String pyInitial;
    private String quanPin;
    private int showHead;
    private int type;
    private int weiboFlag;
    private String weiboNickname;
    private String conRemarkPyFull;
    private String conRemarkPyShort;
    private byte[] lvbuff;
    private int verifyFlag;
    private String encryptUsername;
    private int chatroomFlag;
    private int deleteFlag;
    private String contactLabelIds;
    private String ticket;
    private String openImAppid;
    private String descWordingId;
    private String sourceExtInfo;

    //从这开始是我自己添加的字段
    private String avatarUrl;//头像url
    private String usernameMd5;
}
