package gov.hhs.cms.aca.global_assets.core.commons;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.rmi.ServerException;
import java.util.HashMap;

import org.junit.Test;

public class HttpUtilsTest {

	@Test
	public void testGetBasicAuthToken() {

		String token = HttpUtils.getBasicAuthToken("admin", "admin");

		assertTrue("Testing basic authentication token", null != token && !token.isEmpty());

	}

	@Test
	public void testCreateQueryString() throws UnsupportedEncodingException {

		HashMap<String, String> params = new HashMap<String, String>();

		params.put("test", "hello");
		params.put("test2", "hello2 space");

		String queryString = HttpUtils.createQueryString(params);

		assertEquals("Testing query string", "?test2=hello2+space&test=hello", queryString);
	}

	@Test
	public void testHttpRequest() throws ServerException, IOException {

		ByteArrayOutputStream byteArrayOutputStream1 = new ByteArrayOutputStream();

		HttpUtils.httpRequest("http://google.ca", null, "GET", null, byteArrayOutputStream1);

		assertNotNull("Testing response", byteArrayOutputStream1);
		
		ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();

		HttpUtils.httpRequest("https://httpbin.org/post", null, "POST", null, byteArrayOutputStream2);

		assertNotNull("Testing response", byteArrayOutputStream2);
	}

}
