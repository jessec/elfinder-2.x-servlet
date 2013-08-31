package cn.bluejoe.elfinder.controller.executors;

import io.core9.elfinder.controller.RequestDto;
import io.core9.elfinder.controller.ServerContext;

import java.util.HashMap;
import java.util.Map;


import org.json.JSONObject;

import cn.bluejoe.elfinder.controller.executor.AbstractJsonCommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.FsItemEx;
import cn.bluejoe.elfinder.service.FsService;

public class LsCommandExecutor extends AbstractJsonCommandExecutor implements CommandExecutor
{
	@Override
	public void execute(FsService fsService, RequestDto request, ServerContext servletContext, JSONObject json)
			throws Exception
	{
		String target = request.getParameter("target");

		Map<String, FsItemEx> files = new HashMap<String, FsItemEx>();
		FsItemEx fsi = super.findItem(fsService, target);
		super.addChildren(files, fsi);

		String requestUrl = request.getRequestURL().toString();
		json.put("list", files2JsonArray(requestUrl, files.values()));
	}
}
