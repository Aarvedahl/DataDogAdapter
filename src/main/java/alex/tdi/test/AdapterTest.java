package alex.tdi.test;

import alex.tdi.Adapter;
import alex.tdi.Adapter2;
import alex.tdi.dto.AccountDTO;
import alex.tdi.dto.ResponseDTO;
import alex.tdi.dto.ResultDTO;
import org.apache.log4j.BasicConfigurator;

public class AdapterTest {


    private static String api_key = "9e4f84af430650a9780421d1841b8d8f";
    private static String app_key = "8d3de17fa2755953a8e733553e418ddfcca5571e";

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Adapter2 adapter2 = new Adapter2();
        testGet(adapter2);

       // testPost(adapter2);
       // testGetAccount(adapter2);
       //  testAddAccount(client);
       // testGetAccount(client);
       //  testModifyAccount(client);
       //  testDisableAccount(client);
    }

    private static void testGet(Adapter2 adapter2) {
        AccountDTO account = new AccountDTO();
        account.handle = "alex.a3@enfo.org";
        String dogUrl = "http://app.datadoghq.com/api/v1/user/";
        ResultDTO resultDTO = adapter2.getAccount(account, dogUrl, api_key, app_key);
        System.out.println(resultDTO.getResponseDTO().user.access_role);
        System.out.println(resultDTO.getResponseDTO().user.handle);
    }

    private static void testPost(Adapter2 adapter2) {
        AccountDTO account = new AccountDTO();
        account.handle = "alex.a3@enfo.org";
        account.name = "Alex A3";
        account.access_role = "adm";
        account.email = "alex.a3@enfo.org";
        account.disabled = false;

        String url = "https://app.datadoghq.com/api/v1/user";
        adapter2.addAccount(account, url, api_key, app_key);
    }

    private static void testGetAccount(Adapter2 adapter2) {
        AccountDTO account = new AccountDTO();
        account.handle = "alex.a1@enfo.org";
        account.name = "Alex A1";
        account.access_role = "st";
        account.email = "alex.a1@enfo.org";
        account.disabled = false;

        String dogUrl = "http://app.datadoghq.com/api/v1/user/";
        //adapter2.demoGetRESTAPI(account, dogUrl, api_key, app_key);
        adapter2.getAccount(account, dogUrl, api_key, app_key);
    }

     /*
    private static void testAddAccount(Adapter client) {
        AccountDTO account = new AccountDTO();
        account.handle = "alex.a2@enfo.org";
        account.name = "Alex A2";
        account.access_role = "st";
        account.email = "alex.a2@enfo.org";
        account.disabled = false;

        String url = "https://app.datadoghq.com/api/v1/user";
        ResultDTO result = client.addAccount(account, url, api_key, app_key);
        ResponseDTO respobj = (ResponseDTO) result.getResponseDTO();
        System.out.println("New handle=" + respobj.user.handle);
        System.out.println("Name of the user =" + respobj.user.name);
    }


    private static void testGetAccount(Adapter client) {
        AccountDTO account = new AccountDTO();
        account.handle = "alex.a1@enfo.org";
        account.name = "Alex A1";
        account.access_role = "st";
        account.email = "alex.a1@enfo.org";
        account.disabled = false;

        String dogUrl = "http://app.datadoghq.com/api/v1/user/";
        ResultDTO resultDTO = client.getAccount(account, dogUrl, api_key, app_key);
        System.out.println("Get user result=" + resultDTO.getResultcode());
        ResponseDTO respobj = (ResponseDTO) resultDTO.getResponseDTO();
        System.out.println("handle of the user=" + respobj.user.handle);
        System.out.println("Email of the user=" + respobj.user.email);
    }

    private static void testGetAccount(Adapter client) {
        AccountDTO account = new AccountDTO();
        account.handle = "alex.a1@enfo.org";
        account.name = "Alex A1";
        account.access_role = "st";
        account.email = "alex.a1@enfo.org";
        account.disabled = false;

        ResultDTO resultDTO = client.getAccount(account, "https://app.datadoghq.com/api/v1/user/", "4", "3", api_key, app_key);
        System.out.println("get user result=" + resultDTO.getResultcode());
        ResponseDTO respobj = (ResponseDTO) resultDTO.getResponseDTO();
        System.out.println("handle of the user=" + respobj.user.handle);
        System.out.println("Email of the user=" + respobj.user.email);
        System.out.println("result JSON=" + resultDTO.getResultJSON());
    }


    private static void testModifyAccount(Adapter client) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.handle = "test@test.com";
        accountDTO.name = "Testie Test";
        accountDTO.email = "test@test.com";
        accountDTO.disabled = false;
        accountDTO.access_role = "ro";

        String url = "https://app.datadoghq.com/api/v1/user/";
        ResultDTO resultDTO = client.changeAccount(accountDTO, url, "4", "3", api_key, app_key);
        System.out.println("Modify user result=" + resultDTO.getResultcode());
        ResponseDTO respobj = (ResponseDTO) resultDTO.getResponseDTO();
        System.out.println("handle of the user=" + respobj.user.handle);
        System.out.println("Name of the user =" + respobj.user.name);
        System.out.println("Result JSON=" + resultDTO.getResultJSON());
    }


    private static void testDisableAccount(Adapter client) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.handle = "test@test.com";
        accountDTO.name = "Testie Test";
        accountDTO.email = "test@test.com";
        accountDTO.disabled = true;
        accountDTO.access_role = "ro";

        String url = "https://app.datadoghq.com/api/v1/user/";
        ResultDTO resultDTO = client.disableAccount(accountDTO, url, "4", "3", api_key, app_key);
        System.out.println("Disable user result=" + resultDTO.getResultcode());
        ResponseDTO responseDTO = (ResponseDTO) resultDTO.getResponseDTO();
        System.out.println("Response Message=" + responseDTO.message);
        System.out.println("Result JSON=" + resultDTO.getResultJSON());
    }
 */

}
