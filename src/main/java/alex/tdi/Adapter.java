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

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class Adapter {

    public ResultDTO restoreAccount(AccountDTO account, String url, String api_key, String app_key) {
        account.disabled = false;
        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;
        ResultDTO resultDTO = new ResultDTO();
        HttpPut putRequest = new HttpPut(url);

        //Set the API media type in http Content-Type header
        putRequest.addHeader("Content-Type", "application/json");

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

        //Set the API media type in http Content-Type header
        putRequest.addHeader("Content-Type", "application/json");

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

        //Set the API media type in http Content-Type header
        postRequest.addHeader("Content-Type", "application/json");

        //Set the request post body
        Gson gson = new Gson();
        String json = gson.toJson(account);
        StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
        postRequest.setEntity(entity);

        return makeRequest(resultDTO, postRequest);
    }

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
        return HttpClients.custom().setSSLSocketFactory(sslsf).build();
    }

    private ResultDTO makeRequest(ResultDTO resultDTO, HttpUriRequest request) {
        try {
            Gson gson = new Gson();
            CloseableHttpResponse response = handleStatusCode(request);
            int statusCode = response.getStatusLine().getStatusCode();

            //Now receive the response object
            HttpEntity httpEntity = response.getEntity();
            String apiOutput = EntityUtils.toString(httpEntity);

            //Set results from the API
            ResponseDTO respObj = gson.fromJson(apiOutput, ResponseDTO.class);
            resultDTO.setResponseDTO(respObj);
            resultDTO.setSuccessful(true);
            resultDTO.setResultcode(Integer.toString(statusCode));
            resultDTO.setResultJSON(apiOutput);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
            resultDTO.setResultJSON("Error:" + e.getLocalizedMessage());
            resultDTO.setSuccessful(false);
        }
        return resultDTO;
    }

    private CloseableHttpResponse handleStatusCode(HttpUriRequest request) throws InterruptedException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        int try_count = 1;
        boolean http_409 = false;

        //Send the request; It will immediately return the response in HttpResponse object if any
        CloseableHttpResponse response = getSSL().execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == 408 || statusCode == 409) {
            http_409 = true;
        }

        // If status code is 408 or 409, sleep for 1 sec and try again
        while (http_409) {
            if (statusCode != 408 && statusCode != 409) {
                break;
            }
            if (try_count >= 3) {
                break;
            }
            Thread.sleep(1000);
            response = getSSL().execute(request);
            statusCode = response.getStatusLine().getStatusCode();
            try_count++;
        }

        //If there is not a valid status code, throw exception
        if (statusCode != 201 && statusCode != 200) {
            if (!request.getMethod().equals("DELETE") && statusCode != 400) {
                throw new RuntimeException("Failed with HTTP error code : " + statusCode);
            }
        }
        return response;
    }

}
