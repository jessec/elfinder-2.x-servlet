package cn.bluejoe.elfinder.controller.executors;

import io.core9.elfinder.controller.RequestDto;

import javax.servlet.ServletContext;

import org.json.JSONObject;

import cn.bluejoe.elfinder.controller.executor.AbstractJsonCommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.FsItemEx;
import cn.bluejoe.elfinder.service.FsService;

public class MkdirCommandExecutor extends AbstractJsonCommandExecutor implements CommandExecutor
{
	@Override
	public void execute(FsService fsService, RequestDto request, ServletContext servletContext, JSONObject json)
			throws Exception
	{
		String target = request.getParameter("target");
		String name = request.getParameter("name");

		FsItemEx fsi = super.findItem(fsService, target);
		FsItemEx dir = new FsItemEx(fsi, name);
		dir.createFolder();

		String requestUrl = request.getRequestURL().toString();
		json.put("added", new Object[] { getFsItemInfo(requestUrl, dir) });
	}
}
