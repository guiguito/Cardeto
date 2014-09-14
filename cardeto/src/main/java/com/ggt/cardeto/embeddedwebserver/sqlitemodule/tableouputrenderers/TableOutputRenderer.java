package com.ggt.cardeto.embeddedwebserver.sqlitemodule.tableouputrenderers;

import java.util.List;

/**
 * Interface to implement to provide a class capable of rendering a SQLite Table
 *
 * @author gduche
 */
public interface TableOutputRenderer {

    public void renderHeader(StringBuilder output, List<String> tablesList);

    public void renderTable(StringBuilder output, String tableName,
                            List<String> columns, List<List<String>> rows);

    public void renderFooter(StringBuilder output);

}
