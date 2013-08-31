package cn.bluejoe.elfinder.controller.executor;

import io.core9.elfinder.controller.RequestDto;
import io.core9.elfinder.controller.ResponseDto;

import javax.servlet.ServletContext;

import cn.bluejoe.elfinder.service.FsServiceFactory;

public interface CommandExecutionContext
{
	FsServiceFactory getFsServiceFactory();

	RequestDto getRequest();

	ResponseDto getResponse();

	ServletContext getServletContext();
}
