package cn.bluejoe.elfinder.impl;

import io.core9.elfinder.controller.RequestDto;

import javax.servlet.ServletContext;

import cn.bluejoe.elfinder.service.FsService;
import cn.bluejoe.elfinder.service.FsServiceFactory;

public class StaticFsServiceFactory implements FsServiceFactory
{
	FsService _fsService;

	@Override
	public FsService getFileService(RequestDto request, ServletContext servletContext)
	{
		return _fsService;
	}

	public FsService getFsService()
	{
		return _fsService;
	}

	public void setFsService(FsService fsService)
	{
		_fsService = fsService;
	}
}
