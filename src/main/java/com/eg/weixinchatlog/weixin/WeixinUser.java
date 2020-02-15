package com.eg.weixinchatlog.weixin;

import com.eg.weixinchatlog.weixin.bean.enmicromsg.Rcontact;
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
    private String mmuinMd5;
    private String sqlitePassword;
    private File mmFolder;
    private WeixinService weixinService;
    private EnMicroMsgDao enMicroMsgDao;
    private WxFileIndex2Dao wxFileIndexDao;
    private List<Rcontact> rcotactList;//所有联系人
    private List<Rcontact> friendList;//经过过滤之后，
}
