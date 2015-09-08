package org.abframe.websocket;

import org.java_websocket.WebSocket;

import java.util.*;

/**
 * 在线管理
 */
public class OnlineChatServerPool {

    private static final Map<WebSocket, String> userConnection = new HashMap<WebSocket, String>();

    /**
     * 获取用户名
     */
    public static String getUserByKey(WebSocket conn) {
        return userConnection.get(conn);
    }

    /**
     * 获取在线总数
     *
     * @param
     */
    public static int getUserCount() {
        return userConnection.size();
    }

    /**
     * 获取WebSocket
     *
     * @param user
     */
    public static WebSocket getWebSocketByUser(String user) {
        Set<WebSocket> keySet = userConnection.keySet();
        synchronized (keySet) {
            for (WebSocket conn : keySet) {
                String u = userConnection.get(conn);
                if (u.equals(user)) {
                    return conn;
                }
            }
        }
        return null;
    }

    /**
     * 向连接池中添加连接
     *
     * @param user
     * @param conn
     */
    public static void addUser(String user, WebSocket conn) {
        userConnection.put(conn, user);    //添加连接
    }

    /**
     * 获取所有的在线用户
     *
     * @return
     */
    public static Collection<String> getOnlineUser() {
        List<String> userList = new ArrayList<String>();
        Collection<String> userSet = userConnection.values();
        for (String user : userSet) {
            userList.add(user);
        }
        return userList;
    }

    /**
     * 移除连接池中的连接
     */
    public static boolean removeUser(WebSocket conn) {
        if (userConnection.containsKey(conn)) {
            userConnection.remove(conn);    //移除连接
            return true;
        } else {
            return false;
        }
    }

    /**
     * 向特定的用户发送数据
     *
     * @param message
     */
    public static void sendMessageToUser(WebSocket conn, String message) {
        if (null != conn) {
            conn.send(message);
        }
    }

    /**
     * 向所有的用户发送消息
     *
     * @param message
     */
    public static void sendMessage(String message) {
        Set<WebSocket> keySet = userConnection.keySet();
        synchronized (keySet) {
            for (WebSocket conn : keySet) {
                String user = userConnection.get(conn);
                if (user != null) {
                    conn.send(message);
                }
            }
        }
    }
}
