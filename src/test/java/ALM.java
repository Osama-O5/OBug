
import org.testng.annotations.Test;

import java.io.*;



public class ALM extends BaseTest {

    @Test
    public static void RegressionPack() throws InterruptedException, IOException {
        BaseTest.Caps("webdriver.chrome.driver", "C:\\Users\\MetwallyO\\Documents\\GitHub\\10\\Autonomous-Testing\\.idea\\chromedriver.exe");
        BaseTest.Login("EG_aalaa2", "Ahly@1993");
        URlQuery("Enterprise_prod_and_serv", "mCare_core", "", "", "", "", "", "", ">='2020-02-12'");
        BaseTest.Report();
    }
}