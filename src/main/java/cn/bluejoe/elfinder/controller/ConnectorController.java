package cn.bluejoe.elfinder.controller;

import java.io.IOException;
import java.lang.reflect.Proxy;

import io.core9.elfinder.controller.Core9ConnectorController;





import io.core9.elfinder.controller.TestInvocationHandler;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;





import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;





import cn.bluejoe.elfinder.controller.executor.CommandExecutorFactory;
import cn.bluejoe.elfinder.service.FsServiceFactory;

@Controller
@RequestMapping("connector")
public class ConnectorController
{
	@Resource(name = "commandExecutorFactory")
	private CommandExecutorFactory _commandExecutorFactory;

	@Resource(name = "fsServiceFactory")
	private FsServiceFactory _fsServiceFactory;
	
	private Core9ConnectorController core9ConnectorController = new Core9ConnectorController();

	@RequestMapping
	public void connector(HttpServletRequest request, final HttpServletResponse response) throws IOException
	{
		HttpServletRequest newRequest = makeHttpServletRequestProxy(request);
		HttpServletResponse newResponse = makeHttpServletResponseProxy(response);
		
		core9ConnectorController.privateConnector(newRequest, newResponse, _commandExecutorFactory, _fsServiceFactory);
	}

	private HttpServletResponse makeHttpServletResponseProxy(
			HttpServletResponse response) {
		HttpServletResponse t = (HttpServletResponse) Proxy.newProxyInstance(
				HttpServletResponse.class.getClassLoader(), new Class<?>[] { HttpServletResponse.class },
				new TestInvocationHandler(response));
		
		return t;
	}

	private HttpServletRequest makeHttpServletRequestProxy(HttpServletRequest request) {

		HttpServletRequest t = (HttpServletRequest) Proxy.newProxyInstance(
				HttpServletRequest.class.getClassLoader(), new Class<?>[] { HttpServletRequest.class },
				new TestInvocationHandler(request));
		
		return t;
	}

}