package com.ggt.cardeto.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.http.conn.util.InetAddressUtils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.List;

/**
 * Some network utils.
 *
 * @author guiguito
 */
public class NetworkUtilsOld {

    private static final String LOG_TAG = "NetworkUtils";

    /**
     * Get IP address from first non-localhost interface
     *
     * @param ipv4 true=return ipv4, false=return ipv6
     * @return address or empty string
     */
    /*
    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                // TODO check wifi type
                List<InetAddress> addrs = Collections.list(intf
                        .getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress().toUpperCase();
                        boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 port
                                // suffix
                                return delim < 0 ? sAddr : sAddr.substring(0,
                                        delim);
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.toString());
        } // for now eat exceptions
        return "";
    }

    public static String getIPAddressForWifi(Context context) {
        String result = null;
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            byte[] bytes = BigInteger.valueOf(ipAddress).toByteArray();
            byte[] bytes2 = new byte[4];
            bytes2[0] = bytes[3];
            bytes2[1] = bytes[2];
            bytes2[2] = bytes[1];
            bytes2[3] = bytes[1];
            try {
                InetAddress address = InetAddress.getByAddress(bytes2);
                result = address.getHostAddress();
            } catch (UnknownHostException e) {
            }
        }
        return result;
    }
*/
}
