package cn.bluejoe.elfinder.controller.executors;

import io.core9.elfinder.controller.RequestDto;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.json.JSONObject;

import cn.bluejoe.elfinder.controller.executor.AbstractJsonCommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.FsItemEx;
import cn.bluejoe.elfinder.service.FsService;

public class RmCommandExecutor extends AbstractJsonCommandExecutor implements CommandExecutor
{
	@SuppressWarnings("unused")
	@Override
	public void execute(FsService fsService, RequestDto request, ServletContext servletContext, JSONObject json)
			throws Exception
	{
		String[] targets = request.getParameterValues("targets[]");
		String current = request.getParameter("current");
		List<String> removed = new ArrayList<String>();

		for (String target : targets)
		{
			FsItemEx ftgt = super.findItem(fsService, target);
			ftgt.delete();
			removed.add(ftgt.getHash());
		}

		json.put("removed", removed.toArray());
	}
}
