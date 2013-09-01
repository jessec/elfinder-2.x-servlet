package io.core9.test.upload;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

/**
 * @author Juergen Hoeller
 */
public class MockMultipartHttpServletRequestTests extends TestCase {

	public void testMockMultipartHttpServletRequestWithByteArray() throws IOException {
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		assertFalse(request.getFileNames().hasNext());
		assertNull(request.getFile("file1"));
		assertNull(request.getFile("file2"));
		assertTrue(request.getFileMap().isEmpty());

		request.addFile(new MockMultipartFile("file1", "myContent1".getBytes()));
		request.addFile(new MockMultipartFile("file2", "myOrigFilename", "text/plain", "myContent2".getBytes()));
		doTestMultipartHttpServletRequest(request);
	}

	public void testMockMultipartHttpServletRequestWithInputStream() throws IOException {
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		request.addFile(new MockMultipartFile("file1", new ByteArrayInputStream("myContent1".getBytes())));
		request.addFile(new MockMultipartFile("file2", "myOrigFilename", "text/plain", new ByteArrayInputStream("myContent2".getBytes())));
		doTestMultipartHttpServletRequest(request);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void doTestMultipartHttpServletRequest(MultipartHttpServletRequest request) throws IOException {
		Set fileNames = new HashSet();
		Iterator fileIter = request.getFileNames();
		while (fileIter.hasNext()) {
			fileNames.add(fileIter.next());
		}
		assertEquals(2, fileNames.size());
		assertTrue(fileNames.contains("file1"));
		assertTrue(fileNames.contains("file2"));
		MultipartFile file1 = request.getFile("file1");
		MultipartFile file2 = request.getFile("file2");
		Map fileMap = request.getFileMap();
		List fileMapKeys = new LinkedList(fileMap.keySet());
		assertEquals(2, fileMapKeys.size());
		assertEquals(file1, fileMap.get("file1"));
		assertEquals(file2, fileMap.get("file2"));

		assertEquals("file1", file1.getName());
		assertEquals("", file1.getOriginalFilename());
		assertNull(file1.getContentType());
		assertTrue(ObjectUtils.nullSafeEquals("myContent1".getBytes(), file1.getBytes()));
		assertTrue(ObjectUtils.nullSafeEquals("myContent1".getBytes(), FileCopyUtils.copyToByteArray(file1.getInputStream())));
		assertEquals("file2", file2.getName());
		assertEquals("myOrigFilename", file2.getOriginalFilename());
		assertEquals("text/plain", file2.getContentType());
		assertTrue(ObjectUtils.nullSafeEquals("myContent2".getBytes(), file2.getBytes()));
		assertTrue(ObjectUtils.nullSafeEquals("myContent2".getBytes(), FileCopyUtils.copyToByteArray(file2.getInputStream())));
	}

}