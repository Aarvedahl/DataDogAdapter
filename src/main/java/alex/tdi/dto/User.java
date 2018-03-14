package alex.tdi.dto;

import com.google.api.client.util.Key;

public class User {

    @Key
    public boolean disabled;
    @Key
    public String handle;
    @Key
    public String name;
    @Key
    public boolean is_admin;
    @Key
    public String role;
    @Key
    public String access_role;
    @Key
    public boolean verified;
    @Key
    public String email;
    @Key
    public String icon;
}
