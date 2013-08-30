package cn.bluejoe.elfinder.controller;

import java.io.IOException;

import io.core9.elfinder.controller.Core9ConnectorController;




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
		core9ConnectorController.privateConnector(request, response, _commandExecutorFactory, _fsServiceFactory);
	}

}