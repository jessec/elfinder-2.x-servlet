package cn.bluejoe.elfinder.service;

import io.core9.elfinder.controller.RequestDto;

import javax.servlet.ServletContext;

public interface FsServiceFactory
{

	FsService getFileService(RequestDto requestDto, ServletContext servletContext);

}
