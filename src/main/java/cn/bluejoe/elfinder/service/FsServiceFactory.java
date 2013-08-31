package cn.bluejoe.elfinder.service;

import io.core9.elfinder.controller.RequestDto;
import io.core9.elfinder.controller.ServerContext;


public interface FsServiceFactory
{

	FsService getFileService(RequestDto requestDto, ServerContext servletContext);

}
