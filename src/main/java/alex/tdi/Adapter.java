package alex.tdi;

import alex.tdi.dto.AccountDTO;
import alex.tdi.dto.ResponseDTO;
import alex.tdi.dto.ResultDTO;
import alex.tdi.utils.OkHttpHelper;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import okhttp3.*;
import org.apache.log4j.Logger;

public class Adapter {

    OkHttpClient client = OkHttpHelper.getSSLClient();

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    final static Logger logger = Logger.getLogger(Adapter.class);


    public void testLogger() {
        logger.info("Is this log showing to you!?");
        logger.info("Is this log showing to you!?");
        logger.info("Is this log showing to you!?");
        logger.debug("Testing DEBUG level");
    }

    // Eventuellt getter och setter för api och app key, om de är tomma så kan vi skriva ut det i loggen
    // Eller så skickar man med det i varje metod anrop

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
                .get()
              //  .post(RequestBody.create(JSON, jsonBody))
              //  .addHeader("Authorization", authorizationHeader)
             //   .addHeader("Content-Type", "application/json")
            //    .addHeader("Accept", "application/json")
            //    .addHeader("Cache-Control", "no-cache")
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
                    logger.debug("New account handle=" + respobj.handle);
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


    public ResultDTO getAccount(String email, String authorizationHeader, String url, String reconnectAttemptsStr, String reconnectTimeStr) {
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

        logger.debug("Get Account Request To Datadog");

        Request request = new Request.Builder()
                .url(url)
                .get()
                //  .addHeader("Authorization", authorizationHeader)
             //   .addHeader("Accept", "application/json")
              //  .addHeader("Cache-Control", "no-cache")
                .build();

        while (keep_going) {
            // REST call
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
                    // Get new id from response JSON
                    ResponseDTO respobj = jsonAdapter2.fromJson(responseJSON);
                    logger.debug("Get account handle=" + respobj.handle);
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

}
