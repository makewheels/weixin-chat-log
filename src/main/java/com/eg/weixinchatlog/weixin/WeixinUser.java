package com.eg.weixinchatlog.weixin;

import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.Rcontact;
import com.eg.weixinchatlog.weixin.sqlite.enmicromsg.dao.EnMicroMsgDao;
import com.eg.weixinchatlog.weixin.sqlite.wxfileindex.dao.WxFileIndex2Dao;
import lombok.Data;

import java.io.File;
import java.util.List;

/**
 * 一个微信用户
 *
 * @time 2020-02-15 16:45
 */
@Data
public class WeixinUser {
    private String uin;
    private String mmUinMd5;
    private String sqlitePassword;
    private File mmFolder;

    private WeixinService weixinService;

    private List<Rcontact> rcontactList;//所有联系人
    private List<Rcontact> friendList;  //好友
    private List<Rcontact> chatRoomList;//群
    private List<Rcontact> officalList; //公众号
    private List<Rcontact> appList;     //小程序
}
