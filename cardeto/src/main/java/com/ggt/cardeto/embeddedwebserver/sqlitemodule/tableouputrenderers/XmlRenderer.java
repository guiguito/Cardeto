package com.ggt.cardeto.embeddedwebserver.sqlitemodule.tableouputrenderers;

import java.util.List;

/**
 * Simple XML renderer.
 * <p/>
 * Needs a lot improvements, tests and fixes.
 *
 * @author idapps103
 */
public class XmlRenderer implements TableOutputRenderer {

    @Override
    public void renderHeader(StringBuilder output, List<String> tablesList) {
        output.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n");
    }

    @Override
    public void renderTable(StringBuilder output, String tableName,
                            List<String> columns, List<List<String>> rows) {
        for (List<String> row : rows) {
            output.append("<" + tableName + ">\n");
            for (int i = 0; i < row.size(); i++) {
                output.append("<" + columns.get(i) + ">");
                output.append(row.get(i));
                output.append("</" + columns.get(i) + ">\n");
            }
            output.append("</" + tableName + ">\n");
        }
    }

    @Override
    public void renderFooter(StringBuilder output) {
        output.append("</xml>\n");
    }

}
