package alex.tdi.test;

import alex.tdi.Adapter2;
import alex.tdi.dto.AccountDTO;
import alex.tdi.dto.ResultDTO;
import org.apache.log4j.BasicConfigurator;

public class AdapterTest {

    private static String api_key = "9e4f84af430650a9780421d1841b8d8f";
    private static String app_key = "8d3de17fa2755953a8e733553e418ddfcca5571e";

    public static void main(String[] args) {
        BasicConfigurator.configure();
        Adapter2 adapter2 = new Adapter2();
        testGet(adapter2);

        //  testAdd(adapter2);
        //testUpdate(adapter2);
        // testDisable(adapter2);
        //testRestore(adapter2);
    }

    private static void testGet(Adapter2 adapter2) {
        AccountDTO account = new AccountDTO();
        account.handle = "alex.a3@enfo.org";
        String dogUrl = "http://app.datadoghq.com/api/v1/user/";

        ResultDTO resultDTO = adapter2.getAccount(account, dogUrl, api_key, app_key);
        System.out.println("Access role:" + resultDTO.getResponseDTO().user.access_role);
        System.out.println("Handle of the user:" + resultDTO.getResponseDTO().user.handle);
        System.out.println("Name of the user:" + resultDTO.getResponseDTO().user.name);
    }


    private static void testAdd(Adapter2 adapter2) {
        AccountDTO account = new AccountDTO();
        account.handle = "alex.a3@enfo.org";
        account.name = "Alex A31";
        account.access_role = "adm";
        account.email = "alex.a3@enfo.org";
        account.disabled = false;

        String url = "https://app.datadoghq.com/api/v1/user";
        ResultDTO resultDTO = adapter2.addAccount(account, url, api_key, app_key);
        System.out.println("Access role:" + resultDTO.getResponseDTO().user.access_role);
        System.out.println("Handle of the user:" + resultDTO.getResponseDTO().user.handle);
        System.out.println("Name of the user:" + resultDTO.getResponseDTO().user.name);
    }


    private static void testUpdate(Adapter2 adapter2) {
        AccountDTO account = new AccountDTO();
        account.handle = "alex.arvedahl@gmail.com";
        account.name = "Alex Aaa";
        account.access_role = "st";
        account.email = "alex.arvedahl@gmail.com";

        String url = "https://app.datadoghq.com/api/v1/user/";
        ResultDTO resultDTO = adapter2.updateAccount(account, url, api_key, app_key);
        System.out.println("Updated access role:" + resultDTO.getResponseDTO().user.access_role);
        System.out.println("Updated handle of the user:" + resultDTO.getResponseDTO().user.handle);
        System.out.println("Updated name of the user:" + resultDTO.getResponseDTO().user.name);
    }


    private static void testDisable(Adapter2 adapter2) {
        AccountDTO account = new AccountDTO();
        account.handle = "alex.a1@enfo.org";

        String url = "https://app.datadoghq.com/api/v1/user/";
        ResultDTO resultDTO = adapter2.disableAccount(account, url, api_key, app_key);
        if (resultDTO.getResponseDTO().message != null) {
            System.out.println("Get Message: " + resultDTO.getResponseDTO().message);
        } else {
            System.out.println("Error message: " + resultDTO.getResponseDTO().errors.get(0));
        }
    }


    private static void testRestore(Adapter2 adapter2) {
        AccountDTO account = new AccountDTO();
        account.handle = "alex.a1@enfo.org";
        account.name = "Alex A1";

        String url = "https://app.datadoghq.com/api/v1/user/";
        ResultDTO resultDTO = adapter2.updateAccount(account, url, api_key, app_key);
        System.out.println("Updated access role:" + resultDTO.getResponseDTO().user.access_role);
        System.out.println("Updated handle of the user:" + resultDTO.getResponseDTO().user.handle);
        System.out.println("Updated name of the user:" + resultDTO.getResponseDTO().user.name);
        System.out.println("User disabled:" + resultDTO.getResponseDTO().user.disabled);
    }

}
