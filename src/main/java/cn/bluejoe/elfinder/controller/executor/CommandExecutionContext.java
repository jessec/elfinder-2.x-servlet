package cn.bluejoe.elfinder.controller.executor;

import io.core9.elfinder.controller.RequestDto;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import cn.bluejoe.elfinder.service.FsServiceFactory;

public interface CommandExecutionContext
{
	FsServiceFactory getFsServiceFactory();

	RequestDto getRequest();

	HttpServletResponse getResponse();

	ServletContext getServletContext();
}
