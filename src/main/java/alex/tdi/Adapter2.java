package alex.tdi;

import alex.tdi.dto.AccountDTO;
import alex.tdi.dto.ResponseDTO;
import alex.tdi.dto.ResultDTO;
import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

//Post och Get fungerar som det skall i TDI
// TODO Inspektera SCIM adaptern och kolla hur dess DTO

public class Adapter2 {

    /*
    public void demoGetRESTAPI(AccountDTO account, String url, String api_key, String app_key) {
        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;

        ResultDTO resultDTO = new ResultDTO();

        DefaultHttpClient httpClient = getNewHttpClient();
        try {
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
            ResponseDTO respObj = gson.fromJson(apiOutput, ResponseDTO.class);
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
            ResponseDTO respObj = gson.fromJson(apiOutput, ResponseDTO.class);
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


    public void post(AccountDTO accountDTO) {

        ResultDTO resultDTO = new ResultDTO();
        try {

            //Define a postRequest request
            HttpPost postRequest = new HttpPost("https://app.datadoghq.com/api/v1/user?api_key=41c14834bf1243205b83846e98be8a64&application_key=8d3de17fa2755953a8e733553e418ddfcca5571e");

            //Set the API media type in http content-type header
            postRequest.addHeader("content-type", "application/json");

            //Set the request post body
            Gson gson = new Gson();
            String json = gson.toJson(accountDTO);
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            postRequest.setEntity(entity);

            SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
            CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

            //Send the request; It will immediately return the response in HttpResponse object if any
            CloseableHttpResponse response = httpclient.execute(postRequest);

            //verify the valid error code first
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 201 && statusCode != 200) {
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
            System.out.println(apiOutput);
            ResponseDTO respObj = gson.fromJson(apiOutput, ResponseDTO.class);
            resultDTO.setResponseDTO(respObj);


            //Verify the populated object
            System.out.println(respObj.user.handle);
            System.out.println(respObj.user.name);
            System.out.println(respObj.user.access_role);


        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            resultDTO.setResultJSON(e.getLocalizedMessage());
            resultDTO.setSuccessful(false);
            resultDTO.setResultcode("999");

        } finally {
            //Important: Close the connect
        }
    } */

    public ResultDTO restoreAccount(AccountDTO account, String url, String api_key, String app_key) {
        account.disabled = false;
        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;
        ResultDTO resultDTO = new ResultDTO();
        HttpPut putRequest = new HttpPut(url);

        //Set the API media type in http content-type header
        putRequest.addHeader("content-type", "application/json");
        //Set the request post body
        Gson gson = new Gson();
        String json = gson.toJson(account);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        putRequest.setEntity(entity);

        return makeRequest(resultDTO, putRequest);
    }

    public ResultDTO disableAccount(AccountDTO account, String url, String api_key, String app_key) {
        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;
        ResultDTO resultDTO = new ResultDTO();
        HttpDelete deleteRequest = new HttpDelete(url);

        return makeRequest(resultDTO, deleteRequest);
    }

    public ResultDTO updateAccount(AccountDTO account, String url, String api_key, String app_key) {
        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;
        ResultDTO resultDTO = new ResultDTO();
        HttpPut putRequest = new HttpPut(url);

        //Set the API media type in http content-type header
        putRequest.addHeader("content-type", "application/json");
        //Set the request post body
        Gson gson = new Gson();
        String json = gson.toJson(account);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        putRequest.setEntity(entity);

        return makeRequest(resultDTO, putRequest);
    }


    public ResultDTO addAccount(AccountDTO account, String url, String api_key, String app_key) {
        url += "?api_key=" + api_key;
        url += "&application_key=" + app_key;
        ResultDTO resultDTO = new ResultDTO();
        HttpPost postRequest = new HttpPost(url);

        //Set the API media type in http content-type header
        postRequest.addHeader("content-type", "application/json");
        //Set the request post body
        Gson gson = new Gson();
        String json = gson.toJson(account);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        postRequest.setEntity(entity);

        return makeRequest(resultDTO, postRequest);
    }

    // Put och delete kvar samt restore account
    public ResultDTO getAccount(AccountDTO account, String url, String api_key, String app_key) {
        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;
        ResultDTO result = new ResultDTO();
        HttpGet getRequest = new HttpGet(url);

        return makeRequest(result, getRequest);
    }


    private CloseableHttpClient getSSL() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        SSLContextBuilder builder = new SSLContextBuilder();
        builder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build());
        CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
        return httpclient;
    }


    private ResultDTO makeRequest(ResultDTO resultDTO, HttpUriRequest request) {
        try {
            Gson gson = new Gson();

            //Send the request; It will immediately return the response in HttpResponse object if any
            CloseableHttpResponse response = getSSL().execute(request);

            //Verify valid response code first
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 201 && statusCode != 200) {
                if(request.getMethod() != "DELETE" && statusCode != 400) {
                    throw new RuntimeException("Failed with HTTP error code : " + statusCode);
                }
            }

            //Now pull back the response object
            HttpEntity httpEntity = response.getEntity();
            String apiOutput = EntityUtils.toString(httpEntity);

            //Results from the API
            ResponseDTO respObj = gson.fromJson(apiOutput, ResponseDTO.class);
            resultDTO.setResponseDTO(respObj);
            resultDTO.setSuccessful(true);
            resultDTO.setResultcode(Integer.toString(statusCode));
            resultDTO.setResultJSON(apiOutput);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            resultDTO.setResultJSON(e.getLocalizedMessage());
            resultDTO.setSuccessful(false);
            resultDTO.setResultcode("999");
        }
        return resultDTO;
    }

}
