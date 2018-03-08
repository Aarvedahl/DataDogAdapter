package alex.tdi.test;

import alex.tdi.Adapter;
import alex.tdi.dto.AccountDTO;
import alex.tdi.dto.ResponseDTO;
import alex.tdi.dto.ResultDTO;

public class AdapterTest {


    public static void main(String[] args) {
        Adapter client = new Adapter();

        testAddAccount(client);

    }

    private static void testAddAccount(Adapter client) {
        AccountDTO account = new AccountDTO();
        account.handle = "test@hqv.com";
        account.name = "Alex Test";
        account.access_role = "st";
        account.email = "test@hqv.com";
        account.disabled = false;

        String url = "https://app.datadoghq.com/api/v1/user?api_key=41c14834bf1243205b83846e98be8a64&application_key=8d3de17fa2755953a8e733553e418ddfcca5571e";
        ResultDTO result = client.accountAdd(account, "xxx", url, "4", "3");
        System.out.println("add result=" + result.getResultcode());
        ResponseDTO respobj = (ResponseDTO) result.getResponseDTO();
        System.out.println("new handle=" + respobj.handle);
        System.out.println( "result JSON=" + result.getResultJSON());
    }

}
