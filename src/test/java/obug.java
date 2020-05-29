
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.TestNG;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import java.io.*;

import java.lang.reflect.Type;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

abstract public class obug {

    public static WebDriver driver;
    static LinkedHashMap<String, Integer> sortedMap;
    public static Object[] Key;
    public static Object[] Value;
    public static Object[] array;
    public static Object Keys;
    public static Object Values;
    public static Object arrays;
    public static String scripts;
    public static List<String> scriptsNames;
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


    public static void Caps(String Driver, String Driver_Path) {
        DesiredCapabilities caps = new DesiredCapabilities();
        String path = System.getProperty("user.dir");
        System.setProperty(Driver, path + Driver_Path);
        /*WebDriverManager.chromedriver().clearPreferences();
        WebDriverManager.chromedriver().setup();*/
        System.setProperty("https.protocols", "TLSv1.2");
        driver = new ChromeDriver(caps);
        driver.manage().window().maximize();
        driver.get("https://alm.vodafone.com/qcbin/rest/is-authenticated?login-form-required=y");

    }

    public static void Login(String Username, String Password) throws Exception {
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
            ALM.RegressionPack();
        }
    }

    public static void URlQuery(String Domain_Name, String Project_Name, String Status, String Device_Type, String Priority, String Severity, String Detected_by, String Component_name, String Creation_Time) throws IOException, InterruptedException {
        driver.navigate().to("https://alm.vodafone.com/qcbin/rest/domains/" + Domain_Name + "/projects/" + Project_Name + "/defects?query={status[" + Status + "];user-21[" + Device_Type + "];priority[" + Priority + "];severity[" + Severity + "];detected-by[" + Detected_by + "];user-11[" + Component_name + "];creation-time[" + Creation_Time + "]}");
        list();
    }

    public static void countFrequencies(ArrayList<String> list) {

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

    public static void list() throws IOException, InterruptedException {
        List<WebElement> user11 = driver.findElements(By.xpath("//*[@Name='user-11']/Value"));
        ArrayList<String> list = new ArrayList<String>();
        for (WebElement Name : user11) {
            String module = Name.getAttribute("innerHTML");
            list.add(module);
        }

        obug.countFrequencies(list);
        driver.quit();
    }

    public static void Report() throws IOException, InterruptedException {
        try {
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

            ExportReport(htmlStringBuilder.toString(), "Report.html");
            openReport();
            String projectPath = System.getProperty("user.dir");
            boolean isWindows = System.getProperty("os.name")
                    .toLowerCase().startsWith("windows");
            if (isWindows) {
                try {
                    Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c del LocationsList.json");
                    Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c timeout /T 10");
                    Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c dir /b/s *.java >> LocationsList.json");
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

    public static void AutomaticMatch() throws IOException, InterruptedException {
        String JsonName = "LocationsList";
        String projectPath = System.getProperty("user.dir");
        try {
            FileReader fr = new FileReader("" + projectPath + "/LocationsList.json");
            if (fr.equals(false)) {
                Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c del LocationsList.json");
                Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c timeout /T 10");
                Runtime.getRuntime().exec("cmd.exe " + projectPath + " /c dir /b/s *.java >> LocationsList.json");
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
//                System.out.println(input + " Equal script name " + matchFound);
            }

            if (matchFound == true) {
                matchStatus.add(matchFound);
                names.add(input);
            }
//                names.add(sc);
        }

                    /*{
                if (name2.contains("EIOPassed")) {
//                    collect.add((String) s);
//                    naming = String.valueOf(true);
//                    System.out.println("Regression Pack Contains :" + collect + "");*/
    }


    public static void ExportReport(String fileContent, String fileName) throws IOException, InterruptedException {
        String projectPath = System.getProperty("user.dir");
        String tempFile = projectPath + File.separator + fileName;
        File file = new File(tempFile);
        // if file does exists, then delete and create a new file
        if (file.exists()) {
            try (FileWriter filee = new FileWriter("Report.html")) {
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

    public static void openReport() throws IOException, InterruptedException {

        String command = "";
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");
        ProcessBuilder builder = new ProcessBuilder();
        String projectPath = System.getProperty("user.dir");
        String Reportname = "Report.html";
        String tempFile = projectPath + File.separator + Reportname;
        if (isWindows) {
            Runtime.getRuntime().exec("cmd.exe /c start " + tempFile + "");
        } else {
            Runtime.getRuntime().exec("sh -c open " + tempFile + "");
        }
    }

    public void fullRegressionPack() throws Exception {
        AutomaticMatch();

//        System.out.println("Regression Pack Contains :" + names + "");

        List<XmlSuite> suites = new ArrayList<XmlSuite>();
        XmlSuite suite = new XmlSuite();
        suite.setName("Full Regression Pack");
        XmlTest test = new XmlTest();
        TestNG testNG = new TestNG();
        test.setName("Full Regression Pack");
        suites.add(suite);
        testNG.setXmlSuites(suites);

        pack = new ArrayList<>();
        collect = new ArrayList<>();
        for (Object s : names) {
            scriptsNames.forEach(name2 -> {
                if (name2.contains(s.toString())) {
//                    collect.add(s.toString());
                    naming = String.valueOf(true);
                    pack.add(name2);
                }
            });
        }

        name = new ArrayList<>();
        name.addAll(pack);
        pack.clear();
        pack.addAll(name);

//        while (matchStatus.containsAll(Collections.singleton(true)) ) {
                for (String ClassName : pack) {
                    StringBuilder s = new StringBuilder();
                    s.append(ClassName).append(".class");
                    System.out.println(s);
//                    testNG.setTestClasses(new Class[] {s});


//                    testNG.run();
                }
//            }
        System.out.println("Regression Pack Contains :" + pack + "");

    }




        public static void criticalRegressionPack () {
        }
}