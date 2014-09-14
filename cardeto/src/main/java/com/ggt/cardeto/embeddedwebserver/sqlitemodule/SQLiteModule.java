package com.ggt.cardeto.embeddedwebserver.sqlitemodule;

import java.util.List;
import java.util.Properties;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.ggt.cardeto.R;
import com.ggt.cardeto.embeddedwebserver.CardetoWebServerModule;
import com.ggt.cardeto.embeddedwebserver.sqlitemodule.tableouputrenderers.CSVRenderer;
import com.ggt.cardeto.embeddedwebserver.sqlitemodule.tableouputrenderers.HtmlRenderer;
import com.ggt.cardeto.embeddedwebserver.sqlitemodule.tableouputrenderers.JsonRenderer;
import com.ggt.cardeto.embeddedwebserver.sqlitemodule.tableouputrenderers.TableOutputRenderer;
import com.ggt.cardeto.embeddedwebserver.sqlitemodule.tableouputrenderers.XmlRenderer;
import com.ggt.cardeto.utils.CardetoConstants;

/**
 * 
 * Sqlite module for cardeto. Display Sqlite databases of an application.
 * 
 * @author guiguito
 * 
 */
public class SQLiteModule implements CardetoWebServerModule {

	private Context mContext;

	private static final String SQLITE_MODULE_TAG = "databases";

	// special tables
	private static final String ALL_TABLES = "all_tables";
	private static final String QUERY_DB = "query_db";

	// output format

	private static final String SQLITE_REQUEST_VALUE_PARAM = "sqliterequest";
	private static final String FORMAT_PARAM = "format";
	private static final String HTML_FORMAT = "html";
	private static final String CSV_FORMAT = "csv";
	private static final String JSON_FORMAT = "json";
	private static final String XML_FORMAT = "xml";

	public SQLiteModule(Context context) {
		mContext = context;
	}

	@Override
	public String getModuleTitle() {
		return mContext.getString(R.string.sqlite_module_title);
	}

	@Override
	public String getDescription() {
		return mContext.getString(R.string.sqlite_module_description);
	}

	@Override
	public String getUrl() {
		return "./" + SQLITE_MODULE_TAG;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ggt.cardeto.embeddedwebserver.CardetoWebServerModule#matchURI(java
	 * .lang.String)
	 */
	@Override
	public boolean matchURI(String uri) {
		if (uri != null && uri.startsWith("/" + SQLITE_MODULE_TAG))
			return true;
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.ggt.cardeto.embeddedwebserver.CardetoWebServerModule#handleRequest
	 * (java.lang.String, java.lang.String, java.util.Properties,
	 * java.util.Properties, java.util.Properties)
	 */
	@Override
	public StringBuilder handleRequest(String uri, String method,
			Properties header, Properties params, Properties files) {
		StringBuilder result = new StringBuilder();

		GenericSQLiteDatabaseHelper genericSQLiteDatabaseHelper = new GenericSQLiteDatabaseHelper(
				mContext);

		// see if database selected in URL exists
		String uriMinusHeader = uri.substring(uri.indexOf('/', 1) + 1);
		String dbName = uriMinusHeader;
		if (dbName.contains("/")) {
			dbName = dbName.substring(0, dbName.indexOf('/'));
		}
		// check if database name
		if (dbName.length() > 0
				&& genericSQLiteDatabaseHelper.checkAndOpenDataBase(dbName)) {
			// db found
			// check table name
			String tableName = null;
			String temp = uriMinusHeader
					.substring(uriMinusHeader.indexOf('/') + 1);
			if (temp.contains("/")) {
				tableName = temp.substring(0, temp.indexOf('/'));
			} else {
				tableName = temp;
			}
			if (tableName != null
					&& (genericSQLiteDatabaseHelper.doesTableExist(tableName) || tableName
							.equals(ALL_TABLES))) {
				// table found
				String format = HTML_FORMAT;
				if (params.containsKey(FORMAT_PARAM)) {
					format = params.getProperty(FORMAT_PARAM);
				}
				result.append(displayTableContent(genericSQLiteDatabaseHelper,
						tableName, format, true));
			} else if (tableName != null && tableName.equals(QUERY_DB)) {
				// case where we display a a textarea to put its own query
				result.append(mContext.getString(R.string.html_header));
				result.append(CardetoConstants.HTML_RETURN + "\n");
				result.append(mContext
						.getString(R.string.type_your_sqlite_request)
						+ CardetoConstants.HTML_RETURN + "\n");
				String request = null;
				if (params.containsKey(SQLITE_REQUEST_VALUE_PARAM)) {
					request = params.getProperty(SQLITE_REQUEST_VALUE_PARAM);
				}
				result.append("<form enctype=\"text/plain\" method=\"get\" action=\"/"
						+ SQLITE_MODULE_TAG
						+ "/"
						+ dbName
						+ "/"
						+ QUERY_DB
						+ "\">");
				result.append("<textarea cols=\"150\" rows=\"3\" name=\""
						+ SQLITE_REQUEST_VALUE_PARAM + "\">"
						+ mContext.getString(R.string.select_star)
						+ "</textarea>");
				result.append(CardetoConstants.HTML_RETURN + "\n");
				if (request != null) {
					result.append("<input type=\"submit\" value=\"" + request
							+ "\">");
				} else {
					result.append("<input type=\"submit\" value=\""
							+ mContext.getString(R.string.send) + "\">");
				}

				result.append(CardetoConstants.HTML_RETURN + "\n");
				result.append("</form>");
				if (request != null) {
					// a request is sent try to execute it
					try {
						List<List<String>> requestResult = genericSQLiteDatabaseHelper
								.executeRequest(request);
						result.append(mContext
								.getString(R.string.request_successfull)
								+ CardetoConstants.HTML_RETURN + "\n");
						HtmlRenderer htmlRenderer = new HtmlRenderer(mContext);
						htmlRenderer.renderTable(result, tableName, null,
								requestResult);
					} catch (SQLiteException e) {
						result.append(mContext
								.getString(R.string.request_error)
								+ CardetoConstants.HTML_RETURN + "\n");
						result.append("<B>" + e.getMessage() + "</B>"
								+ CardetoConstants.HTML_RETURN + "\n");
					}

				}
				result.append(mContext.getString(R.string.html_footer));

			} else {
				result.append(mContext.getString(R.string.html_header));
				result.append(CardetoConstants.HTML_RETURN + "\n");
				result.append(TextUtils.htmlEncode(String.format(
						mContext.getString(R.string.table_not_found_in), dbName))
						+ CardetoConstants.HTML_RETURN);
				result.append(displayTablesSummary(genericSQLiteDatabaseHelper,
						dbName));
				result.append(mContext.getString(R.string.html_footer));
			}
		} else {
			result.append(mContext.getString(R.string.html_header));
			result.append(CardetoConstants.HTML_RETURN + "\n");
			result.append("<a href=\"/\">"
					+ mContext.getString(R.string.back_to_modules_list)
					+ "</a>" + CardetoConstants.HTML_RETURN + "\n");
			// display all database available
			result.append(TextUtils.htmlEncode(mContext
					.getString(R.string.db_not_found))
					+ CardetoConstants.HTML_RETURN);
			result.append(displayDatabasesSummary(genericSQLiteDatabaseHelper));
			result.append(mContext.getString(R.string.html_footer));
		}
		genericSQLiteDatabaseHelper.closeDatabase();
		return result;
	}

	private String displayDatabasesSummary(
			GenericSQLiteDatabaseHelper genericSQLiteDatabaseHelper) {
		String result = "";
		List<String> databases = genericSQLiteDatabaseHelper.getDatabasesList();
		for (String dbName : databases) {
			result += "<a href=\"/" + SQLITE_MODULE_TAG + "/" + dbName + "\">"
					+ dbName + "</a>" + CardetoConstants.HTML_RETURN + "\n";
		}
		return result;
	}

	private String displayTablesSummary(
			GenericSQLiteDatabaseHelper genericSQLiteDatabaseHelper,
			String dbName) {
		String result = "";
		List<String> tables = genericSQLiteDatabaseHelper.getTablesList();
		if (tables.size() > 0) {
			result += "<h1><a href=\"/" + SQLITE_MODULE_TAG + "/" + dbName
					+ "/" + QUERY_DB + "\">"
					+ mContext.getString(R.string.query_database) + "</a></h1>"
					+ CardetoConstants.HTML_RETURN + "\n";
		}
		if (tables.size() > 0) {
			result += "<h1><a href=\"/" + SQLITE_MODULE_TAG + "/" + dbName
					+ "/" + ALL_TABLES + "\">"
					+ mContext.getString(R.string.all_database_tables)
					+ "</a></h1>" + CardetoConstants.HTML_RETURN + "\n";
		}
		for (String tableName : tables) {
			result += "<h1><a href=\"/" + SQLITE_MODULE_TAG + "/" + dbName
					+ "/" + tableName + "\">" + tableName + "</a></h1>\n";
			// link for csv format
			result += "<a href=\"/" + SQLITE_MODULE_TAG + "/" + dbName + "/"
					+ tableName + "?" + FORMAT_PARAM + "=" + CSV_FORMAT + "\">"
					+ CSV_FORMAT + "</a>\n";
			// link for xml format
			result += "<a href=\"/" + SQLITE_MODULE_TAG + "/" + dbName + "/"
					+ tableName + "?" + FORMAT_PARAM + "=" + XML_FORMAT + "\">"
					+ XML_FORMAT + "</a>\n";
			// link for json format
			result += "<a href=\"/" + SQLITE_MODULE_TAG + "/" + dbName + "/"
					+ tableName + "?" + FORMAT_PARAM + "=" + JSON_FORMAT
					+ "\">" + JSON_FORMAT + "</a>"
					+ CardetoConstants.HTML_RETURN + "\n";
		}
		return result;
	}

	private StringBuilder displayTableContent(
			GenericSQLiteDatabaseHelper genericSQLiteDatabaseHelper,
			String tableName, String format, boolean buildHeader) {
		StringBuilder result = new StringBuilder();

		TableOutputRenderer renderer = null;
		if (format.equals(CSV_FORMAT)) {
			renderer = new CSVRenderer();
		} else if (format.equals(JSON_FORMAT)) {
			renderer = new JsonRenderer();
		} else if (format.equals(XML_FORMAT)) {
			renderer = new XmlRenderer();
		} else {
			// HTML format
			renderer = new HtmlRenderer(mContext);
		}
		if (tableName.equals(ALL_TABLES)) {
			// case where we display all tables
			List<String> tables = genericSQLiteDatabaseHelper.getTablesList();
			if (buildHeader) {
				renderer.renderHeader(result, tables);
			}
			for (String tableName2 : tables) {
				result.append(displayTableContent(genericSQLiteDatabaseHelper,
						tableName2, format, false));
			}
			if (buildHeader) {
				renderer.renderFooter(result);
			}
		} else {
			// case where we display one table
			if (buildHeader) {
				renderer.renderHeader(result, null);
			}
			List<String> columns = genericSQLiteDatabaseHelper
					.getColumnNames(tableName);
			List<List<String>> rows = genericSQLiteDatabaseHelper
					.getRows(tableName);
			renderer.renderTable(result, tableName, columns, rows);
			if (buildHeader) {
				renderer.renderFooter(result);
			}
		}

		return result;
	}
}
