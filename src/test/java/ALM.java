
import org.testng.annotations.Test;

import java.io.*;



public class ALM extends obug {

    @Test
    public static void RegressionPack() throws Exception {
        obug.Caps("webdriver.chrome.driver", "\\drivers\\chromedriver.exe");
        obug.Login("EG_aalaa2", "ahly@myVFc0re");
        URlQuery("Enterprise_prod_and_serv", "mCare_core", "", "", "", "'Major'", "", "", ">='2020-04-12'");
        obug.ModulesReport();
        fullRegressionPack();
    }
}