package org.abframe.filter;

import org.abframe.websocket.ChatServer;
import org.abframe.websocket.OnlineChatServer;
import org.java_websocket.WebSocketImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WebsocketFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebsocketFilter.class);


    public void init(FilterConfig fc) throws ServletException {
        this.startWebsocketInstantMsg();
        this.startWebsocketOnline();
    }

    /**
     * 启动即时聊天服务
     */
    public void startWebsocketInstantMsg() {
        WebSocketImpl.DEBUG = false;
        int port = 8887;
        ChatServer s;
        try {
            s = new ChatServer(port);
            s.start();

            LOGGER.info("WebSocket server is start...,port is{}", s.getPort());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * 启动在线管理服务
     */
    public void startWebsocketOnline() {
        WebSocketImpl.DEBUG = false;
        int port = 8889;
        OnlineChatServer s;
        try {
            s = new OnlineChatServer(port);
            s.start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    //计时器
    public void timer() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date time = calendar.getTime();        // 得出执行任务的时间

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                //PersonService personService = (PersonService)ApplicationContext.getBean("personService");


                //System.out.println("-------设定要指定任务--------");
            }
        }, time, 1000 * 60 * 60 * 24);// 这里设定将延时每天固定执行
    }


    public void destroy() {

    }

    public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2) throws IOException, ServletException {

    }

}
