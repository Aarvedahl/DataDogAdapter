package alex.tdi;

import alex.tdi.dto.AccountDTO;
import alex.tdi.dto.ResponseDTO;
import alex.tdi.dto.ResultDTO;
import alex.tdi.utils.OkHttpHelper;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.*;
import org.apache.log4j.Logger;

import javax.xml.transform.Result;
import java.io.IOException;

public class Adapter {

    OkHttpClient client = OkHttpHelper.getSSLClient();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    final static Logger logger = Logger.getLogger(Adapter.class);

    // Eventuellt att vi behöver inkludera authorizationheader för vidare authorization
    // Se om vi kan bryta ut det i while loopen till en egen metod

    // User

    // User add
    public ResultDTO addAccount(AccountDTO account, String url, String reconnectAttemptsStr, String reconnectTimeStr) {

        ResultDTO result = new ResultDTO();

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
        logger.debug("AddAccount JSON To Datadog=" + jsonBody);

        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(JSON, jsonBody))
            //    .addHeader("Authorization", authorizationHeader)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .build();

        while (keep_going) {
            // REST call
            try  {
                Response response = client.newCall(request).execute();
                String responseJSON = response.body().string();
                if (!response.isSuccessful()) {
                    String c = Integer.toString(response.code());
                    if (!c.equals("429"))
                        keep_going = false;
                    result.setSuccessful(false);
                    result.setResultJSON(responseJSON);
                    result.setResultcode(Integer.toString(response.code()));
                    logger.error("New Account:Failed Request Response Code=" + Integer.toString(response.code()));
                    logger.error("New Account:Failed Request JSON Response = " + responseJSON);
                } else {
                    // Get new id from response JSON
                    logger.debug("New account response code=" + response.code());
                    logger.debug("New account response isSuccessful()=" + response.isSuccessful());
                    ResponseDTO respobj = jsonAdapter2.fromJson(responseJSON);
                    logger.debug("New account handle=" + respobj.user.handle);
                    logger.debug("New account response code=" + response.code());
                    result.setSuccessful(true);
                    result.setResultcode(Integer.toString(response.code()));
                    result.setResponseDTO(respobj);
                    result.setResultJSON(responseJSON);
                    keep_going = false;
                }
            } catch (Exception e) {
                logger.error("New account exception=" + e);
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
                    logger.warn("New account HTTP 429, wait and retry");
                    // wait for reconnectTime seconds
                    Thread.sleep(reconnectTime * 1000); // sleep for reconnectTime seconds
                } catch (InterruptedException e) {
                    logger.warn("New account HTTP 429, wait, got interrupted!");
                }
            }
        }
        return result;
    }

    public ResultDTO getAccount(AccountDTO account, String url, String reconnectAttemptsStr, String reconnectTimeStr, String api_key, String app_key) {
        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;

        ResultDTO result = new ResultDTO();
        int reconnectAttempts = reconnectAttempts(reconnectAttemptsStr);
        int reconnectTime = reconnectTime(reconnectTimeStr);

        boolean keep_going = true; // Byt namn på denna variabeln, denna är sann sålänge som vi inte får http 429,  "Reached Rate limit"
        int try_count = 1;

        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ResponseDTO> jsonAdapter = moshi.adapter(ResponseDTO.class);
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        while (keep_going) {
            // Man kanske kan bara kolla om det är en request code = 429, om det är det så fortsätter vi annars anropar vi metoderna
            // Calling REST Service
            logger.debug("Get Account Request To Datadog");
            try {
                Response response = client.newCall(request).execute();
                if(!Integer.toString(response.code()).equals("429")) {
                    keep_going = false;
                }
                result = handleResponse(response, result, jsonAdapter);
            } catch (Exception e) {
                logger.error("Get account exception=" + e);
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
                    logger.warn("Get account HTTP 429, wait and retry");
                    // wait for reconnectTime seconds
                    Thread.sleep(reconnectTime * 1000); // sleep for reconnectTime seconds
                } catch (InterruptedException e) {
                    logger.warn("Get account HTTP 429, wait, got interrupted!");
                }
            }
        }
        return result;
    }

    private ResultDTO handleResponse(Response response, ResultDTO result, JsonAdapter jsonAdapter) throws IOException {
        String responseJSON = null;
        responseJSON = response.body().string();
        logger.debug("Get Account response code=" + response.code());
        logger.debug("Get Account response isSuccessful()=" + response.isSuccessful());
        if (!response.isSuccessful()) {
          return failedReq("Get", result, response, responseJSON);
        } else {
            // Get user info from user property within response JSON
            ResponseDTO respobj = null;
            respobj = (ResponseDTO) jsonAdapter.fromJson(responseJSON);
            return succReq("Get", result, response, respobj, responseJSON);
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
    // TODO Kanske bättre att slå ihop dessa metoder
    private ResultDTO succReq(String method, ResultDTO result, Response response, ResponseDTO respobj, String responseJSON) {
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
        logger.error(method + "Get Account:Failed Request responseJSON=" + responseJSON);
        return result;
    }
/*
    public ResultDTO getAccount(AccountDTO account, String url, String reconnectAttemptsStr, String reconnectTimeStr, String api_key, String app_key) {
        url += account.handle + "?";
        url += "api_key=" + api_key;
        url += "&application_key=" + app_key;

        ResultDTO result = new ResultDTO();

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
                .get()
                .build();

        while (keep_going) {
            // REST call
            logger.debug("Get Account Request To Datadog");
            try {
                Response response = client.newCall(request).execute();
                String responseJSON = response.body().string();
                logger.debug("Get Account response code=" + response.code());
                logger.debug("Get Account response isSuccessful()=" + response.isSuccessful());
                if (!response.isSuccessful()) {
                    String c = Integer.toString(response.code());
                    if (!c.equals("429"))
                        keep_going = false;
                    result.setSuccessful(false);
                    result.setResultJSON(responseJSON);
                    result.setResultcode(Integer.toString(response.code()));
                    logger.error("Get Account:Failed Request response code=" + Integer.toString(response.code()));
                    logger.error("Get Account:Failed Request responseJSON=" + responseJSON);
                } else {
                    // Get user info from user property within response JSON
                    ResponseDTO respobj = jsonAdapter2.fromJson(responseJSON);
                    logger.debug("Get account handle=" + respobj.user.handle);
                    logger.debug("Get account response code=" + response.code());
                    result.setSuccessful(true);
                    result.setResultcode(Integer.toString(response.code()));
                    result.setResponseDTO(respobj);
                    result.setResultJSON(responseJSON);
                    keep_going = false;
                }
            } catch (Exception e) {
                logger.error("Get account exception=" + e);
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
                    logger.warn("Get account HTTP 429, wait and retry");
                    // wait for reconnectTime seconds
                    Thread.sleep(reconnectTime * 1000); // sleep for reconnectTime seconds
                } catch (InterruptedException e) {
                    logger.warn("Get account HTTP 429, wait, got interrupted!");
                }
            }
        }
        return result;
    }
*/

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
            try  {
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
            try  {
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

}
