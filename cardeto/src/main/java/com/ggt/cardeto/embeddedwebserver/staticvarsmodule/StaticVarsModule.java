package com.ggt.cardeto.embeddedwebserver.staticvarsmodule;

import android.content.Context;

import com.ggt.cardeto.R;
import com.ggt.cardeto.embeddedwebserver.CardetoWebServerModule;
import com.ggt.cardeto.utils.CardetoConstants;

import java.lang.reflect.Field;
import java.util.Properties;

/**
 * This module shows static vars of a class at runtime.
 *
 * @author guiguito
 */
public class StaticVarsModule implements CardetoWebServerModule {

    private Context mContext;

    private static final String STATICVARS_MODULE_TAG = "staticvars";
    private static final String STATICVARS_VALUE_PARAM = "staticvarsvalue";

    public StaticVarsModule(Context context) {
        mContext = context;
    }

    @Override
    public String getModuleTitle() {
        return mContext.getString(R.string.staticvars_module_title);
    }

    @Override
    public String getDescription() {
        return mContext.getString(R.string.staticvars_module_description);
    }

    @Override
    public String getUrl() {
        return "./" + STATICVARS_MODULE_TAG;
    }

    @Override
    public boolean matchURI(String uri) {
        if (uri != null && uri.startsWith("/" + STATICVARS_MODULE_TAG))
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
        // check if there is a data for the class to check as an input
        if (params.containsKey(STATICVARS_VALUE_PARAM)) {
            String className = params.getProperty(STATICVARS_VALUE_PARAM);
            result.append(CardetoConstants.HTML_RETURN + "\n");
            result.append("<B>" + className + "</B>" + "\n");
            result.append(CardetoConstants.HTML_RETURN + "\n");
            result.append(CardetoConstants.HTML_RETURN + "\n");
            try {
                Class<?> classObj = Class.forName(className);
                Field[] fields = classObj.getFields();
                for (Field field : fields) {
                    String fieldName = field.getName();
                    try {
                        Object fieldValue = field.get(null);
                        result.append("<B>" + fieldName + "</B> : "
                                + fieldValue.toString());
                        result.append(CardetoConstants.HTML_RETURN + "\n");
                    } catch (IllegalArgumentException e) {
                    } catch (IllegalAccessException e) {
                    }
                }
            } catch (ClassNotFoundException e) {
                result.append(mContext.getString(R.string.class_not_found));
                result.append(CardetoConstants.HTML_RETURN + "\n");
            }

            result.append(CardetoConstants.HTML_RETURN + "\n");
        }
        result.append("<form enctype=\"text/plain\" method=\"get\" action=\"/"
                + STATICVARS_MODULE_TAG + "\">");
        result.append("<textarea cols=\"150\" rows=\"3\" name=\""
                + STATICVARS_VALUE_PARAM + "\">"
                + mContext.getString(R.string.full_classname_with_class_path)
                + "</textarea>");
        result.append(CardetoConstants.HTML_RETURN + "\n");
        result.append("<input type=\"submit\" value=\""
                + mContext.getString(R.string.send) + "\">");
        result.append(CardetoConstants.HTML_RETURN + "\n");
        result.append("</form>");
        result.append(CardetoConstants.HTML_RETURN + "\n");
        result.append("</form>");
        result.append(mContext.getString(R.string.html_footer));
        return result;
    }
}
