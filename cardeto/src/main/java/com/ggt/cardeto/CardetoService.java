package com.ggt.cardeto;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ggt.cardeto.embeddedwebserver.CardetoWebServer;
import com.ggt.cardeto.embeddedwebserver.appinfomodule.AppInfoModule;
import com.ggt.cardeto.embeddedwebserver.clipboardmodule.ClipboardModule;
import com.ggt.cardeto.embeddedwebserver.logcatmodule.LogcatModule;
import com.ggt.cardeto.embeddedwebserver.sqlitemodule.SQLiteModule;
import com.ggt.cardeto.embeddedwebserver.staticvarsmodule.StaticVarsModule;
import com.ggt.cardeto.utils.NetworkUtils;

import java.io.IOException;

/**
 * Cardeto foreground service. It hosts the cardeto webserver alive.
 *
 * @author guiguito
 */
public class CardetoService extends Service {

    private CardetoWebServer mCardetoWebServer;
    private static int ONGOING_NOTIFICATION = 1;

    public static final String CARDETO_PORT = "CARDETO_PORT";
    private static final int DEFAULT_PORT = 1500;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        try {
            int port = intent.getIntExtra(CARDETO_PORT, DEFAULT_PORT);
            mCardetoWebServer = new CardetoWebServer(this, port);
            // sqlite module
            mCardetoWebServer.addCardetoWebServerModule(new SQLiteModule(
                    getApplicationContext()));
            mCardetoWebServer.addCardetoWebServerModule(new ClipboardModule(
                    getApplicationContext()));
            mCardetoWebServer.addCardetoWebServerModule(new LogcatModule(
                    getApplicationContext()));
            mCardetoWebServer.addCardetoWebServerModule(new StaticVarsModule(
                    getApplicationContext()));
            mCardetoWebServer.addCardetoWebServerModule(new AppInfoModule(
                    getApplicationContext()));
            // TODO other modules

            // Foreground notification with ip address
            Notification notification = new Notification(
                    R.drawable.ic_launcher,
                    getText(R.string.cardeto_is_running),
                    System.currentTimeMillis());
            Intent notificationIntent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                    notificationIntent, 0);
            NetworkUtils.WifiDetails wifiDetails = NetworkUtils.getWifiDetails(this);
            notification.setLatestEventInfo(this,
                    getText(R.string.cardeto_is_running), "http:\\"
                            + wifiDetails.ipAddress + ":" + port + " on " + wifiDetails.ssid,
                    pendingIntent);
            startForeground(ONGOING_NOTIFICATION, notification);
        } catch (IOException e) {
            Log.e(getClass().toString(),
                    getString(R.string.cardeto_couldnt_start));
            Log.e(getClass().toString(), e.getMessage());
            stopSelf();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
        mCardetoWebServer.stop();
    }

}
