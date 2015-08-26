package org.abframe.websocket;

import org.java_websocket.WebSocket;

import java.util.*;

public class ChatServerPool {

    private static final Map<WebSocket, String> userconnections = new HashMap<WebSocket, String>();

    /**
     * 获取用户名
     *
     * @param ws
     */
    public static String getUserByKey(WebSocket ws) {
        return userconnections.get(ws);
    }

    /**
     * 获取WebSocket
     *
     * @param user
     */
    public static WebSocket getWebSocketByUser(String user) {
        Set<WebSocket> keySet = userconnections.keySet();
        synchronized (keySet) {
            for (WebSocket conn : keySet) {
                String cuser = userconnections.get(conn);
                if (cuser.equals(user)) {
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
     * @param ws
     */
    public static void addUser(String user, WebSocket ws) {
        userconnections.put(ws, user);    //添加连接
    }

    /**
     * 获取所有的在线用户
     *
     * @return
     */
    public static Collection<String> getOnlineUser() {
        List<String> setUsers = new ArrayList<String>();
        Collection<String> setUser = userconnections.values();
        for (String u : setUser) {
            setUsers.add("<a onclick=\"toUserMsg('" + u + "');\">" + u + "</a>");
        }
        return setUsers;
    }

    /**
     * 移除连接池中的连接
     *
     * @param ws
     */
    public static boolean removeUser(WebSocket ws) {
        if (userconnections.containsKey(ws)) {
            userconnections.remove(ws);    //移除连接
            return true;
        } else {
            return false;
        }
    }

    /**
     * 向特定的用户发送数据
     *
     * @param ws
     * @param message
     */
    public static void sendMessageToUser(WebSocket ws, String message) {
        if (null != ws && null != userconnections.get(ws)) {
            ws.send(message);
        }
    }

    /**
     * 向所有的用户发送消息
     *
     * @param message
     */
    public static void sendMessage(String message) {
        Set<WebSocket> keySet = userconnections.keySet();
        synchronized (keySet) {
            for (WebSocket conn : keySet) {
                String user = userconnections.get(conn);
                if (user != null) {
                    conn.send(message);
                }
            }
        }
    }
}
