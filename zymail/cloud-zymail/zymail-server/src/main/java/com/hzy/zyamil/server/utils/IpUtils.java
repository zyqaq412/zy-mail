package com.hzy.zyamil.server.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @title: IpUtils
 * @Author zxwyhzy
 * @Date: 2023/10/27 15:08
 * @Version 1.0
 */
public class IpUtils {
    public static String getIpaddr(){
        String ipaddr = "";
        try {
            InetAddress address = InetAddress.getLocalHost();
            ipaddr = address.getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        return ipaddr;
    }
}
