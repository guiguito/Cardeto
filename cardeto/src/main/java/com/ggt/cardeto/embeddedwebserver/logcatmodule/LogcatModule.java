package com.ggt.cardeto.embeddedwebserver.logcatmodule;

import android.content.Context;

import com.ggt.cardeto.R;
import com.ggt.cardeto.embeddedwebserver.CardetoWebServerModule;
import com.ggt.cardeto.utils.CardetoConstants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * This module display the logcat.
 *
 * @author guiguito
 */
public class LogcatModule implements CardetoWebServerModule {

    private Context mContext;

    private static final String LOGCAT_MODULE_TAG = "logcat";

    public LogcatModule(Context context) {
        mContext = context;
    }

    @Override
    public String getModuleTitle() {
        return mContext.getString(R.string.logcat_module_title);
    }

    @Override
    public String getDescription() {
        return mContext.getString(R.string.logcat_module_description);
    }

    @Override
    public String getUrl() {
        return "./" + LOGCAT_MODULE_TAG;
    }

    @Override
    public boolean matchURI(String uri) {
        if (uri != null && uri.startsWith("/" + LOGCAT_MODULE_TAG))
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
        appendLogcat(result);
        result.append(CardetoConstants.HTML_RETURN + "\n");
        result.append("</form>");
        result.append(mContext.getString(R.string.html_footer));
        return result;
    }

    private void appendLogcat(StringBuilder result) {
        try {
            Process process;
            process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.startsWith("V")) {
                    result.append("<font color=\"grey\">");
                    result.append(line);
                    result.append("</font>");
                } else if (line.startsWith("D")) {
                    result.append("<font color=\"blue\">");
                    result.append(line);
                    result.append("</font>");
                } else if (line.startsWith("I")) {
                    result.append("<font color=\"green\">");
                    result.append(line);
                    result.append("</font>");
                } else if (line.startsWith("W")) {
                    result.append("<font color=\"orange\">");
                    result.append(line);
                    result.append("</font>");
                } else {
                    result.append("<font color=\"red\">");
                    result.append(line);
                    result.append("</font>");
                }
                result.append(CardetoConstants.HTML_RETURN);
            }
        } catch (IOException e) {
            // do nothing
        }
    }

}
