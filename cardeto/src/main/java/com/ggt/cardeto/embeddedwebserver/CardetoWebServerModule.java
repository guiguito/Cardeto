package com.ggt.cardeto.embeddedwebserver;

import java.util.Properties;

/**
 * Interface to implement to become a cardeto web server module which can handle
 * a specific http request.
 * 
 * @author guiguito
 * 
 */
public interface CardetoWebServerModule {

	/**
	 * Get module title.
	 * 
	 * @return
	 */
	public String getModuleTitle();

	/**
	 * Get module description.
	 * 
	 * @return
	 */
	public String getDescription();

	/**
	 * Get module URL.
	 * 
	 * @return
	 */
	public String getUrl();

	/**
	 * Tells whether the module can handle the request.
	 * 
	 * @param uri
	 * @return
	 */
	public boolean matchURI(String uri);

	/**
	 * Calls this method to make the module handle the request.
	 * 
	 * @param uri
	 * @param method
	 * @param header
	 * @param params
	 * @param files
	 * @return
	 */
	public StringBuilder handleRequest(String uri, String method,
			Properties header, Properties params, Properties files);

	// TODO add a method to check if permissions are available to enable this
	// module

}
