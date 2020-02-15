package com.eg.weixinchatlog.weixin;

import lombok.Data;

import java.io.File;

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
    private EnMicroMsgDao enMicroMsgDao;

}
