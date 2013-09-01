package io.core9.test.upload;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
	public void test() {
		fail("Not yet implemented");
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
