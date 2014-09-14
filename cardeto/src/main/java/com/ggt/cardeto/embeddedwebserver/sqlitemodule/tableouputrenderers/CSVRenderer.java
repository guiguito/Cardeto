package com.ggt.cardeto.embeddedwebserver.sqlitemodule.tableouputrenderers;

import java.util.List;

/**
 * Simple CSV renderer.
 *
 * @author idapps103
 */
public class CSVRenderer implements TableOutputRenderer {

    @Override
    public void renderHeader(StringBuilder output, List<String> tablesList) {
        // do nothing
    }

    @Override
    public void renderTable(StringBuilder output, String tableName,
                            List<String> columns, List<List<String>> rows) {
        for (String column : columns) {
            output.append(column);
            output.append(",");
        }
        output.append("\n");
        for (List<String> row : rows) {
            for (String cell : row) {
                output.append(cell);
                output.append(",");
            }
            output.append("\n");
        }
    }

    @Override
    public void renderFooter(StringBuilder output) {
        // do nothing
    }

}
