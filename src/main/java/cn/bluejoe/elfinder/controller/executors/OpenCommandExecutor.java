package cn.bluejoe.elfinder.controller.executors;

import io.core9.elfinder.controller.RequestDto;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.json.JSONObject;

import cn.bluejoe.elfinder.controller.executor.AbstractJsonCommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.FsItemEx;
import cn.bluejoe.elfinder.service.FsService;
import cn.bluejoe.elfinder.service.FsVolume;

public class OpenCommandExecutor extends AbstractJsonCommandExecutor implements CommandExecutor
{
	@Override
	public void execute(FsService fsService, RequestDto request, ServletContext servletContext, JSONObject json)
			throws Exception
	{
		boolean init = request.getParameter("init") != null;
		boolean tree = request.getParameter("tree") != null;
		String target = request.getParameter("target");

		Map<String, FsItemEx> files = new LinkedHashMap<String, FsItemEx>();
		if (init)
		{
			json.put("api", 2.1);
			json.put("netDrivers", new Object[0]);
		}

		if (tree)
		{
			for (FsVolume v : fsService.getVolumes())
			{
				FsItemEx root = new FsItemEx(v.getRoot(), fsService);
				files.put(root.getHash(), root);
				addSubfolders(files, root);
			}
		}

		FsItemEx cwd = findCwd(fsService, target);
		files.put(cwd.getHash(), cwd);
		addChildren(files, cwd);
		
		String requestUrl = request.getRequestURL().toString();

		json.put("files", files2JsonArray(requestUrl, files.values()));
		json.put("cwd", getFsItemInfo(requestUrl, cwd));
		json.put("options", getOptions(cwd));
	}
}
