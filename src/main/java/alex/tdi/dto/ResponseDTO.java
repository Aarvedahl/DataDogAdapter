package alex.tdi.dto;


import java.util.ArrayList;
import java.util.List;

public class ResponseDTO{

    public User user;

    public String message;

    public List<String> errors = new ArrayList<String>();
}
