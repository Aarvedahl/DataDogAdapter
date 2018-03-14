package alex.tdi;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Data;
import com.google.api.client.util.Key;

/** URL for Datadog API. */
public class DataDogUrl extends GenericUrl {

    public DataDogUrl(String encodedUrl) {
        super(encodedUrl);
    }

   // @Key
   // public String fields;
}
