/**
 * Copyright (c) 2015, biezhi 王爵 (biezhi.me@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.blade.loader;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import blade.kit.CollectionKit;
import blade.kit.PatternKit;
import blade.kit.json.JsonObject;

/**
 * Blade Config Class
 * 
 * @author <a href="mailto:biezhi.me@gmail.com" target="_blank">biezhi</a>
 * @since 1.0
 *
 */
public class Config {

	// Store all variables 
	private Map<String, String> configMap = CollectionKit.newHashMap();
	
	// Storage of all routing packets 
	private List<String> routePackages = CollectionKit.newArrayList();
	
	// Store all IOC packages 
	private List<String> iocPackages = CollectionKit.newArrayList();
	
	// Store all filter directories 
	private List<String> staticFolders = CollectionKit.newArrayList();
	
	// Base package
	private String basePackage;
	
	// Interceptor package
	private String interceptorPackage;
	
	// Encoding
	private String encoding = "utf-8";
	
	// View prefix
	private String viewPrefix = "/WEB-INF/";
	
	// View suffix
	private String viewSuffix = ".jsp";
	
	// web root path
	private String webRoot;
	
	//Favicon
	private JsonObject favicon = new JsonObject();
	
	// 404 view page
	private String view404;
	
	// 500 view page
	private String view500;
	
	// Is debug mode
	private boolean isDebug = true;
	
	// Enabled XSS
	private boolean enableXSS = false;
	
	public Config() {
	}
	
	public Map<String, String> getConfigMap() {
		return configMap;
	}

	public void setConfigMap(Map<String, String> configMap) {
		this.configMap = configMap;
	}
	
	public String get(String key){
		return configMap.get(key);
	}
	
	public Integer getAsInt(String key){
		String val = get(key);
		if(null != val && PatternKit.isNumber(val)){
			return Integer.valueOf(val);
		}
		return null;
	}
	
	public Long getAsLong(String key){
		String val = get(key);
		if(null != val && PatternKit.isNumber(val)){
			return Long.valueOf(val);
		}
		return null;
	}
	
	public Boolean getAsBoolean(String key){
		String val = get(key);
		if(null != val){
			return Boolean.valueOf(val);
		}
		return null;
	}
	
	public Double getAsDouble(String key){
		String val = get(key);
		if(null != val){
			return Double.valueOf(val);
		}
		return null;
	}
	
	public Float getAsFloat(String key){
		String val = get(key);
		if(null != val){
			return Float.valueOf(val);
		}
		return null;
	}

	public String[] getRoutePackages() {
		String[] routeArr = new String[routePackages.size()];
		return routePackages.toArray(routeArr);
	}
	
	public void setRoutePackages(String ... packages) {
		routePackages.addAll(Arrays.asList(packages));
	}
	
	public String getBasePackage() {
		return basePackage;
	}

	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
	}

	public String[] getIocPackages() {
		String[] iocArr = new String[iocPackages.size()];
		return iocPackages.toArray(iocArr);
	}

	public void setIocPackages(String ... packages) {
		iocPackages.addAll(Arrays.asList(packages));
	}

	public String getInterceptorPackage() {
		return interceptorPackage;
	}

	public void setInterceptorPackage(String interceptorPackage) {
		this.interceptorPackage = interceptorPackage;
	}

	public String getViewPrefix() {
		return viewPrefix;
	}

	public void setViewPrefix(String viewPrefix) {
		this.viewPrefix = viewPrefix;
	}

	public String getViewSuffix() {
		return viewSuffix;
	}


	public void setViewSuffix(String viewSuffix) {
		this.viewSuffix = viewSuffix;
	}

	public String[] getStaticFolders() {
		String[] folderArr = new String[staticFolders.size()];
		return staticFolders.toArray(folderArr);
	}
	
	public void setStaticFolders(String ... packages) {
		staticFolders.addAll(Arrays.asList(packages));
	}

	/**
	 * @return the favicon JsonObject
	 */
	public JsonObject getFavicon() {
		return favicon;
	}

	/**
	 * @param path   favicon path to set
	 */
	public void setFavicon(String path) {
		this.favicon.add("path",path);
	}
	
	/**
	 * @param path   favicon path to set
	 * @param maxAge  cache maxAge
	 */
	public void setFavicon(String path, int maxAge) {
		this.favicon.add("path",path);
		this.favicon.add("maxAge", maxAge);
	}

	public String getView404() {
		return view404;
	}

	public void setView404(String view404) {
		this.view404 = view404;
	}

	public String getView500() {
		return view500;
	}

	public void setView500(String view500) {
		this.view500 = view500;
	}

	public String getWebRoot() {
		return webRoot;
	}

	public void setWebRoot(String webRoot) {
		this.webRoot = webRoot;
	}

	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public boolean isEnableXSS() {
		return enableXSS;
	}

	public void setEnableXSS(boolean enableXSS) {
		this.enableXSS = enableXSS;
	}

}
