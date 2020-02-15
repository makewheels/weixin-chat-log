package com.eg.weixinchatlog.util;

/**
 * 微信消息类型
 * <p>
 * 参考：
 * https://blog.csdn.net/ziyunyang/article/details/81534560
 *
 * @time 2020-02-15 23:17
 */
public class MessageType {
    public static long TEXT = 1;
    public static long IMAGE = 3;
    public static long VOICE = 34;

    public static long FRIEND_CONFIRM = 37;//好友确认
    public static long POSSIBLEFRIEND_MSG = 40;
    public static long BUSINESS_CARD = 42;//名片
    public static long VIDEO = 43;//视频
    public static long FACIAL_EXPRESSION = 47;//动画表情
    public static long POSITION = 48;//位置
    public static long SHARE_LINK = 49;//分享链接
    public static long VOIPMSG = 50;
    public static long WEIXIN_INIT = 51;//微信初始化
    public static long VOIPNOTIFY = 52;
    public static long VOIPINVITE = 53;
    public static long SHORT_VIDEO = 62;//小视频
    public static long SYSNOTICE = 9999;
    public static long SYSTEM = 10000;//系统
    public static long RECALL = 10002;//撤回

}
