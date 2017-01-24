/**
 *
 */
package yolgun;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * A class used for outgoing http calls.
 *
 * <p>
 * Once the call has been initialized, calls with postdata can be issued.</p>
 *
 *
 * @author Tobias
 *
 */
@SuppressWarnings("ALL")
public class UltHttpConnection {

	private static final int connectionTimeoutMs=20000;
	private static final int timeoutMs=60000;


	/**
	 * Support for synchronous HTTP transactions.  That is, a message
	 * is sent over an HTTP connection to the URL described by the
	 * urlObj and the function waits for a reply.
	 * @param urlString the call to make
	 * @param postData postdata to add to the call, if in POST mode (if <code>null</code>, a GET is assumed)
	 * @return a string with the response message
	 */
	public String processHttpMessage(String urlString, final String postData)	{
		String requestMode = "GET";
		if(postData!=null) {
			requestMode = "POST";
		}


		final StringBuilder response = new StringBuilder();

		try {
			if(urlString.lastIndexOf("https:") > 4) {
				System.out.println("sanitizing https URL: " + urlString);
				urlString = urlString.substring(urlString.lastIndexOf("https:"));
			}
			final HttpURLConnection httpConn = initHttpConnection(urlString, requestMode );
			final int msgStatus;
			final String statusMsg;
			if(postData!=null){
				try (DataOutputStream dos = new DataOutputStream(httpConn.getOutputStream())) {
					dos.writeBytes(postData);
					dos.flush();
				}
			}
			msgStatus = httpConn.getResponseCode();
			statusMsg = httpConn.getResponseMessage();
			if(msgStatus==HttpURLConnection.HTTP_OK){
				try (BufferedReader httpReader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()))) {
					boolean lineRead = false;
					char[] cbuf = new char[512];
					while (httpReader.read(cbuf) != -1) {
						if (lineRead) {
							response.append("\n");
						}
						response.append(httpReader.readLine());
						lineRead = true;
					}
				}
			} else {
				System.err.println("UltHttpConnection: request "+urlString+" returned status "+msgStatus+", "+statusMsg);
			}
			httpConn.disconnect();
		} catch (final IOException e ) {
			System.err.println("processHttpConnection: " +  e );
		} catch (final Exception ex) {
			System.err.println("HTTP Message Process Failed: " +  ex + " URL: " + urlString + " Data: " + postData);
		}

		if(response.length()>0) {
			return response.toString();
		} else {
			System.err.println("Empty Response - URL: " + urlString + " Data: " + postData);
			return null;
		}
	}

	/**
	 * Open a http connection to the specified URL, set up to process GET or POST messages.
	 * @param urlObj the url host to connect to
	 * @param requestMethod the request method, like <code>"GET"</code>, <code>"POST"</code>
	 */
	private HttpURLConnection initHttpConnection(final String urlString, final String requestMethod ) throws IOException {

		HttpURLConnection returnCon = null;

		URL url;
		try {
			url = new URL( urlString );
		}
		catch (final MalformedURLException e) {
			System.err.println("MalformedURLException = " +  e );
			System.err.println(e);
			url = null;
		}

		if (url != null) {
			final HttpURLConnection httpCon = (HttpURLConnection)url.openConnection();
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

	/** Generate an identification String for this user agent. */
	private String getUserAgentIdentification(){
		return "SupClient/";
	}
}

