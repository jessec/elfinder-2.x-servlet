package io.core9.elfinder.controller;



import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;


import cn.bluejoe.elfinder.controller.FsException;
import cn.bluejoe.elfinder.controller.executor.CommandExecutionContext;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutorFactory;
import cn.bluejoe.elfinder.service.FsServiceFactory;

public class Core9ConnectorController
{
	public void privateConnector(RequestDto request,
			final HttpServletResponse response, CommandExecutorFactory _commandExecutorFactory, final FsServiceFactory _fsServiceFactory) throws FsException {


		String cmd = request.getParameter("cmd");
		CommandExecutor ce = _commandExecutorFactory.get(cmd);

		if (ce == null)
		{
			throw new FsException(String.format("unknown command: %s", cmd));
		}

		try
		{
			final RequestDto finalRequest = request;
			ce.execute(new CommandExecutionContext()
			{

				@Override
				public FsServiceFactory getFsServiceFactory()
				{
					return _fsServiceFactory;
				}

				@Override
				public RequestDto getRequest()
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
	

}