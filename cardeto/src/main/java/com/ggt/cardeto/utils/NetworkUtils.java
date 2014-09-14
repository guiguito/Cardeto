package com.ggt.cardeto.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * Network utils. Detect if wifi is activated. Get ip address on wifi network,
 * etc ...
 *
 * @author guiguito
 */
public class NetworkUtils {

    public static class WifiDetails {
        public String ipAddress;
        public String ssid;
    }

    private static final String LOG_TAG = "NetworkUtils";

    public static WifiDetails getWifiDetails(Context context) {
        WifiDetails wifiDetails = null;
        if (isWifiApActivated(context)) {
            wifiDetails = new NetworkUtils.WifiDetails();
            // phone as HotSpot case
            WifiConfiguration wifiConfiguration = getWifiApConfiguration(context);
            if (wifiConfiguration != null) {
                wifiDetails.ssid = wifiConfiguration.SSID;
                wifiDetails.ipAddress = getWifiApIpAddress();
            }
        } else {
            // phone maybe connected on wifi normally
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (networkInfo.isConnected()) {
                wifiDetails = new NetworkUtils.WifiDetails();
                final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
                if (connectionInfo != null && !TextUtils.isEmpty(connectionInfo.getSSID())) {
                    wifiDetails.ssid = connectionInfo.getSSID();
                }
                if (connectionInfo != null) {
                    wifiDetails.ipAddress = convertInToIpAddress(connectionInfo.getIpAddress());
                }
                return wifiDetails;
            }
        }

        return wifiDetails;
    }

    private static String getWifiApIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                if (intf.getName().contains("wlan")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && (inetAddress.getAddress().length == 4)) {
                            return inetAddress.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }

    private static WifiConfiguration getWifiApConfiguration(Context context) {
        WifiConfiguration wifiConfiguration = null;
        try {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            Method method = manager.getClass().getDeclaredMethod("getWifiApConfiguration");
            method.setAccessible(true); // in the case of visibility change in
            // future APIs
            wifiConfiguration = (WifiConfiguration) method.invoke(manager);
        } catch (NoSuchMethodException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return wifiConfiguration;

    }

    private static boolean isWifiApActivated(Context context) {
        boolean value = false;
        try {
            WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            Method method = manager.getClass().getDeclaredMethod("isWifiApEnabled");
            method.setAccessible(true); // in the case of visibility change in
            // future APIs
            value = (Boolean) method.invoke(manager);
        } catch (NoSuchMethodException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }
        return value;
    }

    private static String convertInToIpAddress(int ipAddress) {
        byte[] bytes = BigInteger.valueOf(ipAddress).toByteArray();
        byte[] bytes2 = new byte[4];
        bytes2[0] = bytes[3];
        bytes2[1] = bytes[2];
        bytes2[2] = bytes[1];
        bytes2[3] = bytes[0];
        try {
            InetAddress address = InetAddress.getByAddress(bytes2);
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

}
