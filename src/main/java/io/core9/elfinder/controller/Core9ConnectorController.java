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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;

import cn.bluejoe.elfinder.controller.FsException;
import cn.bluejoe.elfinder.controller.executor.CommandExecutionContext;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutorFactory;
import cn.bluejoe.elfinder.service.FsServiceFactory;

public class Core9ConnectorController
{
	public void privateConnector(HttpServletRequest request,
			final HttpServletResponse response, CommandExecutorFactory _commandExecutorFactory, final FsServiceFactory _fsServiceFactory) throws IOException, FsException {
		try
		{
			request = parseMultipartContent(request);
		}
		catch (Exception e)
		{
			throw new IOException(e.getMessage());
		}

		String cmd = request.getParameter("cmd");
		CommandExecutor ce = _commandExecutorFactory.get(cmd);

		if (ce == null)
		{
			throw new FsException(String.format("unknown command: %s", cmd));
		}

		try
		{
			final HttpServletRequest finalRequest = request;
			ce.execute(new CommandExecutionContext()
			{

				@Override
				public FsServiceFactory getFsServiceFactory()
				{
					return _fsServiceFactory;
				}

				@Override
				public HttpServletRequest getRequest()
				{
					return finalRequest;
				}

				@Override
				public HttpServletResponse getResponse()
				{
					return response;
				}

				@Override
				public ServletContext getServletContext()
				{
					return finalRequest.getSession().getServletContext();
				}
			});
		}
		catch (Exception e)
		{
			throw new FsException("unknown error", e);
		}
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