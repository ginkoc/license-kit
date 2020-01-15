package com.ginko.license.checker.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 硬件信息辅助工具
 *
 * @author ginko
 * @date 8/28/19
 */
public class HardwareInfoUtils {

    /**
     * 获得物理机所有的ip
     *
     * @return ip列表
     * @throws SocketException 异常
     */
    public static List<String> getIpAddress() throws SocketException {
        List<String> result = new ArrayList<String>();
        List<InetAddress> addresses = getAllInetAddress();

        for (InetAddress address : addresses) {
            result.add(address.getHostAddress());
        }

        return result;
    }

    /**
     * 获得物理机所有的mac地址
     *
     * @return mac地址列表
     * @throws Exception 异常
     */
    public static List<String> getMacAddresses() throws Exception {
        List<String> result = new ArrayList<String>();
        List<InetAddress> addresses = getAllInetAddress();

        for (InetAddress address : addresses) {
            result.add(getMacByInetAddress(address));
        }

        return result;
    }

    private static List<InetAddress> getAllInetAddress() throws SocketException {
        List<InetAddress> results = new ArrayList<InetAddress>();

        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        // 遍历所有的网络接口
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();

            //遍历所有IP地址
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();

                //排除LoopbackAddress、SiteLocalAddress、LinkLocalAddress、MulticastAddress类型的IP地址
                if (!address.isLoopbackAddress() && !address.isLinkLocalAddress()
                        && !address.isMulticastAddress()) {
                    results.add(address);
                }
            }
        }

        return results;
    }

    private static String getMacByInetAddress(InetAddress address) throws SocketException {
        byte[] mac = NetworkInterface.getByInetAddress(address).getHardwareAddress();

        StringBuilder sb = new StringBuilder(3 * mac.length);

        for (int i = 0; i < mac.length; i++) {
            if (i != 0) {
                sb.append(":");
            }

            //将十六进制byte转化为字符串
            String temp = Integer.toHexString(mac[i] & 0xff);
            if (temp.length() == 1) {
                sb.append("0");
                sb.append(temp);
            } else {
                sb.append(temp);
            }
        }

        return sb.toString().toUpperCase();
    }
}
