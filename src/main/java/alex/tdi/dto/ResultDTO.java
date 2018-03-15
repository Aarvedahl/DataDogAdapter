package alex.tdi.dto;

public class ResultDTO {

    String resultcode;
    ResponseDTO responseDTO;
    String resultJSON;
    boolean isSuccessful;

    public String getResultcode() { return resultcode; }
    public void setResultcode(String resultcode) { this.resultcode = resultcode; }
    public ResponseDTO getResponseDTO() { return responseDTO; }
    public void setResponseDTO(ResponseDTO responseDTO) { this.responseDTO = responseDTO; }
    public String getResultJSON() { return resultJSON; }
    public void setResultJSON(String resultJSON) { this.resultJSON = resultJSON; }
    public boolean isSuccessful() { return isSuccessful; }
    public void setSuccessful(boolean successful) { isSuccessful = successful; }
}
