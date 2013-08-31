package cn.bluejoe.elfinder.controller.executor;

import io.core9.elfinder.controller.RequestDto;
import io.core9.elfinder.controller.ResponseDto;

import java.io.PrintWriter;


import org.json.JSONObject;

import cn.bluejoe.elfinder.service.FsService;

public abstract class AbstractJsonCommandExecutor extends AbstractCommandExecutor
{
	@Override
	final public void execute(FsService fsService, RequestDto request, ResponseDto response) throws Exception
	{
		JSONObject json = new JSONObject();
		try
		{
			execute(fsService, request, json);
			response.setContentType("application/json; charset=UTF-8");

			PrintWriter writer = response.getWriter();
			json.write(writer);
			writer.flush();
			writer.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			json.put("error", e.getMessage());
		}
	}

	protected abstract void execute(FsService fsService, RequestDto request,
			JSONObject json) throws Exception;

}