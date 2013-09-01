package io.core9.test.upload;

import io.core9.elfinder.controller.Core9ConnectorController;
import io.core9.elfinder.controller.RequestDto;
import io.core9.elfinder.controller.ResponseDto;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;

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
import static org.mockito.Mockito.*;

public class UploadTest {

	private Core9ConnectorController core9ConnectorController = new Core9ConnectorController();
	

	
	@Test
	public void test() throws IOException {
		
		HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class);  
		
        when(request.getParameter("username")).thenReturn("me");
        when(request.getParameter("password")).thenReturn("secret");
        PrintWriter writer = new PrintWriter("somefile.txt");
        when(response.getWriter()).thenReturn(writer);
        
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
