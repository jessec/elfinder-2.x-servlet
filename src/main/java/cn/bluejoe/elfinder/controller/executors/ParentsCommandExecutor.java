package cn.bluejoe.elfinder.controller.executors;

import io.core9.elfinder.controller.RequestDto;

import java.util.HashMap;
import java.util.Map;


import org.json.JSONObject;

import cn.bluejoe.elfinder.controller.executor.AbstractJsonCommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.FsItemEx;
import cn.bluejoe.elfinder.service.FsService;

public class ParentsCommandExecutor extends AbstractJsonCommandExecutor implements CommandExecutor
{
	@Override
	public void execute(FsService fsService, RequestDto request, JSONObject json)
			throws Exception
	{
		String target = request.getParameter("target");

		Map<String, FsItemEx> files = new HashMap<String, FsItemEx>();
		FsItemEx fsi = findItem(fsService, target);
		while (!fsi.isRoot())
		{
			super.addSubfolders(files, fsi);
			fsi = fsi.getParent();
		}

		String requestUrl = request.getRequestURL().toString();
		json.put("tree", files2JsonArray(requestUrl, files.values()));
	}
}
