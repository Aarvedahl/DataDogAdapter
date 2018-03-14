package alex.tdi.dto;

import com.google.api.client.util.Key;

public class AccountDTO {

    @Key
    public String handle;

    @Key
    public String name;

    @Key
    public String access_role;

    @Key
    public String email;

    @Key
    public boolean disabled;

}
