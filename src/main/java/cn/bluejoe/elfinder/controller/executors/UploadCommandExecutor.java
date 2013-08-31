package cn.bluejoe.elfinder.controller.executors;

import io.core9.elfinder.controller.RequestDto;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import cn.bluejoe.elfinder.controller.executor.AbstractJsonCommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.FsItemEx;
import cn.bluejoe.elfinder.service.FsService;

public class UploadCommandExecutor extends AbstractJsonCommandExecutor implements CommandExecutor
{

	@Override
	public void execute(FsService fsService, RequestDto request, JSONObject json)
			throws Exception
	{
		List<FileItemStream> listFiles = (List<FileItemStream>) request.getAttribute(FileItemStream.class.getName());
		List<FsItemEx> added = new ArrayList<FsItemEx>();

		String target = request.getParameter("target");
		FsItemEx dir = super.findItem(fsService, target);

		for (FileItemStream fis : listFiles)
		{
			String fileName = fis.getName();
			FsItemEx newFile = new FsItemEx(dir, fileName);
			newFile.createFile();
			InputStream is = fis.openStream();
			OutputStream os = newFile.openOutputStream();

			IOUtils.copy(is, os);
			os.close();
			is.close();

			added.add(newFile);
		}

		String requestUrl = request.getRequestURL().toString();
		json.put("added", files2JsonArray(requestUrl, added));
	}
}
