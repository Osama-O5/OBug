package com;

import com.github.osamaKhaled.obug.obug;
import org.testng.annotations.Test;


public class ALM extends obug {

    @Test
    public static void RegressionPack() throws Exception {
        obug.Caps("webdriver.chrome.driver", "\\drivers\\chromedriver.exe");
        obug.Login("EG_aalaa2", "ahly@myVFc0re");
        obug.URlQuery("Enterprise_prod_and_serv", "mCare_core", "", "", "", "'Major'", "", "", ">='2020-06-8'");
        obug.fullRegressionPack("Regression");
    }
}