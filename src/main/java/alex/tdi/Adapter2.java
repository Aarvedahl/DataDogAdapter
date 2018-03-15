package alex.tdi;

import alex.tdi.dto.AccountDTO;
import alex.tdi.dto.ResponseDTO;
import alex.tdi.dto.ResultDTO;
import alex.tdi.utils.MySSLSocketFactory;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.security.KeyStore;


public class Adapter2 {

    public void demoGetRESTAPI(AccountDTO account, String url, String api_key, String app_key) {
        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;

        ResultDTO resultDTO = new ResultDTO();

        DefaultHttpClient httpClient = getNewHttpClient();
        try
        {
            //Define a HttpGet request; You can choose between HttpPost, HttpDelete or HttpPut also.
            HttpGet getRequest = new HttpGet(url);

            //Set the API media type in http accept header
            getRequest.addHeader("accept", "application/json");

            //Send the request; It will immediately return the response in HttpResponse object
            HttpResponse response = httpClient.execute(getRequest);

            //verify the valid error code first
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                resultDTO.setSuccessful(false);
                resultDTO.setResultcode(Integer.toString(statusCode));
                throw new RuntimeException("Failed with HTTP error code : " + statusCode);
            }

            //Now pull back the response object
            HttpEntity httpEntity = response.getEntity();
            String apiOutput = EntityUtils.toString(httpEntity);
            resultDTO.setSuccessful(true);
            resultDTO.setResultcode(Integer.toString(statusCode));
            resultDTO.setResultJSON(apiOutput);

            //Lets see what we got from API
            System.out.println("Output from the API");
            System.out.println(apiOutput); //<user id="10"><firstName>demo</firstName><lastName>user</lastName></user>

            Gson gson = new Gson();
            ResponseDTO respObj = gson.fromJson(apiOutput,ResponseDTO.class);
            resultDTO.setResponseDTO(respObj);


            //Verify the populated object
            System.out.println(respObj.user.handle);
            System.out.println(respObj.user.name);
            System.out.println(respObj.user.access_role);
        } catch (Exception e) {
            // Catch exception
            System.out.println(e.getLocalizedMessage());
        } finally {
            //Important: Close the connect
            httpClient.getConnectionManager().shutdown();
        }
    }

    public DefaultHttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    public ResultDTO get() {
        ResultDTO resultDTO = new ResultDTO();

        try {
            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build());
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(
                    sslsf).build();

            HttpGet httpGet = new HttpGet("https://app.datadoghq.com/api/v1/user/alex.a1@enfo.org?api_key=9e4f84af430650a9780421d1841b8d8f&application_key=8d3de17fa2755953a8e733553e418ddfcca5571e");
            CloseableHttpResponse response = httpclient.execute(httpGet);

            //verify the valid error code first
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                resultDTO.setSuccessful(false);
                resultDTO.setResultcode(Integer.toString(statusCode));
                throw new RuntimeException("Failed with HTTP error code : " + statusCode);
            }

            //Now pull back the response object
            HttpEntity httpEntity = response.getEntity();
            String apiOutput = EntityUtils.toString(httpEntity);
            resultDTO.setSuccessful(true);
            resultDTO.setResultcode(Integer.toString(statusCode));
            resultDTO.setResultJSON(apiOutput);


            //Lets see what we got from API
            System.out.println("Output from the API");
            System.out.println(apiOutput); //<user id="10"><firstName>demo</firstName><lastName>user</lastName></user>

            Gson gson = new Gson();
            ResponseDTO respObj = gson.fromJson(apiOutput,ResponseDTO.class);
            resultDTO.setResponseDTO(respObj);


            //Verify the populated object
            System.out.println(respObj.user.handle);
            System.out.println(respObj.user.name);
            System.out.println(respObj.user.access_role);



        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
            resultDTO.setResultJSON(e.getLocalizedMessage());
            resultDTO.setSuccessful(false);
            resultDTO.setResultcode("999");
        }
        return resultDTO;
    }
/*
    public static OkHttpClient getSSLClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            } };

            TrustManagerFactory trustManagerFactory = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init((KeyStore) null);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }

            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new DefaultHttpClient(sslSocketFactory, trustManager);
            OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager).build();
            return client;

        } catch (Exception e) {
            System.out.println("getSSLClient Exception:" + e);
            throw new RuntimeException(e);
        }
    } */
}
