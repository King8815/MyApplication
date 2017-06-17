package com.example.slide_table.utils;

import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * Created by user on 2016/11/29.
 */
public class WifiLinkUdp {
    private DatagramSocket socket;
    private static final String SERVERIP = "10.17.1.255";
    private final int SERVERPORT = 48899; // 端口号

    public WifiLinkUdp(){
        try {
            //创建一个DatagramSocket实例，并将该对象绑定到本机默认IP地址、本机所有可用端口中随机选择的某个端口
            socket = new DatagramSocket();
            socket.setSoTimeout(3000);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析整形的IP地址
     * @param i
     * @return
     */
    private String intToIp(int i) {

        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);
    }
}
