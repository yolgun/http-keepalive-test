package yolgun;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by YunusOlgun on 1/24/2017.
 */
public class MyConnection {
	private static final int connectionTimeoutMs = 20000;
	private static final int timeoutMs = 60000;
	private OkHttpClient client = new OkHttpClient();


	/**
	 * Support for synchronous HTTP transactions.  That is, a message
	 * is sent over an HTTP connection to the URL described by the
	 * urlObj and the function waits for a reply.
	 *
	 * @param urlString the call to make
	 * @param postData  postdata to add to the call, if in POST mode (if <code>null</code>, a GET is assumed)
	 * @return a string with the response message
	 */
	public String processHttpMessage(String urlString, final String postData) throws IOException {
		Request request = new Request.Builder()
				.url(urlString)
				.build();

		Response response = client.newCall(request)
								  .execute();
		return response.body()
					   .string();
	}

	private HttpURLConnection initHttpConnection(final String urlString, final String requestMethod) throws IOException {

		HttpURLConnection returnCon = null;

		URL url;
		try {
			url = new URL(urlString);
		} catch (final MalformedURLException e) {
			System.err.println("MalformedURLException = " + e);
			System.err.println(e);
			url = null;
		}

		if (url != null) {
			final HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
			httpCon.setRequestMethod(requestMethod);
			httpCon.setDoInput(true);//true by default
			httpCon.setDoOutput(true);
			httpCon.setUseCaches(false);
			httpCon.setAllowUserInteraction(false);
			httpCon.setRequestProperty("Context-Type", "text/html");
			httpCon.setRequestProperty("User-Agent", getUserAgentIdentification());
//			httpCon.setRequestProperty("Connection", "Keep-Alive");
//			httpCon.setRequestProperty("Keep-Alive", "header");
			httpCon.setReadTimeout(timeoutMs);
			httpCon.setConnectTimeout(connectionTimeoutMs);
			httpCon.connect();
			returnCon = httpCon;
		}

		return returnCon;
	}

	/**
	 * Generate an identification String for this user agent.
	 */
	private String getUserAgentIdentification() {
		return "SupClient/";
	}

}
