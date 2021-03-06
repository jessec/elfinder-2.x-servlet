package cn.bluejoe.elfinder.controller.executors;

import io.core9.elfinder.controller.RequestDto;
import io.core9.elfinder.controller.ResponseDto;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Calendar;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.time.DateUtils;
import org.json.JSONObject;

import cn.bluejoe.elfinder.controller.executor.AbstractCommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.FsItemEx;
import cn.bluejoe.elfinder.service.FsService;

import com.mortennobel.imagescaling.DimensionConstrain;
import com.mortennobel.imagescaling.ResampleOp;

public class TmbCommandExecutor extends AbstractCommandExecutor implements CommandExecutor
{
	@SuppressWarnings({ "unused", "deprecation" })
	@Override
	public JSONObject execute(FsService fsService, RequestDto request, ResponseDto response) throws Exception
	{
		String target = request.getParameter("target");
		FsItemEx fsi = super.findItem(fsService, target);
		InputStream is = fsi.openInputStream();
		BufferedImage image = ImageIO.read(is);
		int width = fsService.getServiceConfig().getTmbWidth();
		ResampleOp rop = new ResampleOp(DimensionConstrain.createMaxDimension(width, -1));
		rop.setNumberOfThreads(4);
		BufferedImage b = rop.filter(image, null);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(b, "png", baos);
		byte[] bytesOut = baos.toByteArray();
		is.close();

		response.setHeader("Last-Modified", DateUtils.addDays(Calendar.getInstance().getTime(), 2 * 360).toGMTString());
		response.setHeader("Expires", DateUtils.addDays(Calendar.getInstance().getTime(), 2 * 360).toGMTString());

		ImageIO.write(b, "png", response.getOutputStream());
		
		
		JSONObject json = new JSONObject();
		return json;
	}
}
