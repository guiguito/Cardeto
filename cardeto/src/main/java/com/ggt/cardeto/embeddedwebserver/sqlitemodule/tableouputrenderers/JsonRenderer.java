package com.ggt.cardeto.embeddedwebserver.sqlitemodule.tableouputrenderers;

import java.util.List;

/**
 * Simple Json renderer.
 * 
 * Needs a lot improvements, tests and fixes.
 * 
 * @author idapps103
 * 
 */
public class JsonRenderer implements TableOutputRenderer {

	@Override
	public void renderHeader(StringBuilder output, List<String> tablesList) {
		output.append("{\n");
	}

	@Override
	public void renderTable(StringBuilder output, String tableName,
			List<String> columns, List<List<String>> rows) {
		output.append("\"" + tableName + "\":[\n");
		for (List<String> row : rows) {
			output.append("{");
			for (int i = 0; i < row.size(); i++) {
				output.append("\"" + columns.get(i) + "\":");
				output.append("\"" + row.get(i) + "\"");
				output.append(",");
			}
			if (row.size() > 0)
				output.deleteCharAt(output.length() - 1);
			output.append("},");
		}
		if (rows.size() > 0)
			output.deleteCharAt(output.length() - 1);
		output.append("]\n");
	}

	@Override
	public void renderFooter(StringBuilder output) {
		output.append("}\n");
	}

}
