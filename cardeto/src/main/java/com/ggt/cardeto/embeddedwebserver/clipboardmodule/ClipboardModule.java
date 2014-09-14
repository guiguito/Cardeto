package com.ggt.cardeto.embeddedwebserver.clipboardmodule;

import android.content.ClipData;
import android.content.Context;

import com.ggt.cardeto.R;
import com.ggt.cardeto.embeddedwebserver.CardetoWebServerModule;
import com.ggt.cardeto.utils.CardetoConstants;

import java.util.Properties;

/**
 * This cardeto module is used to interact with the clipboard of your device.
 *
 * @author gduche
 */
public class ClipboardModule implements CardetoWebServerModule {

    private Context mContext;

    private static final String CLIPBOARD_MODULE_TAG = "clipboard";

    private static final String CLIPBOARD_VALUE_PARAM = "clipboardvalue";

    public ClipboardModule(Context context) {
        mContext = context;
    }

    @Override
    public String getModuleTitle() {
        return mContext.getString(R.string.clipboard_module_title);
    }

    @Override
    public String getDescription() {
        return mContext.getString(R.string.clipboard_module_description);
    }

    @Override
    public String getUrl() {
        return "./" + CLIPBOARD_MODULE_TAG;
    }

    @Override
    public boolean matchURI(String uri) {
        if (uri != null && uri.startsWith("/" + CLIPBOARD_MODULE_TAG))
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
        result.append("<a href=\"/\">"
                + mContext.getString(R.string.back_to_modules_list) + "</a>"
                + CardetoConstants.HTML_RETURN + "\n");

        // check if there is a data for the clipboard as an input
        if (params.containsKey(CLIPBOARD_VALUE_PARAM)) {
            String value = params.getProperty(CLIPBOARD_VALUE_PARAM);
            // set it in the clipboard
            setClipboardValue(value);
            result.append(mContext.getString(R.string.clipboard_value_modified)
                    + "\n");
            result.append(CardetoConstants.HTML_RETURN + "\n");
        }
        // check value of the clipboard and display it
        result.append(mContext.getString(R.string.current_clipboard_value)
                + CardetoConstants.HTML_RETURN + "<B>" + getClipboardValue()
                + "</B>\n");
        result.append(CardetoConstants.HTML_RETURN + "\n");
        result.append(mContext.getString(R.string.set_new_clipboard_value)
                + CardetoConstants.HTML_RETURN + "\n");
        // display text area and html form to send data to put in the clipboard
        result.append("<form enctype=\"text/plain\" method=\"get\" action=\"/"
                + CLIPBOARD_MODULE_TAG + "\">");
        result.append("<textarea cols=\"150\" rows=\"3\" name=\""
                + CLIPBOARD_VALUE_PARAM + "\">"
                + mContext.getString(R.string.new_clipboard_value)
                + "</textarea>");
        result.append(CardetoConstants.HTML_RETURN + "\n");
        result.append("<input type=\"submit\" value=\""
                + mContext.getString(R.string.send) + "\">");
        result.append(CardetoConstants.HTML_RETURN + "\n");
        result.append("</form>");
        result.append(mContext.getString(R.string.html_footer));
        return result;
    }

    private void setClipboardValue(String value) {
        if (android.os.Build.VERSION.SDK_INT <= 11) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mContext
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(value);
            // http://stackoverflow.com/questions/6624763/android-copy-to-clipboard-selected-text-from-a-textview
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText(value, value);
            clipboard.setPrimaryClip(clipData);
        }
    }

    private String getClipboardValue() {
        String value = "";
        if (android.os.Build.VERSION.SDK_INT < 11) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) mContext
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            value = clipboard.getText().toString();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) mContext
                    .getSystemService(Context.CLIPBOARD_SERVICE);
            value = clipboard.getPrimaryClip().toString();
        }
        return value;
    }
}