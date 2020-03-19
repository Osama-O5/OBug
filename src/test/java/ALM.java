
import org.testng.annotations.Test;

import java.io.*;



public class ALM extends obug {

    @Test
    public static void RegressionPack() throws InterruptedException, IOException {
        obug.Caps("webdriver.chrome.driver", "/drivers/chromedriver");
        obug.Login("EG_aalaa2", "Ahly@1993");
        URlQuery("Enterprise_prod_and_serv", "mCare_core", "", "", "", "", "", "", ">='2020-02-12'");
        obug.Report();
    }
}