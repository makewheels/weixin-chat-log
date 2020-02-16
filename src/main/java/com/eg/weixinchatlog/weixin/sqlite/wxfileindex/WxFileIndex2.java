package com.eg.weixinchatlog.weixin.sqlite.wxfileindex;

import lombok.Data;

@Data
public class WxFileIndex2 {
    private String msgId;
    private String username;
    private long msgType;
    private long msgSubType;
    private String path;
    private long size;
    private String msgtime;
}