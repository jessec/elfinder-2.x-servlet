package cn.bluejoe.elfinder.service;

import io.core9.elfinder.controller.RequestDto;


public interface FsServiceFactory
{

	FsService getFileService(RequestDto requestDto);

}
