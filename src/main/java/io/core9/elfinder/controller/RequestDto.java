package io.core9.elfinder.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

public class RequestDto {

	private HttpServletRequest request;

	private HashMap<String, String> param = new HashMap<String, String>();
	private Map<String, String[]> paramValues = new HashMap<>();

	private StringBuffer requestUrl;

	public void setRequest(HttpServletRequest request) throws IOException {
		this.request = request;

		getParams();
		getParamValues();
		extractRequestURL();

		try {
			parseMultipartContent(request);
		} catch (Exception e) {
			throw new IOException(e.getMessage());
		}
	}

	public String getMethod() {
		return request.getMethod();
	}

	public String getParameter(String key) {
		return param.get(key);
	}

	public String[] getParameterValues(String key) {
		return paramValues.get(key);
	}

	public StringBuffer getRequestURL() {
		return requestUrl;
	}

	public void setRequestURL(StringBuffer requestUrl){
		this.requestUrl = requestUrl;
	}
	
	@SuppressWarnings("unchecked")
	public List<FileItemStream> getAttribute(String name) {
		return (List<FileItemStream>) request.getAttribute(name);
	}
	
	private void extractRequestURL(){
		requestUrl = request.getRequestURL();
	}

	@SuppressWarnings("unchecked")
	private void getParams() {

		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			String paramValues = request.getParameter(paramName);
			param.put(paramName, paramValues);
		}

	}

	private void getParamValues() {

		@SuppressWarnings("unchecked")
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			String[] values = request.getParameterValues(paramName);
			paramValues.put(paramName, values);
		}
	}

	private void parseMultipartContent(final HttpServletRequest request)
			throws Exception {
		if (!ServletFileUpload.isMultipartContent(request))
			return;

		final Map<String, String> requestParams = new HashMap<String, String>();
		List<FileItemStream> listFiles = new ArrayList<FileItemStream>();

		// Parse the request
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(request);
		while (iter.hasNext()) {
			final FileItemStream item = iter.next();
			String name = item.getFieldName();
			InputStream stream = item.openStream();
			if (item.isFormField()) {
				requestParams.put(name, Streams.asString(stream));
			} else {
				String fileName = item.getName();
				if (fileName != null && !"".equals(fileName.trim())) {
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					IOUtils.copy(stream, os);
					final byte[] bs = os.toByteArray();
					stream.close();

					listFiles.add((FileItemStream) Proxy.newProxyInstance(this
							.getClass().getClassLoader(),
							new Class[] { FileItemStream.class },
							new InvocationHandler() {
								@Override
								public Object invoke(Object proxy,
										Method method, Object[] args)
										throws Throwable {
									if ("openStream".equals(method.getName())) {
										return new ByteArrayInputStream(bs);
									}

									return method.invoke(item, args);
								}
							}));
				}
			}
		}

		param.putAll(requestParams);

		request.setAttribute(FileItemStream.class.getName(), listFiles);
	}

	public void setParam(HashMap<String, String> param) {
		this.param = param;
	}

	public void setParamValues(Map<String, String[]> paramValues) {
		this.paramValues = paramValues;
	}

}
