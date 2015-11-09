package org.abframe.util;

import org.springframework.context.ApplicationContext;

public class Constant {

    public static final String SESSION_SECURITY_CODE = "sessionSecCode";

    public static final String SESSION_USER = "sessionUser";

    public static final String SESSION_ROLE_RIGHTS = "sessionRoleRights";

    /**
     * 当前菜单
     */
    public static final String SESSION_menuList = "menuList";

    /**
     * 全部菜单
     */
    public static final String SESSION_allmenuList = "allmenuList";

    public static final String SESSION_QX = "QX";

    public static final String SESSION_userpds = "userpds";

    public static final String SESSION_USERROL = "USERROL";                //用户对象

    public static final String SESSION_USERNAME = "USERNAME";            //用户名

    public static final String LOGIN = "/toLogin";                //登录地址


    public static final String EMAIL = "admin/config/EMAIL.txt";        //邮箱服务器配置路径

    public static final String FILEPATHIMG = "upload/img/";    //图片上传路径

    public static final String FILEPATHFILE = "uploadFiles/file/";        //文件上传路径

    public static String PICTURE_VISIT_FILE_PATH = "";//图片访问的路径

    public static String PICTURE_SAVE_FILE_PATH = "";//图片存放的路径

    public static final int WEB_SOCKET_PORT = 8887;

    /**
     * 用户建立连接
     */
    public static final String WEB_SOCKET_USER_CONN = "WSUC";

    /**
     * 用户下线
     */
    public static final String WEB_SOCKET_USER_LEAVE = "WSUL";

    /**
     * 消息发送（私信）
     */
    public static final String WEB_SOCKET_USER_PRIVATE_MSG = "PRIVATE";

    public static final String WEB_SOCKET_USER_PRIVATE_MSG_2 = "PRIVATE2";


    /**
     * 二维码存放路径
     */
    public static final String FILEPATHTWODIMENSIONCODE = "uploadFiles/twoDimensionCode/";

    //不对匹配该值的访问路径拦截（正则）
    public static final String NO_INTERCEPTOR_PATH = ".*/((login)|(toLogin)|(logout)|(validateCode)|(app)|(weixin)|(static)|(main)|(websocket)).*";


    public static ApplicationContext WEB_APP_CONTEXT = null; //该值会在web容器启动时由WebAppContextListener初始化

    /**
     * APP Constants
     */
    //app注册接口_请求协议参数)
    public static final String[] APP_REGISTERED_PARAM_ARRAY = new String[]{"countries", "uname", "passwd", "title", "full_name", "company_name", "countries_code", "area_code", "telephone", "mobile"};

    public static final String[] APP_REGISTERED_VALUE_ARRAY = new String[]{"国籍", "邮箱帐号", "密码", "称谓", "名称", "公司名称", "国家编号", "区号", "电话", "手机号"};

    //app根据用户名获取会员信息接口_请求协议中的参数
    public static final String[] APP_GETAPPUSER_PARAM_ARRAY = new String[]{"USERNAME"};

    public static final String[] APP_GETAPPUSER_VALUE_ARRAY = new String[]{"用户名"};


    public static String getPICTURE_VISIT_FILE_PATH() {
        return PICTURE_VISIT_FILE_PATH;
    }

    public static void setPICTURE_VISIT_FILE_PATH(String pICTURE_VISIT_FILE_PATH) {
        PICTURE_VISIT_FILE_PATH = pICTURE_VISIT_FILE_PATH;
    }

    public static String getPICTURE_SAVE_FILE_PATH() {
        return PICTURE_SAVE_FILE_PATH;
    }

    public static void setPICTURE_SAVE_FILE_PATH(String pICTURE_SAVE_FILE_PATH) {
        PICTURE_SAVE_FILE_PATH = pICTURE_SAVE_FILE_PATH;
    }


}
