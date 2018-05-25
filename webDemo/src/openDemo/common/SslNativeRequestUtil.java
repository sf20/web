package openDemo.common;

import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;

public class SslNativeRequestUtil {
	private static final int TIME_OUT = 5000;
	private static final String CHARSET_UTF8 = "UTF-8";

	public static String getRequest(String urlAddress) throws Exception {
		URL url = new URL(urlAddress);
		if ("https".equalsIgnoreCase(url.getProtocol())) {
			ignoreSsl();
		}
		URLConnection conn = url.openConnection();
		conn.setConnectTimeout(TIME_OUT);
		conn.setReadTimeout(TIME_OUT);
		return IOUtils.toString(conn.getInputStream(), CHARSET_UTF8);
	}

	public static String postRequest(String urlAddress, String params) throws Exception {
		URL url = new URL(urlAddress);
		if ("https".equalsIgnoreCase(url.getProtocol())) {
			ignoreSsl();
		}
		URLConnection conn = url.openConnection();
		conn.setDoInput(true);
		conn.setDoOutput(true);
		conn.setConnectTimeout(TIME_OUT);
		conn.setReadTimeout(TIME_OUT);
		OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream(), CHARSET_UTF8);
		osw.write(params);
		osw.flush();
		osw.close();
		return IOUtils.toString(conn.getInputStream(), CHARSET_UTF8);
	}

	/**
	 * 忽略HTTPS请求的SSL证书，必须在openConnection之前调用
	 * 
	 * @throws Exception
	 */
	private static void ignoreSsl() throws Exception {
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String urlHostName, SSLSession session) {
				return true;
			}
		};
		trustAllHttpsCertificates();
		HttpsURLConnection.setDefaultHostnameVerifier(hv);
	}

	private static void trustAllHttpsCertificates() throws Exception {
		TrustManager[] trustAllCerts = new TrustManager[1];
		TrustManager tm = new X509TrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		trustAllCerts[0] = tm;
		SSLContext sc = SSLContext.getInstance("SSL");
		sc.init(null, trustAllCerts, null);
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	}

}