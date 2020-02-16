package com.eg.weixinchatlog.weixin.bean.enmicromsg;

import lombok.Data;

/**
 * 微信头像图片
 */
@Data
public class ImgFlag {
    private String username;
    private long imgflag;
    private long lastupdatetime;
    private String reserved1;
    private String reserved2;
    private long reserved3;
    private long reserved4;
}
