package com.github.osamaKhaled.obug;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.*;
import org.testng.annotations.*;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Main class to use Aeye library
 *
 * @author Osama Khaled
 * @author https://github.com/Osama-O5
 */
public class obug  {

    public static WebDriver driver;
    public static AndroidDriver<?> appiumDriver;
    static LinkedHashMap<String, Integer> sortedMap;
    public static Object[] Key;
    public static Object[] Value;
    public static Object[] array;
    public static Object Keys;
    public static Object Values;
    public static Object[] capKeys;
    public static Object[] capValues;
    public static Object arrays;
    public static String scripts;
    public static List<String> scriptsNames;
    public static List<String> Classes;
    public static List<Class> Classess;
    public static List<Class> Clas;
    public static List<String> modulesNames;
    public static List<String> separateModule;
    public static List<Object> scriptName;
    public static List<String> names;
    public static List<String> collect;
    public static List<String> pack;
    public static List<String> name;
    public static Object module;
    public static Object[] script;
    public static boolean matchFound;
    public static List<Boolean> matchStatus;
    public static String patternStr;
    public static String input;
    public static String naming;
    public static ITestResult testResult;
    public static ArrayList<Class> listWithoutDuplicatesinClass;
    public static String Platform;
    public static String PLATFORM_VERSION;
    public static String PLATFORM_NAME;
    public static String AUTOMATION_NAME;
    public static String DEVICE_NAME;
    public static String appActivity;
    public static String appPackage;
    public static String host;
    public static String scriptNa;
    public static ITestResult result;
    public static DesiredCapabilities capabilities;
    public static  Thread thread;

    String projectPath = System.getProperty("user.dir");

    /**
     * Setting up the browser capabilites
     *
     */
    public void Caps(String Driver, String Driver_Path) {
        DesiredCapabilities caps = new DesiredCapabilities();
        String path = System.getProperty("user.dir");
        String allurePath = path + "/Reports/allure-results";
        System.setProperty(Driver, path + Driver_Path);
       /* WebDriverManager.chromedriver().clearPreferences();
        WebDriverManager.chromedriver().setup();*/
        System.setProperty("https.protocols", "TLSv1.2");
//        System.setProperty(Driver , "allure.results.directory=/"+allurePath );
        driver = new ChromeDriver(caps);
        driver.manage().window().maximize();
        driver.get("https://alm.vodafone.com/qcbin/rest/is-authenticated?login-form-required=y");

    }

    /**
     * Setting up ALM Login access
     *
     */
    public void Login(String Username, String Password) throws Exception {
        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.elementToBeClickable(By.id("j_username")));
        WebElement User = driver.findElement(By.id("j_username"));
        WebElement Pass = driver.findElement(By.id("j_password"));
        WebElement Authenticate = driver.findElement(By.xpath("//*[contains(@type,'submit')] "));

        if (Authenticate.isDisplayed()) {
            User.sendKeys(Username);
            Pass.sendKeys(Password);
            Authenticate.click();
        } else {
        }
    }

    /**
     * Setting up the Defects Filtration
     *
     */
    public void URlQuery(String Domain_Name, String Project_Name, String Status, String Device_Type, String Priority, String Severity, String Detected_by, String Component_name, String Creation_Time) throws IOException, InterruptedException {
        driver.navigate().to("https://alm.vodafone.com/qcbin/rest/domains/" + Domain_Name + "/projects/" + Project_Name + "/defects?query={status[" + Status + "];user-21[" + Device_Type + "];priority[" + Priority + "];severity[" + Severity + "];detected-by[" + Detected_by + "];user-11[" + Component_name + "];creation-time[" + Creation_Time + "]}");
        list();
        ModulesReport();
    }


    private static void countFrequencies(ArrayList<String> list) {

        Map<String, Integer> Counter = new HashMap<String, Integer>();

        for (String i : list) {
            Integer j = Counter.get(i);
            Counter.put(i, (j == null) ? 1 : j + 1);
        }
        obug.sortedMap = new LinkedHashMap<>();
        Counter.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));
    }

    private static void list() throws IOException, InterruptedException {
        List<WebElement> user11 = driver.findElements(By.xpath("//*[@Name='user-11']/Value"));
        ArrayList<String> list = new ArrayList<String>();
        for (WebElement Name : user11) {
            String module = Name.getAttribute("innerHTML");
            list.add(module);
        }

        obug.countFrequencies(list);
        driver.quit();
    }

    /**
     * Setting up Modules Report in a Pie Chart
     *
     */
    private static void ModulesReport() throws IOException, InterruptedException {
        String projectPath = System.getProperty("user.dir");
        try {
            Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c mkdir Reports");
            Thread.sleep(5000);
            Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c timeout /T 100");

            StringBuilder htmlStringBuilder = new StringBuilder();

            Key = sortedMap.keySet().toArray();
            Value = sortedMap.values().toArray();
            array = sortedMap.entrySet().toArray();
            StringBuilder paramCollection = new StringBuilder();

            modulesNames = new ArrayList<>();
            for (int i = 0; i < array.length; i++) {
                Keys = Key[i];
                Values = Value[i];
                arrays = array[i];

                modulesNames.add((String) Keys);

                String collect = "[ '" + Keys + "'  ,  " + Values + " ]";
                paramCollection.append(collect).append("\n");
                if (i < array.length - 1) {
                    paramCollection.append(",");
                }

            }
            htmlStringBuilder.append("<html>\n" +
                    "  <head>\n" +
                    "    <script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>\n" +
                    "    <script type=\"text/javascript\">\n" +
                    "      google.charts.load(\"current\", {packages:[\"corechart\"]});\n" +
                    "      google.charts.setOnLoadCallback(drawChart);\n" +
                    "      function drawChart() {\n" +
                    "        var data = google.visualization.arrayToDataTable([\n" +
                    "          ['Task', 'Hours per Day'],\n" +
                    "          " + paramCollection.toString() +
                    "        ]);\n" +
                    "\n" +
                    "        var options = {\n" +
                    "          title: 'Effected Modules',\n" +
                    "          is3D: true,\n" +
                    "        };\n" +
                    "\n" +
                    "        var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));\n" +
                    "        chart.draw(data, options);\n" +
                    "      }\n" +
                    "    </script>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <div id=\"piechart_3d\" style=\"width: 900px; height: 500px;\"></div>\n" +
                    "  </body>\n" +
                    "</html>");

            ExportReport(htmlStringBuilder.toString(), "Reports/Modules_Report.html");
            openModule_Report();
            boolean isWindows = System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");
            if (isWindows) {
                try {
                    Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c del LocationsList.json");
                    Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c rmdir /S /Q allure-results");
                    Runtime.getRuntime().exec("cmd.exe /c y");
                    Thread.sleep(30000);
                    Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c timeout /T 30");
                    Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c dir /b/s *.java >> LocationsList.json");
                    Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c timeout /T 70");
                    Thread.sleep(5000);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Runtime.getRuntime().exec("sh -c " + projectPath + " /c del LocationsList.json");
                Runtime.getRuntime().exec("sh -c " + projectPath + " /c dir /b/s *.java >> LocationsList.json");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Making Automatic Matching With The Scripts Names After Saving Them In Json File
     *
     */
    private static void AutomaticMatch() throws IOException, InterruptedException {
        Properties Pro =new Properties();
        String JsonName = "LocationsList";
        String projectPath = System.getProperty("user.dir");


        try {
            FileReader fr = new FileReader("" + projectPath + "/LocationsList.json");
            if (fr.equals(false)) {
                Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c del LocationsList.json");
                Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c rmdir /S /Q allure-results");
                Runtime.getRuntime().exec("y");

                Thread.sleep(30000);
                Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c timeout /T 30");
                Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c dir /b/s *.java >> LocationsList.json");
                Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c timeout /T 70");
                Thread.sleep(5000);
            }
            BufferedReader br = new BufferedReader(fr);
            String currentLine;
            scriptsNames = new ArrayList<>();
            String getJavaLine = null;
            while ((currentLine = br.readLine()) != null) {
                for (int i = 0; i < currentLine.length(); i++) {
                    getJavaLine = currentLine.substring(currentLine.lastIndexOf("\\") + 1);
                    scripts = getJavaLine.replace(".java", "");
                }
                scriptsNames.add(scripts);
                script = scriptsNames.toArray();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        separateModule = new ArrayList<>();
        Object[] modules = modulesNames.toArray();
        matchStatus = new ArrayList<>();
        names = new ArrayList<>();
        scriptName = new ArrayList<>();
        for (int y = 0; y < modules.length; y++) {
            module = modules[y];
            separateModule.add((String) module);
            if (scripts.length() != 0) {
                patternStr = scriptsNames.toString();
                Pattern pattern = Pattern.compile(patternStr);
                input = module.toString();
                Matcher matcher = pattern.matcher(input);
                matcher.reset(input);
                matchFound = matcher.lookingAt();
            }

            if (matchFound == true) {
                matchStatus.add(matchFound);
                names.add(input);
            }
        }
    }


    /**
     * Export The Modules Report
     *
     */
    private static void ExportReport(String fileContent, String fileName) throws IOException, InterruptedException {
        String projectPath = System.getProperty("user.dir");
        String tempFile = projectPath + File.separator + fileName;
        File file = new File(tempFile);
        // if file does exists, then delete and create a new file
        if (file.exists()) {
            try (FileWriter filee = new FileWriter("Reports/Modules_Report.html")) {
                filee.write(fileContent);
                filee.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //write to file with OutputStreamWriter
        OutputStream outputStream = new FileOutputStream(file.getAbsoluteFile());
        Writer writer = new OutputStreamWriter(outputStream);
        writer.write(fileContent);
        writer.close();
    }

    /**
     * Open The Modules Report Within Runtime
     *
     */
    private static void openModule_Report() throws IOException, InterruptedException {

        String command = "";
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();
        String projectPath = System.getProperty("user.dir");
        String Reportname = "Reports/Modules_Report.html";
        String tempFile = projectPath + File.separator + Reportname;
        if (isWindows) {
            Runtime.getRuntime().exec("cmd.exe /c start " + tempFile + "");
        } else {
            Runtime.getRuntime().exec("sh -c open " + tempFile + "");
        }
    }

    /**
     * Here after Selecting The Scripts to Run .. Create new suite includes the selected scripts to run
     *
     */
    @Test
    public void fullRegressionPack(String RegressionPackageName) throws Exception {
        AutomaticMatch();
        String projectPath = System.getProperty("user.dir");
        String w = "Reports\\ScreenShots";
        String tempFile = projectPath + File.separator + w;
        pack = new ArrayList<>();
        collect = new ArrayList<>();
        for (Object s : names) {
            scriptsNames.forEach(name2 -> {
                if (name2.contains(s.toString())) {
                    naming = String.valueOf(true);
                    pack.add(name2);
                }
            });
        }

        name = new ArrayList<>();
        name.addAll(pack);
        pack.clear();
        pack.addAll(name);

        for (String ClassName : pack) {
            StringBuilder builder = new StringBuilder();
            builder.append(ClassName).append(".java");
            String string = builder.toString();
            try {
                FileReader fr = new FileReader("" + projectPath + "/LocationsList.json");
                BufferedReader br = new BufferedReader(fr);
                String currentLine;
                scriptsNames = new ArrayList<>();
                Classes = new ArrayList<>();
                String getJavaLine = null;
                while ((currentLine = br.readLine()) != null) {
                    for (int i = 0; i < currentLine.length(); i++) {
                        getJavaLine = currentLine.substring(currentLine.lastIndexOf("\\") + 1);
                        scripts = getJavaLine.replace(".java", "");
                    }

                    if (getJavaLine.equals(string)) {
                        Classes.add(getJavaLine);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        TestListenerAdapter tla = new TestListenerAdapter();
        TestNG testNG = new TestNG();
        XmlSuite suite = new XmlSuite();
        // Creating a new Test
        XmlTest test = new XmlTest(suite);
        // New list for the classes
        List<XmlClass> classes = new ArrayList<XmlClass>();
        // New list for the Suites
        List<XmlSuite> suites = new ArrayList<XmlSuite>();

        Map<String, String> pram = new HashMap<>();
        pram.put("configfailurepolicy", "continue");
        suite.setParameters(pram);
        String lastNa;

        LinkedHashSet<String> hashSet = new LinkedHashSet<>(pack);
        LinkedHashSet<String> RegressionPackScripts = new LinkedHashSet<>();
        ArrayList<String> listWithoutDuplicates = new ArrayList<>(hashSet);
        for (String modle : listWithoutDuplicates) {
            int i = 0;

            // Set Test name
            test.setName(modle);

            try {
                FileReader fr = new FileReader("" + projectPath + "/LocationsList.json");
                BufferedReader br = new BufferedReader(fr);
                String currentLine;
                String one = "" + modle + "";
                String getJavaLine = null;
                while ((currentLine = br.readLine()) != null) {
                    for (int x = 0; x < currentLine.length(); x++) {
                        getJavaLine = currentLine.substring(currentLine.lastIndexOf("\\") + 1).replace(".java", "");
                        boolean line = getJavaLine.equals(one);
                        if (line == true) {
                            String curentLine = currentLine;
                            String packagee = curentLine.substring(curentLine.substring(0, curentLine.lastIndexOf("\\")).lastIndexOf("\\") + 1).replace("\\" + one + ".java", "");
                            // if there isn`t a package name
                            if (RegressionPackageName != "") {
                                if (packagee.equals(RegressionPackageName)) {
                                    Class firstonly = Class.forName("" + packagee + "." + one);
                                    // Putting the classes to the list
                                    classes.add(new XmlClass(firstonly));
                                    // Add classes to test
                                    test.setClasses(classes);
                                    for (String script : listWithoutDuplicates) {
                                        if (one.equals(script)){
                                            RegressionPackScripts.add(script);
                                        }
                                    }
                                }
                            }
                            else {
                                Class firstonly = Class.forName("" + packagee + "." + one);
                                // Putting the classes to the list
                                classes.add(new XmlClass(firstonly));
                                // Add classes to test
                                test.setClasses(classes);
                                for (String script : listWithoutDuplicates) {
                                    if (one.equals(script)){
                                        RegressionPackScripts.add(script);
                                    }
                                }
                            }
                        }
                        // call createTest method and pass the name of TestCase- Based on your requirement
                    }

                }

                i++;

             /*   testNG.setUseDefaultListeners(true);
                testNG.addListener((ITestNGListener) tla);*/
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Opss .. SomeThing Went Wrong");
            }

            Map<String, Object> capMap;
            capMap = capabilities.asMap();
            Map<String, String> cap = new HashMap<>();
            capKeys = capMap.keySet().toArray();
            capValues = capMap.values().toArray();
            Object key;
            Object value;
            for (int j = 0; j < capMap.size(); j++) {
                key = capKeys[j];
                value = capValues[j];
                cap.put(key.toString(), value.toString());
            }
            suite.setName("Full Regression Pack");
            suite.setParameters(cap);
        }
        if (RegressionPackScripts.size() != 0) {
            System.out.println("Regression Pack Contains :" + RegressionPackScripts + "");
            System.out.println("Please Wait ...");
            System.out.println("We Are Preparing Your Regression Pack .........");


            // Creating the xml
            suites.add(suite);
            testNG.setXmlSuites(suites);
            testNG.setConfigFailurePolicy(XmlSuite.FailurePolicy.CONTINUE);
            testNG.run();
      /*  tests.log(Status.INFO, "Test Steps");
//        for (int i = 0; i < tla.toString().length() ; i++) {
//            if (tla.toString().equals("passed")) {
                getResult((ITestResult) tla.getPassedTests());
//            } else if (tla.toString().equals("failed")) {
                getResult((ITestResult) tla.getFailedTests());
//            } else {
                getResult((ITestResult) tla.getSkippedTests());
//            }
//        }
        extent.flush();*/
        } else {
            System.out.println("Sorry .. We didn`t Found Scripts In Your Project Matches with The Affected Modules which we Found to Run");
        }
        
    }

    public static void criticalRegressionPack() {
    }

    @Parameters
    @BeforeSuite
    public void MobileCapability(String deviceName, String platform, String PLATFORM_VERSION, String AUTOMATION_NAME, String appActivity, String appPackage, String No_RESET) throws MalformedURLException {
        capabilities = new DesiredCapabilities();
        capabilities.setCapability("platform", platform);
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("PLATFORM_VERSION", PLATFORM_VERSION);
        capabilities.setCapability("AUTOMATION_NAME", AUTOMATION_NAME);
        capabilities.setCapability("appActivity", appActivity);
        capabilities.setCapability("appPackage", appPackage);
        capabilities.setCapability(MobileCapabilityType.NO_RESET, No_RESET);
        capabilities.setCapability("PLATFORM_NAME", "Android");
        capabilities.setCapability("build", "test");
        capabilities.setCapability("DEVICE_NAME", "samsung-s8");
        capabilities.setCapability("APPIUM_VERSION", "1.13");
        capabilities.setCapability("udid", "");
        capabilities.setCapability("bundleId", "");

        capabilities.setCapability("host", "http://127.0.0.1:4723/wd/hub");

        appiumDriver = new AndroidDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);

    }

  /*  @AfterMethod(alwaysRun = true)
    public static void getResult(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            tests.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + " Test case FAILED due to below issues:", ExtentColor.RED));
            tests.fail(result.getThrowable());
            String methodName = result.getMethod().getMethodName();
            String expectationMessage = Arrays.toString(result.getThrowable().getStackTrace());
            extentTest.get().fail("<details><summary><b><fontt color=red>" + "Expectation Occured , click to see details:" + "</font></summary>" + expectationMessage.replaceAll(",", "<br>") + "</details>\n");
            WebDriver driver = ((Test.obug) result.getInstance()).driver;
            String path = takescreenshot(driver, result.getMethod().getMethodName());
            try {
                extentTest.get().fail("<b><font color=red>" + "ScreenShot of failure" + "</font></b>", MediaEntityBuilder.createScreenCaptureFromPath(path).build());
            } catch (IOException E) {
                extentTest.get().fail("Test Failed , Cannot attach screenShot");
            }

            String logText = "<b>Test Method" + methodName + "Failed </b>";
            Markup m = MarkupHelper.createLabel(logText, ExtentColor.RED);
            extentTest.get().log(Status.FAIL, m);
        } else if (result.getStatus() == ITestResult.SUCCESS) {
//            tests.log(Status.PASS, MarkupHelper.createLabel(result.getName()+" Test Case PASSED", ExtentColor.GREEN));
            String logText = "<b> Test Method" + result.getMethod().getMethodName() + "Successfully </b>";
            Markup m = MarkupHelper.createLabel(logText, ExtentColor.GREEN);
            extentTest.get().log(Status.PASS, m);
        } else {
            tests.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + " Test Case SKIPPED", ExtentColor.ORANGE));
            tests.skip(result.getThrowable());
            String logText = "<b> Test Method" + result.getMethod().getMethodName() + "Skipped </b>";
            Markup m = MarkupHelper.createLabel(logText, ExtentColor.YELLOW);
            extentTest.get().log(Status.SKIP, m);
        }
    }

    public static String takescreenshot(WebDriver driver, String methodName) {
        String fileName = getScreenShots(methodName);
        String directory = System.getProperty("user.dir") + "/Reports/ScreenShots/";
        new File(directory).mkdirs();
        String path = directory + fileName;

        try {
            File screenshot = ((TakesScreenshot) Test.obug.driver).getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(screenshot, new File(path));
            System.out.println("************************");
            System.out.println("ScreenShot sorted at :" + path);
            System.out.println("************************");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    public static String getScreenShots(String methodName) {
        Date d = new Date();
        String fileName = methodName + "_" + d.toString().replace(":", "_").replace(" ", "_") + ".png";
        return fileName;
    }
*/
/*    @AfterTest
    public void tearDown() throws IOException, InterruptedException {
        extent.flush();
        openRegression_Report();
    }*/
  /*  @AfterClass()
    public void setResult(ITestResult result) throws Exception{
        if(result.getStatus() == ITestResult.FAILURE){
            tests.log(Status.FAIL, MarkupHelper.createLabel(result.getName() + "Test Case Failed", ExtentColor.RED));
            tests.fail(result.getThrowable());
            String temp = getScreenshot(driver);
            try {
                tests.fail(result.getThrowable().getMessage(), MediaEntityBuilder.createScreenCaptureFromPath(temp).build());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if  (result.getStatus()== ITestResult.SUCCESS){
            tests.log(Status.PASS, MarkupHelper.createLabel(result.getName() + "Test Case Passed", ExtentColor.GREEN));
        }
        else {
            tests.log(Status.SKIP, MarkupHelper.createLabel(result.getName() + "Test Case Skipped", ExtentColor.YELLOW));
        }

        extent.flush();
        openRegression_Report();

    }*/

/*    @AfterSuite(alwaysRun = true)
    public static void setResult() throws Exception {
        extent.flush();
//        openRegression_Report();
    }*/
   /* public static String getScreenshot(WebDriver driver) {
        TakesScreenshot ts = (TakesScreenshot) driver;

        File src = ts.getScreenshotAs(OutputType.FILE);

        String path = System.getProperty("user.dir") + "/Screenshots/" + System.currentTimeMillis() + ".png";

        File destination = new File(path);

        try {
            FileUtils.copyFile(src, destination);
        } catch (IOException e) {
            System.out.println("Capture Failed " + e.getMessage());
        }

        return path;
    }
*/



   @AfterSuite
   public void openRegression_Report() throws IOException {
       boolean isWindows = System.getProperty("os.name")
               .toLowerCase().startsWith("windows");
       ProcessBuilder builder = new ProcessBuilder();
       String projectPath = System.getProperty("user.dir");
//       String Reportname = "Reports/Modules_Report.html";
//       String tempFile = projectPath + File.separator + Reportname;
       if (isWindows) {
           Runtime.getRuntime().exec("cmd.exe /c "+projectPath+" allure serve allure-results");
       } else {
           Runtime.getRuntime().exec("sh -c "+projectPath+" allure serve allure-results");
       }
 }
}