package openDemo.common;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * HttpUtil
 */
public class HttpUtil {
	private static final Logger LOGGER = Logger.getLogger("HttpUtil");
	public static final String STARFISH_TOKEN = "gAAAAABW-dfuGHZVbBcl-Ni3Oj1RtI7ga3Bfyk4-TcYv3p5hpmSzFN98oKaFldT_DxGIHKvVuubF8ElaR0BFjKNCaAuJngz3Xg==";
	private static final String STARFISH_PUSH_TOKEN = "gAAAAABXB1xu0HvVWLKC9xgU4QsP5J80OCOU1K0Nt1YJ-CrChgOS4ifweQpbiXGEZYF-FHNxgH3YqqEA1VEPKmPfXFLYuXUAitYU2zzu63f5WJJAC9rtfao=";
	private static final String ERROR_JSON = "{\"error\":{\"key\":\"global.ServiceInternalError\",\"message\":\"A server error has been encountered.\"}}";
	private static RestTemplate restTemplate;

    static {
   	 PoolingHttpClientConnectionManager pollingConnectionManager = new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
     // 总连接数
     pollingConnectionManager.setMaxTotal(1000);
     // 同路由的并发数
     pollingConnectionManager.setDefaultMaxPerRoute(1000);

     HttpClientBuilder httpClientBuilder = HttpClients.custom();
     httpClientBuilder.setConnectionManager(pollingConnectionManager);
     // 重试次数，默认是3次，没有开启
     httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));
     // 保持长连接配置，需要在头添加Keep-Alive
     httpClientBuilder.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy());

     List<Header> headers = new ArrayList<Header>();
     headers.add(new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.16 Safari/537.36"));
     headers.add(new BasicHeader("Accept-Encoding", "gzip,deflate"));
     headers.add(new BasicHeader("Accept-Language", "zh-CN"));
     headers.add(new BasicHeader("Connection", "Keep-Alive"));

     httpClientBuilder.setDefaultHeaders(headers);

     HttpClient httpClient = httpClientBuilder.build();

     // httpClient连接配置，底层是配置RequestConfig
     HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
     // 连接超时
     clientHttpRequestFactory.setConnectTimeout(5000);
     // 数据读取超时时间，即SocketTimeout
    // clientHttpRequestFactory.setReadTimeout(5000);
     // 连接不够用的等待时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
     clientHttpRequestFactory.setConnectionRequestTimeout(200);
     // 缓冲请求数据，默认值是true。通过POST或者PUT大量发送数据时，建议将此属性更改为false，以免耗尽内存。
     // clientHttpRequestFactory.setBufferRequestBody(false);
     restTemplate = new RestTemplate(clientHttpRequestFactory);
    }
 

	/**
	 * sendPostForCreateTask
	 *
	 * @param url String
	 * @param userId String
	 * @param orgId String
	 * @param fullName String
	 */
	public static void sendPostForCreateTask(String url, String userId, String orgId, String fullName) {
		HttpPost post = new HttpPost(url);
		CloseableHttpClient httpclient = HttpClients.createDefault();
		JSONObject jsonParam = new JSONObject();
		jsonParam.put("OrgID", orgId);
		jsonParam.put("CreateUserID", userId);
		jsonParam.put("CreateUserName", fullName);
		jsonParam.put("CreateDate", new Date());
		jsonParam.put("apikey", "ELearning");
		jsonParam.put("salt", "939500325685895");
		jsonParam.put("signature", "1e55406904a1c7de72833775659d9e017273691a9bcb04d2bf7bcf794823b737");
		StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		post.setEntity(entity);
		try {
			HttpResponse result = httpclient.execute(post);
			result.getEntity().writeTo(System.out);
		} catch (Exception e) {
			// LOGGER.error("httpclient.execute() error:", e);
		}
	}

	/**
	 * sendPost
	 *
	 * @param request HttpServletRequest
	 * @param url String
	 * @param json JSONObject
	 * @return String
	 */
	public static String sendPost(HttpServletRequest request, String url, JSONObject json) {
		HttpHeaders headers = getHttpHeaders(request);
		return sendPost(headers, url, json.toString());
	}
	
	/**
	 * sendPost
	 *
	 * @param request HttpServletRequest
	 * @param url String
	 * @param json JSONObject
	 * @return String
	 */
	public static String sendPostPool(HttpServletRequest request, String url, JSONObject json) {
		HttpHeaders headers = getHttpHeaders(request);
		return sendPostPool(headers, url, json.toString());
	}


	/**
	 * sendPost
	 *
	 * @param headers Map
	 * @param url String
	 * @param json JSONObject
	 * @return String
	 */
	public static String sendPost(Map<String, String> headers, String url, JSONObject json) {
		HttpHeaders header = createHeaders(headers);
		HttpEntity<String> httpEntity = new HttpEntity<String>(json.toString(), header);
		String result;
		try {
			result = restTemplate.postForObject(url, httpEntity, String.class);
		} catch (HttpClientErrorException e) {
			// LOGGER.error("HttpUtil.exchange() error:url:" + url + "json:" + json.toString(), e);
			result = "{}";
		}
		return result;
	}

	/**
	 * getClientIp
	 *
	 * @param request HttpServletRequest
	 * @return String
	 */
	public static String getClientIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (StringUtils.isBlank(ipAddress)) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	/**
	 * sendPost
	 *
	 * @param url String
	 * @param json String
	 * @return String
	 */
	public static String sendPost(String url, String json) {
		return sendPost(getHttpHeaders(), url, json);
	}

	/**
	 * sendPost
	 *
	 * @param url String
	 * @param json JSONObject
	 * @return String
	 */
	public static String sendPost(String url, JSONObject json) {
		return sendPost(url, json.toString());
	}

	/**
	 * sendPost
	 *
	 * @param request HttpServletRequest
	 * @param url String
	 * @param json String
	 * @return String
	 */
	public static String sendPost(HttpServletRequest request, String url, String json) {
		HttpHeaders headers = getHttpHeaders(request);
		return sendPost(headers, url, json);
	}

	/**
	 * sendPost
	 *
	 * @param headers HttpHeaders
	 * @param url String
	 * @param json String
	 * @return String
	 */
	public static String sendPostPool(HttpHeaders headers, String url, String json) {
		HttpEntity<String> httpEntity = new HttpEntity<String>(json, headers);
		String result;
		try {
			result = restTemplate.postForObject(url, httpEntity, String.class);
		} catch (HttpClientErrorException e) {
			result = e.getResponseBodyAsString();
			// LOGGER.error("restTemplate.postForObject() error:" + e.getResponseBodyAsString() + "@url:" + url + "@@body:" + json, e);
		} catch (Exception e) {
			result = "";
			// LOGGER.error("restTemplate.postForObject() error:" + e.getMessage() + "@url:" + url + "@@body:" + json, e);
		}
		return result;
	}
	
	/**
	 * sendPost
	 *
	 * @param headers HttpHeaders
	 * @param url String
	 * @param json String
	 * @return String
	 */
	public static String sendPost(HttpHeaders headers, String url, String json) {
		HttpEntity<String> httpEntity = new HttpEntity<String>(json, headers);
		String result;
		try {
			result = restTemplate.postForObject(url, httpEntity, String.class);
		} catch (HttpClientErrorException e) {
			result = e.getResponseBodyAsString();
			// LOGGER.error("restTemplate.postForObject() error:" + e.getResponseBodyAsString() + "@url:" + url + "@@body:" + json, e);
		} catch (Exception e) {
			result = "";
			// LOGGER.error("restTemplate.postForObject() error:" + e.getMessage() + "@url:" + url + "@@body:" + json, e);
		}
		return result;
	}


	public static String sendDelete(HttpServletRequest request, String url) {
		return sendDelete(null, request, url);
	}

	public static String sendDelete(HttpHeaders httpHeaders, HttpServletRequest request, String url) {
		HttpHeaders headers = httpHeaders;
		if (headers == null) {
			headers = getHttpHeaders(request);
		}
		HttpEntity<String> httpEntity = new HttpEntity<String>(null, headers);
		String result;
		try {
			restTemplate.exchange(url, HttpMethod.DELETE, httpEntity, String.class);
			result = "";
		} catch (HttpClientErrorException e) {
			result = e.getResponseBodyAsString();
			// LOGGER.error("restTemplate.sendDelete() error:" + e.getResponseBodyAsString() + "@url:" + url, e);
		} catch (Exception e) {
			result = "";
			// LOGGER.error("restTemplate.sendDelete() error:" + e.getMessage() + "@url:" + url, e);
		}
		return result;
	}

	/**
	 * sendPut
	 *
	 * @param headers HttpHeaders
	 * @param url String
	 * @param json String
	 * @return String
	 */
	public static String sendPut(HttpHeaders headers, String url, String json) {
		HttpEntity<String> httpEntity = new HttpEntity<String>(json, headers);
		String result = null;
		try {
            restTemplate.put(url, httpEntity, String.class);
		} catch (HttpClientErrorException e) {
			result = e.getResponseBodyAsString();
		} catch (Exception e) {
			result = "";
			// LOGGER.error("restTemplate.put() error:" + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * sendPut
	 *
	 * @param request HttpServletRequest
	 * @param url String
	 * @param json String
	 * @return String
	 */
	public static String sendPut(HttpServletRequest request, String url, String json) {
		HttpHeaders headers = getHttpHeaders(request);

		return sendPut(headers, url, json);
	}

	/**
	 * sendGet
	 *
	 * @param request HttpServletRequest
	 * @param url String
	 * @return String
	 */
	public static String sendGet(HttpServletRequest request, String url) {
		HttpHeaders headers = getHttpHeaders(request);
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
		ResponseEntity<String> jsonRes = exchange(url, HttpMethod.GET, httpEntity);
		return jsonRes.getBody();
	}

	/**
	 * createHeaders
	 *
	 * @param headers Map
	 * @return HttpHeaders
	 */
	public static HttpHeaders createHeaders(Map<String, String> headers) {
		HttpHeaders headers2 = new HttpHeaders();
		for (Map.Entry<String, String> entry : headers.entrySet()) {
			headers2.add(entry.getKey(), entry.getValue());
		}
		headers2.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		return headers2;
	}

	/**
	 * sendGet
	 *
	 * @param headers HttpHeaders
	 * @param url String
	 * @return String
	 */
	public static String sendGet(HttpHeaders headers, String url) {
		HttpEntity<String> httpEntity = new HttpEntity<String>(headers);
		ResponseEntity<String> jsonRes = exchange(url, HttpMethod.GET, httpEntity);
		return jsonRes.getBody();
	}

	/**
	 * getHttpHeaders
	 *
	 * @return HttpHeaders
	 */
	public static HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		return headers;
	}

	private static HttpHeaders getHttpHeaders(HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		if (StringUtils.isNotBlank(request.getHeader("Source"))) {
			headers.set("Source", request.getHeader("Source"));
		}
		if (StringUtils.isNotBlank(request.getHeader("Token"))) {
			headers.set("Token", request.getHeader("Token"));
		}
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		return headers;
	}

	/**
	 * exchange
	 *
	 * @param url String
	 * @param httpMethod HttpMethod
	 * @param httpEntity HttpEntity
	 * @return ResponseEntity
	 */
	public static ResponseEntity<String> exchange(String url, HttpMethod httpMethod, HttpEntity<String> httpEntity) {
		ResponseEntity<String> result;
		try {
			result = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
		} catch (HttpClientErrorException e) {
			result = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			result = new ResponseEntity<String>(ERROR_JSON, HttpStatus.INTERNAL_SERVER_ERROR);
			// LOGGER.error("restTemplate.exchange() error:" + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * exchangeWithPool
	 *
	 * @param url String
	 * @param httpMethod HttpMethod
	 * @param httpEntity HttpEntity
	 * @return ResponseEntity
	 */
	public static ResponseEntity<String> exchangeWithPool(String url, HttpMethod httpMethod,
														  HttpEntity<String> httpEntity) {
		ResponseEntity<String> result;
		try {
			result = restTemplate.exchange(url, httpMethod, httpEntity, String.class);
		} catch (HttpClientErrorException e) {
			result = new ResponseEntity<String>(e.getResponseBodyAsString(), e.getStatusCode());
		} catch (Exception e) {
			result = new ResponseEntity<String>(ERROR_JSON, HttpStatus.INTERNAL_SERVER_ERROR);
			// LOGGER.error("restTemplate.exchange() error:" + e.getMessage(), e);
		}
		return result;
	}

	/**
	 * getHttpEntity
	 *
	 * @return HttpEntity
	 */
	public static HttpEntity<MultiValueMap<String, String>> getHttpEntity() {
		MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		return new HttpEntity<MultiValueMap<String, String>>(map, headers);
	}

	/**
	 * sendPostQidaAPI
	 *
	 * @param request HttpServletRequest
	 * @param url String
	 * @param json JSONObject
	 * @return String
	 */
	public static String sendPostQidaAPI(HttpServletRequest request, String url, JSONObject json) {
		HttpHeaders headers = getQidaHttpHeaders();
		HttpEntity<String> httpEntity = new HttpEntity<String>(json.toString(), headers);
		String result;
		try {
			result = restTemplate.postForObject(url, httpEntity, String.class);
		} catch (HttpClientErrorException e) {
			// LOGGER.error("HttpUtil.exchange() error", e);
			result = e.getResponseBodyAsString();
		}
		return result;
	}

	private static HttpHeaders getQidaHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		return headers;
	}

	public static String sendPost(String url, HttpEntity<MultiValueMap<String, String>> httpEntity) {
		String result;
		try {
			result = restTemplate.postForObject(url, httpEntity, String.class);
		} catch (HttpClientErrorException e) {
			result = e.getResponseBodyAsString();
		}
		return result;
	}

	/**
	 * sendGet
	 *
	 * @param url String
	 * @return String
	 */
	public static String sendGet(String url) {
		ResponseEntity<String> jsonRes = exchange(url, HttpMethod.GET, null);
		return jsonRes.getBody();
	}

	/**
	 * postXml
	 *
	 * @param url String
	 * @param xml String
	 * @return String
	 */
	public static String postXml(String url, String xml) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "xml", Charset.forName("UTF-8")));
		HttpEntity<String> httpEntity = new HttpEntity<String>(xml, headers);
		String result;
		try {
			result = restTemplate.postForObject(url, httpEntity, String.class);
		} catch (HttpClientErrorException e) {
			result = e.getResponseBodyAsString();
		}
		return result;
	}

}
