package com.ggt.cardeto.embeddedwebserver;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

import com.ggt.cardeto.R;
import com.ggt.cardeto.externallib.NanoHTTPD;
import com.ggt.cardeto.utils.CardetoConstants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Nano http server of cardeto. Handles module management and dispatch.
 *
 * @author guiguito
 */
public class CardetoWebServer extends NanoHTTPD {

    private Context mContext;

    private ArrayList<CardetoWebServerModule> mModules = new ArrayList<CardetoWebServerModule>();

    public CardetoWebServer(Context context, int port) throws IOException {
        super(port, null);
        mContext = context;
    }

    public void addCardetoWebServerModule(
            CardetoWebServerModule cardetoWebServerModule) {
        mModules.add(cardetoWebServerModule);
    }

    public void removeCardetoWebServerModule(
            CardetoWebServerModule cardetoWebServerModule) {
        mModules.remove(cardetoWebServerModule);
    }

    public Response serve(String uri, String method, Properties header,
                          Properties parms, Properties files) {
        Looper.prepare();
        boolean isHandled = false;
        StringBuilder responsePage = new StringBuilder();
        for (CardetoWebServerModule cardetoWebServerModule : mModules) {
            if (cardetoWebServerModule.matchURI(uri)) {
                responsePage = cardetoWebServerModule.handleRequest(uri,
                        method, header, parms, files);
                isHandled = true;
                break;
            }
        }
        if (!isHandled) {
            // no module matched then display welcome page
            responsePage.append(mContext.getString(R.string.html_header));
            responsePage.append(TextUtils.htmlEncode(mContext
                    .getString(R.string.no_module_module))
                    + CardetoConstants.HTML_RETURN);
            if (mModules.size() > 0) {
                responsePage.append("<table>\n<tbody>\n");

                responsePage.append("<tr>\n");

                responsePage.append("<td>\n");
                responsePage.append(TextUtils.htmlEncode(mContext
                        .getString(R.string.module)) + "\n");
                responsePage.append("</td>\n");

                responsePage.append("<td>\n");
                responsePage.append(TextUtils.htmlEncode(mContext
                        .getString(R.string.description)) + "\n");
                responsePage.append("</td>\n");

                responsePage.append("<td>\n");
                responsePage.append(TextUtils.htmlEncode(mContext
                        .getString(R.string.url)) + "\n");
                responsePage.append("</td>\n");

                responsePage.append("</tr>\n");
                for (CardetoWebServerModule cardetoWebServerModule : mModules) {

                    responsePage.append("<tr>\n");

                    responsePage.append("<td>\n");
                    responsePage
                            .append(TextUtils.htmlEncode(cardetoWebServerModule
                                    .getModuleTitle()) + "\n");
                    responsePage.append("</td>\n");

                    responsePage.append("<td>\n");
                    responsePage
                            .append(TextUtils.htmlEncode(cardetoWebServerModule
                                    .getDescription()) + "\n");
                    responsePage.append("</td>\n");

                    responsePage.append("<td>\n");
                    responsePage.append("<a href=\""
                            + cardetoWebServerModule.getUrl() + "\">"
                            + cardetoWebServerModule.getUrl() + "</a>"
                            + CardetoConstants.HTML_RETURN + "\n");
                    responsePage.append("</td>\n");

                    responsePage.append("</tr>\n");
                }
                responsePage.append("</tbody>\n</table>\n");
            }
            responsePage.append(mContext.getString(R.string.html_footer));
        }

        return new NanoHTTPD.Response(NanoHTTPD.HTTP_OK, MIME_HTML,
                responsePage.toString());
    }
}
