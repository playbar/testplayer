package com.mojing.vrplayer.publicc;

/**
 * 接口相关配置
 * Created by muyu on 2016/4/1.
 */
public class ConfigConstant {
    public static final String PACKAGE_NAME = "com.baofeng.mj";//包名
    public static final String STORAGE_DIR = "mojing";//存储目录
    public static final String STORAGE_FILE_DIR = "files/res";//U3D读取资源地址

    public static final String MJ_KEY_LIHAO="Bf@)(*$s1&2^3XVF#Mj";//魔镜加密
    public static final String MJ_KEY_1 ="w15q!@!^&72k5!%@hj!HJB132";//魔镜加密key
    public static final String MJ_KEY_2 = "41f836e3d488337eeb49b7f6e87175db";//魔镜加密key
    /**
     * http://192.168.12.66/projects/bf-mj-pt/wiki/%E6%B8%A0%E9%81%93%E4%BF%A1%E6%81%AF%E6%8E%A5%E5%8F%A3
     */
    public static final String MJ_KEY_3 = "744f95618927de20e56b60e41b0a44ef"; //魔镜加密key

    //支付相关参数
    public static String PAY_PLATFORM = "10";
    public static String PAY_CHANNEL = "13";
    public static String PAY_APPID = "3918465429995810";

   //登录相关参数
    public static final String APP_KEY      = "1117744451";
    //微信账号 app_id
    public static final String APP_KEY_WEIXIN = "wx3b2c51a5e12a93da";

    public static final String SECRET_WEIXIN = "3260db805c9c282ffee1d29ffe7d564c";
    // API密钥，在商户平台设置
    public static String WX_API_KEY = "822a62c5819a0df5dd4aef3a42473fc7";
    // 商户号
    public static String WX_MCH_ID = "1271529301";
    //首页获取信息流接口加密key
    public static final String RECOMMEND_KEY = "!Q2w#E4r%T";

    /**
     * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     * </p>
     */
    public static final String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     *
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     *
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     *
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

    /**
     * 魔镜加密
     */
    public static final String MJ_KEY="@)(*$s123";

    public static final String MJ_KEY_ZHANG_HONG_DONG="0p9o8i7u";
    //魔镜用户中心加密key
    public static final String MJ_UESR_CENTER_KEY = "0p9o8i7u";
    //魔镜 请求魔豆处理的key
    public static final String GET_MODOU_URL_KEY = "2a0848847cbf215111ff405c3ae680b8";

    //第三方登陆用的加密key
    public static final String MJ_UESR_PARTY_CHAR= "ead7e62df9";

    //暴风影音提供给暴风魔镜的 appid
    public static final String MJ_STORM_APP_ID= "20150909110001";
    //暴风影音提供给暴风魔镜的 appid
    public static final String MJ_STORM_APP_KEY= "62398ec5a5db83a5a22361df1c5318b7";
    //魔镜OTA key
    public static final String MJ_OTA_KEY = "cca0cebb4f91491547836f5c92a8b54f";
    //墨镜开屏图 key
    public static final String MJ_FLASH_KEY="cca0cebb4f91491547836f5c92a8b54f";

    //获取直播插件 key
    public static final String MJ_PLUGIN_KEY = "Bf@)(*$s1&2^3XVF#Mj";

    public static final int pageStart = 1;//起始页
    public static final int pageCount20 = 20;//每页个数
    public static final int pageCount12 = 12;//每页个数

    public static final int MOJING_PRO = 5;//魔镜pro版
    public static final int MOJING_MINI = 10;//魔镜mini版

}
