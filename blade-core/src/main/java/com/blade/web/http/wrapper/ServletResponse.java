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
package com.blade.web.http.wrapper;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import blade.kit.Assert;
import blade.kit.json.JsonObject;
import blade.kit.log.Logger;

import com.blade.Blade;
import com.blade.context.BladeWebContext;
import com.blade.render.ModelAndView;
import com.blade.render.Render;
import com.blade.web.http.HttpException;
import com.blade.web.http.HttpStatus;
import com.blade.web.http.Path;
import com.blade.web.http.Request;
import com.blade.web.http.Response;

/**
 * ServletResponse
 *
 * @author	<a href="mailto:biezhi.me@gmail.com" target="_blank">biezhi</a>
 * @since	1.0
 */
public class ServletResponse implements Response {
	
	private HttpServletResponse response;
	
	private boolean written = false;
	
	private Render render;
			
	public ServletResponse(HttpServletResponse response, Render render) {
		this.response = response;
		this.render = render;
	}
	
	@Override
	public HttpServletResponse raw() {
		return response;
	}

	@Override
	public int status() {
		return response.getStatus();
	}

	@Override
	public Response status(int status) {
		response.setStatus(status);
		return this;
	}

	@Override
	public Response badRequest() {
		response.setStatus(HttpStatus.BAD_REQUEST);
		return this;
	}

	@Override
	public Response unauthorized() {
		response.setStatus(HttpStatus.UNAUTHORIZED);
		return this;
	}

	@Override
	public Response notFound() {
		response.setStatus(HttpStatus.NOT_FOUND);
		return this;
	}

	@Override
	public Response conflict() {
		response.setStatus(HttpStatus.CONFLICT);
		return this;
	}

	@Override
	public String contentType() {
		return response.getContentType();
	}

	@Override
	public Response contentType(String contentType) {
		response.setContentType(contentType);
		return this;
	}

	@Override
	public String header(String name) {
		return response.getHeader(name);
	}

	@Override
	public Response header(String name, String value) {
		response.setHeader(name, value);
		return this;
	}

	@Override
	public Response cookie(Cookie cookie) {
		response.addCookie(cookie);
		return this;
	}

	@Override
	public Response cookie(String name, String value) {
		return cookie(name, value);
	}

	@Override
	public Response cookie(String name, String value, int maxAge) {
		return cookie(name, value, maxAge, false);
	}

	@Override
	public Response cookie(String name, String value, int maxAge, boolean secured) {
		return cookie(null, name, value, maxAge, secured);
	}

	@Override
	public Response cookie(String path, String name, String value, int maxAge, boolean secured) {
		Cookie cookie = new Cookie(name, value);
		if(null != path){
			cookie.setPath(path);
		}
        cookie.setMaxAge(maxAge);
        cookie.setSecure(secured);
        response.addCookie(cookie);
		return this;
	}
	
	@Override
	public Response removeCookie(Cookie cookie) {
        cookie.setMaxAge(0);
        response.addCookie(map(cookie));
		return this;
	}

	javax.servlet.http.Cookie map(Cookie cookie) {
		javax.servlet.http.Cookie servletCookie = new javax.servlet.http.Cookie(cookie.getName(), cookie.getValue());
		servletCookie.setMaxAge(cookie.getMaxAge());
		if (cookie.getPath() != null) {
			servletCookie.setPath(cookie.getPath());
		}
		if (cookie.getDomain() != null) {
			servletCookie.setDomain(cookie.getDomain());
		}
		servletCookie.setHttpOnly(cookie.isHttpOnly());
        servletCookie.setSecure(cookie.getSecure());
		return servletCookie;
	}
	
	@Override
	public Response removeCookie(String name) {
		Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
		return this;
	}

	@Override
	public Response favicon(JsonObject favicon){
		
		int maxAgeDef = 60 * 60 * 24 * 365 * 1000; // 1 year
		String path = favicon.getString("path");
		int maxAge = favicon.getInt("maxAge", maxAgeDef);
		
		try{
			File file = new File(path);
			if(file.exists() && !file.isDirectory()){
				
				response.setHeader("Content-Type", "image/x-icon");
				response.setHeader("Cache-Control", "public, max-age=" + Math.floor(maxAge/1000));
		        response.setHeader("Content-Length", String.valueOf(file.length()));
//		        response.setHeader("Content-Disposition", "inline; filename=\"" + file.getName() + "\"");
		        Files.copy(file.toPath(), response.getOutputStream());
			}else{
				response.sendError(HttpStatus.NOT_FOUND);
			}
			return this;
		}
		catch (IOException e){
			throw new HttpException(e);
		}
	}

	@Override
	public Response text(String text) {
		try {
			response.setHeader("Cache-Control", "no-cache");
    		response.setContentType("text/plain;charset=utf-8");
			response.getWriter().print(text);
			this.written = true;
			return this;
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public Response html(String html) {
		try {
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("text/html;charset=utf-8");
			
			PrintWriter out = response.getWriter();
			out.print(html);
    		out.flush();
    		out.close();
    		
    		this.written = true;
			return this;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public Response json(String json) {
		Request request = BladeWebContext.request();
		String userAgent = request.userAgent();
		if (userAgent.contains("MSIE")) {
			response.setContentType("text/html;charset=utf-8");
		} else {
			response.setContentType("application/json;charset=utf-8");
		}
		try {
			response.setHeader("Cache-Control", "no-cache");
			PrintWriter out = response.getWriter();
			out.print(json);
    		out.flush();
    		out.close();
    		
    		this.written = true;
			return this;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Response xml(String xml) {
		try {
			response.setHeader("Cache-Control", "no-cache");
			response.setContentType("text/xml;charset=utf-8");
			
			PrintWriter out = response.getWriter();
			out.print(xml);
    		out.flush();
    		out.close();
    		
    		this.written = true;
			return this;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public ServletOutputStream outputStream() throws IOException {
		return response.getOutputStream();
	}

	@Override
	public Response render(String view) {
		try {
			Assert.notBlank(view, "view not is null");
			
			Blade blade = Blade.me();
			String viewPath = blade.viewPrefix() + view + blade.viewSuffix();
			if(view.endsWith(blade.viewSuffix())){
				viewPath = blade.viewPrefix() + view;
			}
			viewPath = Path.cleanPath(viewPath);
			ModelAndView modelAndView = new ModelAndView(viewPath);
			
			render.render(modelAndView, response.getWriter());
			return this;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Response render(ModelAndView modelAndView) {
		try {
			Assert.notNull(modelAndView, "ModelAndView not is null!");
			Assert.notBlank(modelAndView.getView(), "view not is null");
			
			Blade blade = Blade.me();
			String viewPath = blade.viewPrefix() + modelAndView.getView() + blade.viewSuffix();
			if(modelAndView.getView().endsWith(blade.viewSuffix())){
				viewPath = blade.viewPrefix() + modelAndView.getView();
			}
			viewPath = Path.cleanPath(viewPath);
			modelAndView.setView(viewPath);
			
			render.render(modelAndView, response.getWriter());
			return this;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void redirect(String path) {
		try {
			response.sendRedirect(path);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}
	
	@Override
	public void go(String path) {
		try {
			String ctx = BladeWebContext.servletContext().getContextPath();
        	String location = Path.fixPath(ctx + path);
			response.sendRedirect(location);
		} catch (IOException e) {
			throw new HttpException(e);
		}
	}

	@Override
	public boolean isWritten() {
		return written;
	}

}
