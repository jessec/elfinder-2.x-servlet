package cn.bluejoe.elfinder.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.core9.elfinder.controller.Core9ConnectorController;
import io.core9.elfinder.controller.RequestDto;
import io.core9.elfinder.controller.ResponseDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.bluejoe.elfinder.controller.executor.CommandExecutor;
import cn.bluejoe.elfinder.controller.executor.CommandExecutorFactory;
import cn.bluejoe.elfinder.controller.executor.DefaultCommandExecutorFactory;
import cn.bluejoe.elfinder.impl.DefaultFsService;
import cn.bluejoe.elfinder.impl.DefaultFsServiceConfig;
import cn.bluejoe.elfinder.impl.FsSecurityCheckFilterMapping;
import cn.bluejoe.elfinder.impl.FsSecurityCheckForAll;
import cn.bluejoe.elfinder.impl.FsSecurityCheckerChain;
import cn.bluejoe.elfinder.impl.StaticFsServiceFactory;
import cn.bluejoe.elfinder.localfs.LocalFsVolume;
import cn.bluejoe.elfinder.service.FsServiceConfig;
import cn.bluejoe.elfinder.service.FsServiceFactory;
import cn.bluejoe.elfinder.service.FsVolume;

@Controller
@RequestMapping("connector")
public class ConnectorController {
	/*
	 * @Resource(name = "commandExecutorFactory") private CommandExecutorFactory
	 * _commandExecutorFactory;
	 */

/*	@Resource(name = "fsServiceFactory")
	private FsServiceFactory _fsServiceFactory;*/

	private Core9ConnectorController core9ConnectorController = new Core9ConnectorController();

	@RequestMapping
	public void connector(HttpServletRequest request,
			final HttpServletResponse response) throws IOException {

		RequestDto requestDto = makeRequestDto(request);
		ResponseDto responseDto = makeResponseDto(response);

		CommandExecutorFactory _commandExecutorFactory = new DefaultCommandExecutorFactory();
		_commandExecutorFactory
				.setClassNamePattern("cn.bluejoe.elfinder.controller.executors.%sCommandExecutor");
		Map<String, CommandExecutor> map = new HashMap<String, CommandExecutor>();
		_commandExecutorFactory.setMap(map);

		
		
		FsServiceFactory _fsServiceFactoryNew = new StaticFsServiceFactory();
		DefaultFsService fsService = new DefaultFsService();
		
		FsServiceConfig serviceConfig = new DefaultFsServiceConfig();
		serviceConfig.setTmbWidth(80);
		fsService.setServiceConfig(serviceConfig);
		
		
		FsVolume[] volumes = new FsVolume[2];
		LocalFsVolume vol1 = new LocalFsVolume();
		vol1.setName("MyFiles");
		vol1.setRootDir(new File("C:/temp/elfinder/my"));
		volumes[0] = vol1;
		LocalFsVolume vol2 = new LocalFsVolume();
		vol2.setName("Shared");
		vol2.setRootDir(new File("C:/temp/elfinder/share"));
		volumes[1] = vol2;
		fsService.setVolumes(volumes);
		
		
		FsSecurityCheckerChain securityChecker = new FsSecurityCheckerChain();
		List<FsSecurityCheckFilterMapping> filterMappings = new ArrayList<FsSecurityCheckFilterMapping>();
		
		FsSecurityCheckFilterMapping mapping1 = new FsSecurityCheckFilterMapping();
		String pattern = "A_.*";
		mapping1.setPattern(pattern);
		FsSecurityCheckForAll checker = new FsSecurityCheckForAll();
		boolean readable = true;
		checker.setReadable(readable );
		boolean writable = true;
		checker.setWritable(writable );
		mapping1.setChecker(checker);
		
		FsSecurityCheckFilterMapping mapping2 = new FsSecurityCheckFilterMapping();
		String pattern2 = "A_.*";
		mapping2.setPattern(pattern2);
		FsSecurityCheckForAll checker2 = new FsSecurityCheckForAll();
		boolean readable2 = true;
		checker2.setReadable(readable2 );
		boolean writable2 = true;
		checker2.setWritable(writable2 );
		mapping2.setChecker(checker2);
		
		filterMappings.add(mapping1);
		filterMappings.add(mapping2);
		
		securityChecker.setFilterMappings(filterMappings);
		fsService.setSecurityChecker(securityChecker);
		
		_fsServiceFactoryNew.setFsService(fsService);
		

		core9ConnectorController.privateConnector(requestDto, responseDto,
				_commandExecutorFactory, _fsServiceFactoryNew);
	}

	private RequestDto makeRequestDto(HttpServletRequest newRequest)
			throws IOException {

		RequestDto requestDto = new RequestDto();
		requestDto.setRequest(newRequest);

		return requestDto;
	}

	private ResponseDto makeResponseDto(HttpServletResponse newResponse)
			throws IOException {

		ResponseDto responseDto = new ResponseDto();
		responseDto.setResponse(newResponse);

		return responseDto;
	}

}