package com.eg.weixinchatlog.weixin.bean;

import lombok.Data;

/**
 * @time 2020-02-15 18:22
 */
@Data
public class Contact {
    private String username;
    private String alias;
    private String conRemark;
    private String nickname;
    private Integer showHead;
    private Integer type;
    private String encryptUsername;
    private Integer chatroomFlag;
    private Integer deleteFlag;
    private String contactLabelIds;

}
