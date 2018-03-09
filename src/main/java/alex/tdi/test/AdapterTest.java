package alex.tdi.test;

import alex.tdi.Adapter;
import alex.tdi.dto.AccountDTO;
import alex.tdi.dto.ResponseDTO;
import alex.tdi.dto.ResultDTO;
import org.apache.log4j.BasicConfigurator;

public class AdapterTest {


    public static void main(String[] args) {
        BasicConfigurator.configure();
        Adapter client = new Adapter();

        //testAddAccount(client);
        testGetAccount(client);
    }



    private static void testAddAccount(Adapter client) {
        AccountDTO account = new AccountDTO();
        account.handle = "wildturtle@datadog.org";
        account.name = "Jen Mij";
        account.access_role = "st";
        account.email = "wildturtle@datadog.org";
        account.disabled = false;

        String url = "https://app.datadoghq.com/api/v1/user?api_key=41c14834bf1243205b83846e98be8a64&application_key=8d3de17fa2755953a8e733553e418ddfcca5571e";
        ResultDTO result = client.addAccount(account, url, "4", "3");
        System.out.println("add result=" + result.getResultcode());
        ResponseDTO respobj = (ResponseDTO) result.getResponseDTO();
        System.out.println("new handle=" + respobj.user.handle);
        System.out.println("Name of the user =" + respobj.user.name);
        System.out.println( "result JSON=" + result.getResultJSON());
    }


    // TODO Behöver testa med felaktiga uppgifter
    private static void testGetAccount(Adapter client) {
        ResultDTO resultDTO = client.getAccount("jason.tran@datadog.org", "https://app.datadoghq.com/api/v1/user/", "4" , "3", "9e4f84af430650a9780421d1841b8d8f", "8d3de17fa2755953a8e733553e418ddfcca5571e");
        System.out.println("get user result=" + resultDTO.getResultcode());
        ResponseDTO respobj = (ResponseDTO) resultDTO.getResponseDTO();
        System.out.println("handle of the user=" + respobj.user.handle);
        System.out.println("Email of the user=" + respobj.user.email);
        System.out.println( "result JSON=" + resultDTO.getResultJSON());
    }

}
