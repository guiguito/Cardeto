package com.ggt.cardeto.embeddedwebserver.appinfomodule;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ServiceInfo;

import com.ggt.cardeto.R;
import com.ggt.cardeto.embeddedwebserver.CardetoWebServerModule;
import com.ggt.cardeto.utils.CardetoConstants;

import java.util.Properties;

/**
 * This cardeto module display app info.
 *
 * @author guiguito
 */
public class AppInfoModule implements CardetoWebServerModule {

    private Context mContext;

    private static final String APPINFO_MODULE_TAG = "appinfo";

    public AppInfoModule(Context context) {
        mContext = context;
    }

    @Override
    public String getModuleTitle() {
        return mContext.getString(R.string.appinfo_module_title);
    }

    @Override
    public String getDescription() {
        return mContext.getString(R.string.appinfo_module_description);
    }

    @Override
    public String getUrl() {
        return "./" + APPINFO_MODULE_TAG;
    }

    @Override
    public boolean matchURI(String uri) {
        if (uri != null && uri.startsWith("/" + APPINFO_MODULE_TAG))
            return true;
        else
            return false;
    }

    @Override
    public StringBuilder handleRequest(String uri, String method,
                                       Properties header, Properties params, Properties files) {
        StringBuilder result = new StringBuilder();
        result.append(mContext.getString(R.string.html_header));
        result.append(CardetoConstants.HTML_RETURN + "\n");
        result.append(CardetoConstants.HTML_RETURN + "\n");
        result.append("<a href=\"/\">"
                + mContext.getString(R.string.back_to_modules_list) + "</a>"
                + CardetoConstants.HTML_RETURN + "\n");
        result.append(CardetoConstants.HTML_RETURN + "\n");

        try {
            result.append("<U><B>App name</B></U> : "
                    + mContext.getApplicationInfo().name);
            result.append(CardetoConstants.HTML_RETURN + "\n");
            result.append("<U><B>App class</B></U> : "
                    + mContext.getApplicationInfo().className);
            result.append(CardetoConstants.HTML_RETURN + "\n");
            result.append("<U><B>Version code</B></U> : "
                    + mContext.getPackageManager().getPackageInfo(
                    mContext.getApplicationInfo().packageName,
                    PackageManager.GET_RECEIVERS).versionCode);
            result.append(CardetoConstants.HTML_RETURN + "\n");
            result.append("<U><B>Version name</B></U> : "
                    + mContext.getPackageManager().getPackageInfo(
                    mContext.getApplicationInfo().packageName,
                    PackageManager.GET_RECEIVERS).versionName);
            result.append(CardetoConstants.HTML_RETURN + "\n");
            result.append(CardetoConstants.HTML_RETURN + "\n");

            ActivityInfo[] activitiesInfo = mContext.getPackageManager()
                    .getPackageInfo(mContext.getApplicationInfo().packageName,
                            PackageManager.GET_ACTIVITIES).activities;
            ServiceInfo[] servicesInfo = mContext.getPackageManager()
                    .getPackageInfo(mContext.getApplicationInfo().packageName,
                            PackageManager.GET_SERVICES).services;
            PermissionInfo[] permissionsInfo = mContext.getPackageManager()
                    .getPackageInfo(mContext.getApplicationInfo().packageName,
                            PackageManager.GET_PERMISSIONS).permissions;
            ActivityInfo[] receiversInfo = mContext.getPackageManager()
                    .getPackageInfo(mContext.getApplicationInfo().packageName,
                            PackageManager.GET_RECEIVERS).receivers;
            if (activitiesInfo != null) {
                result.append("<U><B>Activities</B></U>");
                result.append(CardetoConstants.HTML_RETURN + "\n");
                for (ActivityInfo activityInfo : activitiesInfo) {
                    result.append(activityInfo.toString());
                    result.append(CardetoConstants.HTML_RETURN + "\n");
                }
                result.append(CardetoConstants.HTML_RETURN + "\n");
            }
            if (servicesInfo != null) {
                result.append("<U><B>Services</B></U>");
                result.append(CardetoConstants.HTML_RETURN + "\n");
                for (ServiceInfo serviceInfo : servicesInfo) {
                    result.append(serviceInfo.toString());
                    result.append(CardetoConstants.HTML_RETURN + "\n");
                }
                result.append(CardetoConstants.HTML_RETURN + "\n");
            }
            if (permissionsInfo != null) {
                result.append("<U><B>Permissions</B></U>");
                result.append(CardetoConstants.HTML_RETURN + "\n");
                for (PermissionInfo permissionInfo : permissionsInfo) {
                    result.append(permissionInfo.toString());
                    result.append(CardetoConstants.HTML_RETURN + "\n");
                }
                result.append(CardetoConstants.HTML_RETURN + "\n");
            }
            if (receiversInfo != null) {
                result.append("<U><B>ReceiversInfo</B></U>");
                result.append(CardetoConstants.HTML_RETURN + "\n");
                for (ActivityInfo receiverInfo : receiversInfo) {
                    result.append(receiverInfo.toString());
                    result.append(CardetoConstants.HTML_RETURN + "\n");
                }
                result.append(CardetoConstants.HTML_RETURN + "\n");
            }
            // TODO add more app info

        } catch (PackageManager.NameNotFoundException e) {
            // Do nothing. Adapter will be empty.
        }
        result.append(CardetoConstants.HTML_RETURN + "\n");
        result.append(mContext.getString(R.string.html_footer));
        return result;
    }
}
