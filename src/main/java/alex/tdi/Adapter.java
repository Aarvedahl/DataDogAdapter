package alex.tdi;

import alex.tdi.dto.AccountDTO;
import alex.tdi.dto.ResponseDTO;
import alex.tdi.dto.ResultDTO;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;

public class Adapter {

   // OkHttpClient client = OkHttpHelper.getSSLClient();

   // public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
  //  final static Logger logger = Logger.getLogger(Adapter.class);

    // TODO En klass för ISIM Användaren
    // TODO Ev. att vi behöver en metod för att kunna restorea en användare
    // TODO Behöver vi en metod för att kunna tolka strängar och ta bort "" från varje argument?
    // Eventuellt att vi behöver inkludera authorizationheader för vidare authorization

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

    HttpRequestFactory requestFactory =
            HTTP_TRANSPORT.createRequestFactory(new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest request) {
                    request.setParser(new JsonObjectParser(JSON_FACTORY));
                }
            });

    public ResultDTO getAccount(AccountDTO account, String url, String api_key, String app_key) {
        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;

        ResultDTO resultDTO = new ResultDTO();
        DataDogUrl dogUrl = new DataDogUrl(url);
        try {
            HttpRequest request = requestFactory.buildGetRequest(dogUrl);
            ResponseDTO responseDTO = request.execute().parseAs(ResponseDTO.class);
            resultDTO.setResponseDTO(responseDTO);
            return  resultDTO;
        } catch (IOException e) {
            e.printStackTrace();
            return resultDTO;
        }
    }
    // User

    // User add
   /*
    public ResultDTO addAccount(AccountDTO account, String url, String reconnectAttemptsStr, String reconnectTimeStr, String api_key, String app_key) {
        // Base url = https://app.datadoghq.com/api/v1/user
        url += "?api_key=" + api_key;
        url += "&application_key=" + app_key;

        ResultDTO result = new ResultDTO();
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<AccountDTO> jsonAdapter = moshi.adapter(AccountDTO.class);
        String jsonBody = jsonAdapter.toJson(account);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, jsonBody))
                //  .addHeader("Authorization", authorizationHeader)
                //  .addHeader("Content-Type", "application/json")
                //  .addHeader("Accept", "application/json")
                //  .addHeader("Cache-Control", "no-cache")
                .build();
        result = makeRequest("Add", request, result, reconnectAttemptsStr, reconnectTimeStr);
        return result;
    }

    public ResultDTO changeAccount(AccountDTO account, String url, String reconnectAttemptsStr, String reconnectTimeStr, String api_key, String app_key) {
        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;

        ResultDTO result = new ResultDTO();
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<AccountDTO> jsonAdapter = moshi.adapter(AccountDTO.class);
        String jsonBody = jsonAdapter.toJson(account);

        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(JSON, jsonBody))
                .build();
        result = makeRequest("Modify", request, result, reconnectAttemptsStr, reconnectTimeStr);
        return result;
    }

    public ResultDTO getAccount(AccountDTO account, String url, String reconnectAttemptsStr, String reconnectTimeStr, String api_key, String app_key) {
        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;

        ResultDTO result = new ResultDTO();


        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        result = makeRequest("Get", request, result, reconnectAttemptsStr, reconnectTimeStr);

        return result;
    }


    private ResultDTO handleResponse(String method, Response response, ResultDTO result, JsonAdapter jsonAdapter) throws IOException {
        String responseJSON = response.body().string();
        logger.debug(method + " Account response code=" + response.code());
        logger.debug(method + " Account response isSuccessful()=" + response.isSuccessful());
        if (!response.isSuccessful()) {
            return failedReq(method, result, response, responseJSON);
        } else {
            // Get user info from user property within response JSON
            ResponseDTO respobj = (ResponseDTO) jsonAdapter.fromJson(responseJSON);
            return successReq(method, result, response, respobj, responseJSON);
        }
    }

    private int reconnectTime(String reconnectTimeStr) {
        int reconnectTime = 1;
        if (reconnectTimeStr != null && !reconnectTimeStr.equals(""))
            reconnectTime = Integer.parseInt(reconnectTimeStr);
        return reconnectTime;
    }

    private int reconnectAttempts(String reconnectAttemptsStr) {
        int reconnectAttempts = 1;
        if (reconnectAttemptsStr != null && !reconnectAttemptsStr.equals(""))
            reconnectAttempts = Integer.parseInt(reconnectAttemptsStr);
        return reconnectAttempts;
    }

    private ResultDTO successReq(String method, ResultDTO result, Response response, ResponseDTO respobj, String responseJSON) {
        logger.debug(method + " account handle=" + respobj.user.handle);
        logger.debug(method + " account response code=" + response.code());
        result.setSuccessful(true);
        result.setResultcode(Integer.toString(response.code()));
        result.setResponseDTO(respobj);
        result.setResultJSON(responseJSON);
        return result;
    }

    private ResultDTO failedReq(String method, ResultDTO result, Response response, String responseJSON) {
        result.setSuccessful(false);
        result.setResultJSON(responseJSON);
        result.setResultcode(Integer.toString(response.code()));
        logger.error(method + " Account:Failed Request response code=" + Integer.toString(response.code()));
        logger.error(method + " Account:Failed Request responseJSON=" + responseJSON);
        return result;
    }

    private ResultDTO makeRequest(String method, Request request, ResultDTO result, String reconnectAttemptsStr, String reconnectTimeStr) {
        boolean http_429 = true;
        int try_count = 1;
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ResponseDTO> jsonAdapter = moshi.adapter(ResponseDTO.class);

        while (http_429) {
            // Calling REST Service
            logger.debug(method + " Account Request To Datadog");
            try {
                Response response = client.newCall(request).execute();
                if (!Integer.toString(response.code()).equals("429")) {
                    http_429 = false;
                }
                result = handleResponse(method, response, result, jsonAdapter);
            } catch (Exception e) {
                logger.error(method + " account exception=" + e);
                result.setSuccessful(false);
                result.setResultcode(e.getMessage().toString());
                result.setResultJSON(e.getLocalizedMessage());
                http_429 = false;
            }

            try_count++;
            if (http_429 && try_count > reconnectAttempts(reconnectAttemptsStr)) {
                http_429 = false;
            }
            if (http_429) {
                try {
                    logger.warn(method + " account HTTP 429, wait and retry");
                    // wait for reconnectTime seconds
                    Thread.sleep(reconnectTime(reconnectTimeStr) * 1000); // sleep for reconnectTime seconds
                } catch (InterruptedException e) {
                    logger.warn(method + " account HTTP 429, wait, got interrupted!");
                }
            }
        }
        return result;
    }



    // User modify
    public ResultDTO modifyAccount(AccountDTO account, String url, String reconnectAttemptsStr, String reconnectTimeStr, String api_key, String app_key) {
        ResultDTO result = new ResultDTO();

        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;

        int reconnectAttempts = 1;
        int reconnectTime = 1;
        if (reconnectAttemptsStr != null && !reconnectAttemptsStr.equals(""))
            reconnectAttempts = Integer.parseInt(reconnectAttemptsStr);
        if (reconnectTimeStr != null && !reconnectTimeStr.equals(""))
            reconnectTime = Integer.parseInt(reconnectTimeStr);

        boolean keep_going = true;
        int try_count = 1;

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<AccountDTO> jsonAdapter = moshi.adapter(AccountDTO.class);
        JsonAdapter<ResponseDTO> jsonAdapter2 = moshi.adapter(ResponseDTO.class);

        String jsonBody = jsonAdapter.toJson(account);
        logger.debug("Modify Account JSON To Datadog=" + jsonBody);

        Request request = new Request.Builder()
                .url(url)
                .put(RequestBody.create(JSON, jsonBody))
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .build();

        while (keep_going) {
            // REST call
            try {
                Response response = client.newCall(request).execute();
                String responseJSON = response.body().string();
                if (!response.isSuccessful()) {
                    String code = Integer.toString(response.code());
                    if (!code.equals("429"))
                        keep_going = false;
                    result.setSuccessful(false);
                    result.setResultJSON(responseJSON);
                    result.setResultcode(Integer.toString(response.code()));
                    logger.error("Modify Account:Failed Request Response Code=" + Integer.toString(response.code()));
                    logger.error("Modify Account:Failed Request JSON Response = " + responseJSON);
                } else {
                    // Get user info from user property within response JSON
                    logger.debug("Modify account response code=" + response.code());
                    logger.debug("Modify account response isSuccessful()=" + response.isSuccessful());
                    ResponseDTO respobj = jsonAdapter2.fromJson(responseJSON);
                    logger.debug("Modify account handle=" + respobj.user.handle);
                    logger.debug("Modify account response code=" + response.code());
                    result.setSuccessful(true);
                    result.setResultcode(Integer.toString(response.code()));
                    result.setResponseDTO(respobj);
                    result.setResultJSON(responseJSON);
                    keep_going = false;
                }
            } catch (Exception e) {
                logger.error("Modify account exception=" + e);
                result.setSuccessful(false);
                result.setResultcode(e.getMessage().toString());
                result.setResultJSON(e.getLocalizedMessage());
                keep_going = false;
            }

            try_count++;
            if (keep_going && try_count > reconnectAttempts) {
                keep_going = false;
            }
            if (keep_going) {
                try {
                    logger.warn("Modify account HTTP 429, wait and retry");
                    // wait for reconnectTime seconds
                    Thread.sleep(reconnectTime * 1000); // sleep for reconnectTime seconds
                } catch (InterruptedException e) {
                    logger.warn("Modify account HTTP 429, wait, got interrupted!");
                }
            }
        }
        return result;
    }

    // User Disable
    public ResultDTO disableAccount(AccountDTO account, String url, String reconnectAttemptsStr, String reconnectTimeStr, String api_key, String app_key) {
        ResultDTO result = new ResultDTO();
        account.disabled = true;
        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;

        int reconnectAttempts = 1;
        int reconnectTime = 1;
        if (reconnectAttemptsStr != null && !reconnectAttemptsStr.equals(""))
            reconnectAttempts = Integer.parseInt(reconnectAttemptsStr);
        if (reconnectTimeStr != null && !reconnectTimeStr.equals(""))
            reconnectTime = Integer.parseInt(reconnectTimeStr);

        boolean keep_going = true;
        int try_count = 1;

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ResponseDTO> jsonAdapter2 = moshi.adapter(ResponseDTO.class);

        Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Accept", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .build();

        while (keep_going) {
            // REST call
            logger.debug("Disable Account Handle To Datadog=" + account.handle);
            try {
                Response response = client.newCall(request).execute();
                String responseJSON = response.body().string();
                if (!response.isSuccessful()) {
                    String code = Integer.toString(response.code());
                    if (!code.equals("429"))
                        keep_going = false;
                    result.setSuccessful(false);
                    result.setResultJSON(responseJSON);
                    result.setResultcode(Integer.toString(response.code()));
                    logger.error("Disable Account:Failed Request Response Code=" + Integer.toString(response.code()));
                    logger.error("Disable Account:Failed Request JSON Response = " + responseJSON);
                } else {
                    // Get message from response JSON
                    logger.debug("Disable account response code=" + response.code());
                    logger.debug("Disable account response isSuccessful()=" + response.isSuccessful());
                    ResponseDTO respobj = jsonAdapter2.fromJson(responseJSON);
                    logger.debug("Disable account message=" + respobj.message);
                    logger.debug("Disable account response code=" + response.code());
                    result.setSuccessful(true);
                    result.setResultcode(Integer.toString(response.code()));
                    result.setResponseDTO(respobj);
                    result.setResultJSON(responseJSON);
                    keep_going = false;
                }
            } catch (Exception e) {
                logger.error("Disable account exception=" + e);
                result.setSuccessful(false);
                result.setResultcode(e.getMessage().toString());
                result.setResultJSON(e.getLocalizedMessage());
                keep_going = false;
            }

            try_count++;
            if (keep_going && try_count > reconnectAttempts) {
                keep_going = false;
            }
            if (keep_going) {
                try {
                    logger.warn("Disable account HTTP 429, wait and retry");
                    // wait for reconnectTime seconds
                    Thread.sleep(reconnectTime * 1000); // sleep for reconnectTime seconds
                } catch (InterruptedException e) {
                    logger.warn("Disable account HTTP 429, wait, got interrupted!");
                }
            }
        }
        return result;
    }
     */


}
