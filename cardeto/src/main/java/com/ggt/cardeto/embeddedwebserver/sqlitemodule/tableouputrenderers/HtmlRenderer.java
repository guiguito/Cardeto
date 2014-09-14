package com.ggt.cardeto.embeddedwebserver.sqlitemodule.tableouputrenderers;

import android.content.Context;
import android.text.TextUtils;

import com.ggt.cardeto.R;
import com.ggt.cardeto.utils.CardetoConstants;

import java.util.List;

/**
 * Simple HTML renderer.
 * <p/>
 * Needs a lot improvements, tests and fixes.
 *
 * @author idapps103
 */
public class HtmlRenderer implements TableOutputRenderer {

    private Context mContext;

    public HtmlRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void renderHeader(StringBuilder output, List<String> tablesList) {
        output.append(mContext.getString(R.string.html_header));
        output.append(CardetoConstants.HTML_RETURN + "\n");

        if (tablesList != null) {
            for (String tableName : tablesList) {
                output.append("<B><a href=\"#" + tableName + "\">" + tableName
                        + "</a></B>" + CardetoConstants.HTML_RETURN + "\n");
            }
        }
        output.append(CardetoConstants.HTML_RETURN + "\n");
    }

    @Override
    public void renderTable(StringBuilder output, String tableName,
                            List<String> columns, List<List<String>> rows) {
        output.append("<B><a name=\"" + tableName + "\">" + tableName
                + "</a></B>" + CardetoConstants.HTML_RETURN + "\n");
        // display columns
        output.append("<table>\n<tbody>\n");
        if (columns != null && columns.size() > 0) {
            output.append("<tr>\n");
            for (String column : columns) {
                output.append("<td>\n");
                output.append("<B>" + column + "</B>\n");
                output.append("</td>\n");
            }
            output.append("</tr>\n");
        }
        // display rows
        for (List<String> row : rows) {
            output.append("<tr>\n");
            for (String data : row) {
                output.append("<td>\n");
                if (data != null) {
                    output.append(TextUtils.htmlEncode(data) + "\n");
                }
                output.append("</td>\n");
            }
            output.append("</tr>\n");
        }
        output.append("</tbody>\n</table>\n");
        output.append(CardetoConstants.HTML_RETURN + "\n");
    }

    @Override
    public void renderFooter(StringBuilder output) {
        output.append(CardetoConstants.HTML_RETURN + "\n");
        output.append(mContext.getString(R.string.html_footer));
    }

}
