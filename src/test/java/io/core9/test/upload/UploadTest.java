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
	
	
	private HashMap<String, String> param = new HashMap<String, String>();
	private Map<String, String[]> paramValues = new HashMap<String, String[]>();

	
	@Test
	public void test() throws IOException {
		
		//{cmd=open, tree=1, target=B_, init=1, _=1378067380255}
		param.put("cmd", "open");
		param.put("tree", "1");
		param.put("target", "B_");
		param.put("init", "1");
		param.put("_", "1378067380255");
		
		//{cmd=[open], tree=[Ljava.lang.String;@2bd30166, target=[Ljava.lang.String;@2a00e604, init=[Ljava.lang.String;@20fd2627, _=[Ljava.lang.String;@4bd3db5c}
		String[] val1 = {"open"};
		paramValues.put("cmd", val1);
		String[] val2 = {"1"};
		paramValues.put("tree", val2);
		String[] val3 = {"B_"};
		paramValues.put("target", val3);
		String[] val4 = {"1378067380255"};
		paramValues.put("init", val4);
		

		RequestDto requestDto = new RequestDto();
		requestDto.setParam(param);
		requestDto.setParamValues(paramValues);
		StringBuffer requestUrl = new StringBuffer().append("http://localhost:8080/elfinder-2.x-servlet/elfinder-servlet/connector");
		requestDto.setRequestURL(requestUrl);
		
		
        HttpServletResponse response = mock(HttpServletResponse.class);  
        PrintWriter writer = new PrintWriter("somefile.txt");
        when(response.getWriter()).thenReturn(writer);
		ResponseDto responseDto = new ResponseDto();
		responseDto.setResponse(response);
        
        
        
		
/*
		HttpServletRequest request = mock(HttpServletRequest.class);       
        HttpServletResponse response = mock(HttpServletResponse.class);  
        when(request.getParameter("username")).thenReturn("me");
        when(request.getParameter("password")).thenReturn("secret");
        PrintWriter writer = new PrintWriter("somefile.txt");
        when(response.getWriter()).thenReturn(writer);
        
		RequestDto requestDto = makeRequestDto(request);
		ResponseDto responseDto = makeResponseDto(response);
*/
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
	
	@SuppressWarnings("unused")
	private RequestDto makeRequestDto(HttpServletRequest newRequest)
			throws IOException {

		RequestDto requestDto = new RequestDto();
		requestDto.setRequest(newRequest);

		return requestDto;
	}

	@SuppressWarnings("unused")
	private ResponseDto makeResponseDto(HttpServletResponse newResponse)
			throws IOException {

		ResponseDto responseDto = new ResponseDto();
		responseDto.setResponse(newResponse);

		return responseDto;
	}

	

}
