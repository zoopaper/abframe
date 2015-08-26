package org.abframe.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class PublicUtil {


    public static String getPorjectPath() {
        String nowpath = "";
        nowpath = System.getProperty("user.dir") + "/";
        return nowpath;
    }

    /**
     * 获取本机ip
     *
     * @return
     */
    public static String getIp() {
        String ip = "";
        try {
            InetAddress inet = InetAddress.getLocalHost();
            ip = inet.getHostAddress();
            //System.out.println("本机的ip=" + ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ip;
    }

}