package io.core9.elfinder.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

public class RequestDto {
	
	private HttpServletRequest request;

	public void setRequest(HttpServletRequest request) throws IOException{
		this.request = request;
		try
		{
			request = parseMultipartContent(request);
		}
		catch (Exception e)
		{
			throw new IOException(e.getMessage());
		}
	}

	public String getMethod() {
		return request.getMethod();
	}

	public String getParameter(String key) {
		return request.getParameter(key);
	}

	public HttpSession getSession() {
		return request.getSession();
	}

	public StringBuffer getRequestURL() {
		return request.getRequestURL();
	}
	
	public String[] getParameterValues(String key) {
		return request.getParameterValues(key);
	}
	
	@SuppressWarnings("unchecked")
	public List<FileItemStream> getAttribute(String name) {
		return (List<FileItemStream>) request.getAttribute(name);
	}

	
	private HttpServletRequest parseMultipartContent(final HttpServletRequest request) throws Exception
	{
		if (!ServletFileUpload.isMultipartContent(request))
			return request;

		final Map<String, String> requestParams = new HashMap<String, String>();
		List<FileItemStream> listFiles = new ArrayList<FileItemStream>();

		// Parse the request
		ServletFileUpload upload = new ServletFileUpload();
		FileItemIterator iter = upload.getItemIterator(request);
		while (iter.hasNext())
		{
			final FileItemStream item = iter.next();
			String name = item.getFieldName();
			InputStream stream = item.openStream();
			if (item.isFormField())
			{
				requestParams.put(name, Streams.asString(stream));
			}
			else
			{
				String fileName = item.getName();
				if (fileName != null && !"".equals(fileName.trim()))
				{
					ByteArrayOutputStream os = new ByteArrayOutputStream();
					IOUtils.copy(stream, os);
					final byte[] bs = os.toByteArray();
					stream.close();

					listFiles.add((FileItemStream) Proxy.newProxyInstance(this.getClass().getClassLoader(),
						new Class[] { FileItemStream.class }, new InvocationHandler()
						{
							@Override
							public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
							{
								if ("openStream".equals(method.getName()))
								{
									return new ByteArrayInputStream(bs);
								}

								return method.invoke(item, args);
							}
						}));
				}
			}
		}

		request.setAttribute(FileItemStream.class.getName(), listFiles);
		return (HttpServletRequest) Proxy.newProxyInstance(this.getClass().getClassLoader(),
			new Class[] { HttpServletRequest.class }, new InvocationHandler()
			{
				@Override
				public Object invoke(Object arg0, Method arg1, Object[] arg2) throws Throwable
				{
					if ("getParameter".equals(arg1.getName()))
					{
						return requestParams.get(arg2[0]);
					}

					return arg1.invoke(request, arg2);
				}
			});
	}




}