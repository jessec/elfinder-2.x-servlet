package io.core9.elfinder.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

public class ResponseDto {

	private HttpServletResponse response;

	public void setContentType(String contentType) {
		response.setContentType(contentType);
	}

	public PrintWriter getWriter() throws java.io.IOException {
		return response.getWriter();
	}

	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	public void setCharacterEncoding(String encoding) {
		response.setCharacterEncoding(encoding);
	}

	public void setHeader(String key, String value) {
		response.setHeader(key, value);
	}

	public OutputStream getOutputStream() throws IOException {
		return response.getOutputStream();
	}

	public void setContentLength(int size) {
		response.setContentLength(size);
	}

}
