package cn.bluejoe.elfinder.controller.executors;

import io.core9.elfinder.controller.RequestDto;


import org.json.JSONObject;

import cn.bluejoe.elfinder.controller.executor.AbstractJsonCommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.FsItemEx;
import cn.bluejoe.elfinder.service.FsService;

public class RenameCommandExecutor extends AbstractJsonCommandExecutor implements CommandExecutor
{
	@SuppressWarnings("unused")
	@Override
	public void execute(FsService fsService, RequestDto request, JSONObject json)
			throws Exception
	{
		String target = request.getParameter("target");
		String current = request.getParameter("current");
		String name = request.getParameter("name");

		FsItemEx fsi = super.findItem(fsService, target);
		FsItemEx dst = new FsItemEx(fsi.getParent(), name);
		fsi.renameTo(dst);

		String requestUrl = request.getRequestURL().toString();
		json.put("added", new Object[] { getFsItemInfo(requestUrl, dst) });
		json.put("removed", new String[] { target });
	}
}
