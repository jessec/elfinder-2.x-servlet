package cn.bluejoe.elfinder.controller.executors;

import io.core9.elfinder.controller.RequestDto;
import io.core9.elfinder.controller.ServerContext;

import java.util.ArrayList;
import java.util.List;


import org.json.JSONObject;

import cn.bluejoe.elfinder.controller.executor.AbstractJsonCommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.FsItemEx;
import cn.bluejoe.elfinder.service.FsService;

public class PasteCommandExecutor extends AbstractJsonCommandExecutor implements CommandExecutor
{
	@SuppressWarnings("unused")
	@Override
	public void execute(FsService fsService, RequestDto request, ServerContext servletContext, JSONObject json)
			throws Exception
	{
		String[] targets = request.getParameterValues("targets[]");
		String src = request.getParameter("src");
		String dst = request.getParameter("dst");
		boolean cut = "1".equals(request.getParameter("cut"));

		List<FsItemEx> added = new ArrayList<FsItemEx>();
		List<FsItemEx> removed = new ArrayList<FsItemEx>();

		FsItemEx fsrc = super.findItem(fsService, src);
		FsItemEx fdst = super.findItem(fsService, dst);

		for (String target : targets)
		{
			FsItemEx ftgt = super.findItem(fsService, target);
			String name = ftgt.getName();
			FsItemEx newFile = new FsItemEx(fdst, name);
			super.createAndCopy(ftgt, newFile);
			added.add(newFile);

			if (cut)
			{
				ftgt.delete();
				removed.add(ftgt);
			}
		}

		String requestUrl = request.getRequestURL().toString();
		json.put("added", files2JsonArray(requestUrl, added));
		json.put("removed", files2JsonArray(requestUrl, removed));
	}
}
