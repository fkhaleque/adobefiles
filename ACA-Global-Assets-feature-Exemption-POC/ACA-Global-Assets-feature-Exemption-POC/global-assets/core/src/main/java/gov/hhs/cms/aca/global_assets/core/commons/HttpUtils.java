package gov.hhs.cms.aca.global_assets.core.commons;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.rmi.ServerException;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.Header;
import org.apache.http.entity.mime.FormBodyPart;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.request.RequestParameter;

/**
 * A helper class which contains helper methods related to HTTP calls
 * 
 * @author Guillaume
 * 
 */
public class HttpUtils {

	public static final String HTTPS_PREFIX = "https://";
	public static final String HTTP_PREFIX = "http://";
	
	/**
	 * Create a new Basic Authentication token for use in an Authorization header
	 * 
	 * @param username
	 * @param password
	 * @return string
	 */
	public static String getBasicAuthToken(String username, String password)
	{
		String basicAuthString = username + ":" + password;
		byte [] baseBytes = Base64.getEncoder().encode(basicAuthString.getBytes());

		String token = new String(baseBytes); 
		token = "Basic " + token;

		return token;
	}

	public static String createQueryString(Map<String, String> params) throws UnsupportedEncodingException {

		StringBuffer requestParams = new StringBuffer();
		int i = 0;

		for (Map.Entry<String, String> entry : params.entrySet()) {

			//First param
			if(i == 0){
				requestParams.append("?")
				.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
				.append("=")
				.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			}
			//Others
			else {
				requestParams.append("&")
				.append(URLEncoder.encode(entry.getKey(), "UTF-8"))
				.append("=")
				.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
			}

			//Simulate for on for-each	
			i++;	
		}

		return requestParams.toString();
	}

	/**
	 * A convenience request handler to submit generic GET/POST requests to endpoints
	 * @param requestURL
	 * @param basicAuthToken
	 * @param method
	 * @param outputStream
	 * @throws ServerException
	 * @throws IOException
	 */
	public static void httpRequest(String requestURL, 
			String basicAuthToken,
			String method,
			Map<String,String> headers,
			OutputStream outputStream) throws ServerException, IOException{

		URL targetUrl = null;

		//Instantiate the new URL
		targetUrl = new URL(requestURL);

		//Open Connection
		if(requestURL.startsWith(HTTPS_PREFIX)){
			HttpsURLConnection httpsURLConnection = (HttpsURLConnection) targetUrl.openConnection();

			//Set up headers and method
			httpsURLConnection.setDoOutput(true);
			httpsURLConnection.setRequestMethod(method);
			httpsURLConnection.setUseCaches(false);
			httpsURLConnection.setRequestProperty("Cache-Control", "no-cache");
			
			// Add custom header
			if (headers != null ) {			
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpsURLConnection.setRequestProperty(entry.getKey(),entry.getValue());
				}
			}


			//Handle basic authentication
			if(null != basicAuthToken && !basicAuthToken.isEmpty()){
				httpsURLConnection.setRequestProperty("Authorization", basicAuthToken);
			}

			httpRequest(httpsURLConnection, outputStream);
		}
		else if (requestURL.startsWith(HTTP_PREFIX)) {
			HttpURLConnection httpURLConnection = (HttpURLConnection) targetUrl.openConnection();

			//Set up headers and method
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setRequestMethod(method);
			httpURLConnection.setUseCaches(false);
			httpURLConnection.setRequestProperty("Cache-Control", "no-cache");

			// Add custom header
			if (headers != null ) {			
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httpURLConnection.setRequestProperty(entry.getKey(),entry.getValue());
				}
			}

			//Handle basic authentication
			if(null != basicAuthToken && !basicAuthToken.isEmpty()){
				httpURLConnection.setRequestProperty("Authorization", basicAuthToken);
			}

			httpRequest(httpURLConnection, outputStream);
		}
		else{
			throw new ServerException("Unsupported protocol. Supported protocols are http:// and https://");
		}
	}

	/**
	 * A helper to handle submitting custom HttpsURLConnection
	 * 
	 * @param httpsURLConnection
	 * @param basicAuthToken
	 * @param method
	 * @param outputStream
	 * @throws ServerException
	 * @throws IOException
	 */
	public static void httpRequest(HttpsURLConnection httpsURLConnection,
			OutputStream outputStream) throws ServerException, IOException{

		InputStream input = null;

		try {

			//Initiate connection
			int responseCode = httpsURLConnection.getResponseCode();

			//Handle response code gracefully or throw an exception
			if(responseCode == SlingHttpServletResponse.SC_OK){
				//Read the remote request's stream
				input = httpsURLConnection.getInputStream();

				Primitives.stream(input, outputStream);
			}
			else if(responseCode == SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR){

				input = httpsURLConnection.getErrorStream();

				Primitives.stream(input, outputStream);

				throw new ServerException("Request failed with HTTP response code: " + responseCode);
			}
			else{

				input = httpsURLConnection.getInputStream();

				if(null == input){
					input = httpsURLConnection.getErrorStream();
				}

				Primitives.stream(input, outputStream);

				throw new ServerException("Request failed with HTTP response code: " + responseCode);
			}

		} finally {
			//Disconnect and clean up
			if(null != httpsURLConnection){
				httpsURLConnection.disconnect();
			}
			if(null != input){
				input.close();
			}
		}
	}

	/**
	 * A helper to handle submitting custom HttpURLConnection
	 * 
	 * @param httpsURLConnection
	 * @param basicAuthToken
	 * @param method
	 * @param outputStream
	 * @throws ServerException
	 * @throws IOException
	 */
	public static void httpRequest(HttpURLConnection httpsURLConnection, 
			OutputStream outputStream) throws ServerException, IOException{

		InputStream input = null;

		try {

			//Initiate connection
			int responseCode = httpsURLConnection.getResponseCode();

			//Handle response code gracefully or throw an exception
			if(responseCode == SlingHttpServletResponse.SC_OK){
				//Read the remote request's stream
				input = httpsURLConnection.getInputStream();

				Primitives.stream(input, outputStream);
			}
			else if(responseCode == SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR){

				input = httpsURLConnection.getErrorStream();

				Primitives.stream(input, outputStream);

				throw new ServerException("Request failed with HTTP response code: " + responseCode);
			}
			else{

				input = httpsURLConnection.getInputStream();

				if(null == input){
					input = httpsURLConnection.getErrorStream();
				}

				Primitives.stream(input, outputStream);

				throw new ServerException("Request failed with HTTP response code: " + responseCode);
			}

		} finally {
			//Disconnect and clean up
			if(null != httpsURLConnection){
				httpsURLConnection.disconnect();
			}
			if(null != input){
				input.close();
			}

		}
	}

	/**
	 * A method used to request delegation. This method will instantiate a new request to a remote host and stream it's contents 
	 * to the provided response, therefore acting as a basic proxy.
	 * 
	 * <b>NOTE: This method will commit the provided response. A committed response can no longer be modified.</b>
	 * 
	 * @param remoteHostUri
	 * @param basicAuthToken
	 * @param enforceSSL
	 * @param request
	 * @param response
	 * @throws ServerException
	 * @throws IOException
	 * @throws ServletException 
	 */
	public static void proxyRequest(String remoteHostUri, 
			String basicAuthToken,
			String acceptedResponse,
			boolean enforceSSL, 
			SlingHttpServletRequest request, 
			SlingHttpServletResponse response) throws ServerException, IOException, ServletException{

		HttpURLConnection httpConnection = null; // TODO Test SSL Support with HTTP object
		URL targetUrl = null;
		InputStream proxyInput = null;
		OutputStream originOutput = null;
		OutputStream remoteOutput = null;

		try {

			//Check is SSL is enforced
			if (!remoteHostUri.startsWith(HTTPS_PREFIX) && enforceSSL) {
				throw new ServletException("A clear text (non-SSL) request was attempted during RPC delegation."); 
			}

			//TODO Could probably be handled via cloneConnection();

			//Load context path if one is available
			if(null != request.getRequestURI() && !request.getRequestURI().isEmpty()){
				remoteHostUri += request.getRequestURI();
			}		

			//Load query string if one is available
			if(null != request.getQueryString()){
				remoteHostUri += "?" + request.getQueryString();
			}

			//Instantiate the new URL
			targetUrl = new URL(remoteHostUri);

			//Open Connection
			httpConnection = (HttpURLConnection) targetUrl.openConnection();

			//Set up headers and method
			httpConnection.setDoOutput(true);
			httpConnection.setRequestMethod(request.getMethod());
			httpConnection.setRequestProperty("Content-Type", (Primitives.isNull(request.getContentType())) ? "" : request.getContentType()); //Set content type if we have one
			httpConnection.setRequestProperty("Accept", (Primitives.isNull(acceptedResponse)) ? "text/plain" : acceptedResponse); //Set accepted response if we have one

			//Handle basic authentication
			if(null != basicAuthToken && !basicAuthToken.isEmpty()){
				httpConnection.setRequestProperty("Authorization", basicAuthToken);
			}

			//Is this a way-too-hard-to-handle-for-it's-own-good POST?
			if (request.getMethod().equalsIgnoreCase("post")) {

				/*
				 * Once we've even simply figured out this was a POST, we've committed the data read on the input stream for the request.
				 * Now we're stuck reading and parsing the whole thing to rebuild the multipart message on our own before proxying it in...
				 */
				java.util.Map<String, org.apache.sling.api.request.RequestParameter[]> params = parseMultipartParams((SlingHttpServletRequest) request);

				//Build a new multipart entity from the parsed params
				MultipartEntity multipartEntity = buildMultipartRequest(params);

				//Get the content type of the Multipart Entity. This is super important since it contains the unique boundary flag
				Header contentType = multipartEntity.getContentType();

				/*
				 * Set the content type of the remote connection. This tells the connection it's a multipart message,
				 * but more importantly tells it how to parse it by splitting the multipart boundaries. 
				 */
				httpConnection.setRequestProperty(contentType.getName(), contentType.getValue());

				//Get the remote output stream
				remoteOutput = httpConnection.getOutputStream();

				//Write multipart to output stream
				multipartEntity.writeTo(remoteOutput);

				//Flush the stream
				remoteOutput.flush();

			}

			//Initiate connection
			int responseCode = httpConnection.getResponseCode();

			//Handle response code gracefully or throw an exception
			if(responseCode == SlingHttpServletResponse.SC_OK){
				//Read the remote request's stream
				proxyInput = httpConnection.getInputStream();
			}
			else if(responseCode == SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR){
				proxyInput = httpConnection.getErrorStream();
			}
			else{
				throw new ServerException("Request failed with HTTP response code: " + responseCode);
			}

			//Transfer Connection Properties
			response.setContentType(httpConnection.getContentType());
			response.setContentLength(httpConnection.getContentLength());

			//Open up our response stream
			originOutput = response.getOutputStream();

			//Transfer the remote stream to our response stream
			Primitives.stream(proxyInput, originOutput);

		} finally {
			//Disconnect and clean up
			if(!Primitives.isNull(httpConnection)){
				httpConnection.disconnect();
			}
			if(!Primitives.isNull(proxyInput)){
				proxyInput.close();
			}
			if(!Primitives.isNull(originOutput)){
				originOutput.close();
			}
			if(!Primitives.isNull(remoteOutput)){
				remoteOutput.close();
			}
		}
	}

	/**
	 * Takes a map of parameters, and adds them to a multipart entity that can be used 
	 * 
	 * @param params
	 * @return MultipartEntity
	 * @throws IOException
	 */
	public static MultipartEntity buildMultipartRequest(java.util.Map<String, org.apache.sling.api.request.RequestParameter[]> params) throws IOException {

		//Replicate what a browser would send, generate a new boundary, and use UTF-8
		MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE, null, Charset.defaultCharset());

		//For every param we need a new part
		for (final java.util.Map.Entry<String, org.apache.sling.api.request.RequestParameter[]> pairs : params.entrySet()) {

			//Get request parameters
			final org.apache.sling.api.request.RequestParameter[] pArr = pairs.getValue();

			if(!pArr[0].isFormField()){
				//This is a file! Include an output stream body 
				InputStreamBody inputStreamBody = new InputStreamBody(pArr[0].getInputStream(), pArr[0].getContentType(), pArr[0].getFileName()); 

				multipartEntity.addPart(pArr[0].getFileName(), inputStreamBody);

			} else {
				//This is some other param, include it in a form body
				FormBodyPart formBodyPart = new FormBodyPart(pArr[0].getName(), new StringBody(pArr[0].getString()));

				multipartEntity.addPart(formBodyPart);				
			}			
		}

		return multipartEntity;		
	}

	/**
	 * Parses multipart parameters and returns a map or RequestParameters
	 * @param request
	 * @return params
	 * @throws ServletException
	 */
	public static java.util.Map<String, org.apache.sling.api.request.RequestParameter[]> parseMultipartParams(SlingHttpServletRequest request) throws ServletException {

		final java.util.Map<String, org.apache.sling.api.request.RequestParameter[]> params = request.getRequestParameterMap();

		return params;		
	}


	/**
	 * A connection clone method. This method will take all connection settings and readable 
	 * request properties and map them to a new connection object. This clone will not commit
	 * the new connection object, and will leave the object open for the caller to close.
	 * 
	 * Non string properties will not be cloned, and an exception will be thrown if such a property 
	 * is cloned. 
	 * 
	 * @param httpURLConnection
	 * @return cloneConnection
	 * @throws IOException
	 * @deprecated - Not in use
	 */
	public static HttpURLConnection cloneConnection(HttpURLConnection httpURLConnection) throws IOException {

		HttpURLConnection cloneConnection = null;
		URL targetUrl = null;

		//Instantiate the new URL
		targetUrl = httpURLConnection.getURL();

		cloneConnection = (HttpURLConnection) targetUrl.openConnection();

		cloneConnection.setDoOutput(httpURLConnection.getDoOutput());
		cloneConnection.setDoInput(httpURLConnection.getDoInput());
		cloneConnection.setRequestMethod(httpURLConnection.getRequestMethod());

		//Get all headers
		Map<String, List<String>> headers = httpURLConnection.getRequestProperties();

		//Map all request properties
		for (Entry<String, List<String>> header : headers.entrySet()) {

			String cloneProperty = "";

			for (String property : header.getValue()) {
				cloneProperty = cloneProperty
						.concat(property)
						.concat(","); //Will add a comma for the last value as well, don't care
			}

			cloneConnection.setRequestProperty(header.getKey(), cloneProperty);
		}		

		return cloneConnection;

	}

	/**
	 * A connection clone method. This method will take all connection settings and readable 
	 * request properties and map them to a new connection object. This clone will not commit
	 * the new connection object, and will leave the object open for the caller to close.
	 * 
	 * Non string properties will not be cloned, and an exception will be thrown if such a property 
	 * is cloned. 
	 * 
	 * @param httpServletRequest
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 */
	public static HttpURLConnection cloneConnection(String remoteHostUri, String basicAuthToken, HttpServletRequest httpServletRequest) throws IOException, ServletException {

		HttpURLConnection cloneConnection = null;
		URL targetUrl = null;

		//Load context path if one is available
		if(null != httpServletRequest.getRequestURI() && !httpServletRequest.getRequestURI().isEmpty()){
			remoteHostUri += httpServletRequest.getRequestURI();
		}		

		//Load query string if one is available
		if(null != httpServletRequest.getQueryString()){
			remoteHostUri += "?" + httpServletRequest.getQueryString();
		}
		
		//Instantiate the new URL
		targetUrl = new URL(remoteHostUri);

		cloneConnection = (HttpURLConnection) targetUrl.openConnection();

		//Assuming this is true
		cloneConnection.setDoOutput(true);
		cloneConnection.setDoInput(true);
		cloneConnection.setRequestMethod(httpServletRequest.getMethod());
		
		//Handle basic authentication
		if(null != basicAuthToken && !basicAuthToken.isEmpty()){
			cloneConnection.setRequestProperty("Authorization", basicAuthToken);
		}

		return cloneConnection;

	}

}
