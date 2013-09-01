package io.core9.test.upload;


import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.ByteArrayPartSource;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.util.FileCopyUtils;

public class Upload {

	@Test
	public void test() throws HttpException, IOException {
		
		
		
		File resourceUrl = new File("src\\test\\resources\\upload\\upload.jpg");
		
		assertTrue(resourceUrl.exists());
		
		String url = "http://localhost:8080/elfinder-2.x-servlet/elfinder-servlet/connector";
		uploadFile(resourceUrl, url);
	}
	
	
	
	public static String uploadFile(File resourceUrl,String url) throws HttpException, IOException{
		File f = resourceUrl;
		PostMethod filePost = new PostMethod(url);
		Part[] parts = {new FilePart(f.getName(), f)};
		filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
		HttpClient client = new HttpClient();
		@SuppressWarnings("unused")
		int status = client.executeMethod(filePost);
		String resultUUid=null;
		resultUUid = filePost.getResponseBodyAsString();
		filePost.releaseConnection();
		return resultUUid;
	}
	
	
	

	public void createMultipartFormDataRequest(MockHttpServletRequest request, String resourceName, String partName) throws IOException {
	    // Load resource being uploaded
	    byte[] fileContent = FileCopyUtils.copyToByteArray(
	        getClass().getResourceAsStream(resourceName));
	    // Create part & entity from resource
	    Part[] parts = new Part[] {
	        new FilePart(partName, new ByteArrayPartSource(resourceName, fileContent)) };
	    MultipartRequestEntity multipartRequestEntity =
	        new MultipartRequestEntity(parts, new PostMethod().getParams());
	    // Serialize request body
	    ByteArrayOutputStream requestContent = new ByteArrayOutputStream();
	    multipartRequestEntity.writeRequest(requestContent);
	    // Set request body to HTTP servlet request
	    request.setContent(requestContent.toByteArray());
	    // Set content type to HTTP servlet request (important, includes Mime boundary string)
	    request.setContentType(multipartRequestEntity.getContentType());
	}
	

}
