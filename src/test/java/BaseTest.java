
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.util.*;
import java.util.List;

abstract public class BaseTest {

    public static WebDriver driver;
    static LinkedHashMap<String, Integer> sortedMap;
    public static Object[] Key;
    public static Object[] Value;
    public static Object[] array;
    public static Object Keys;
    public static Object Values;
    public static Object arrays;


    public static void Caps(String Driver, String Driver_Path) {
        DesiredCapabilities caps = new DesiredCapabilities();
        String path = System.getProperty("user.dir");
        System.setProperty(Driver, path +Driver_Path);
        System.setProperty("https.protocols", "TLSv1.2");
        driver = new ChromeDriver(caps);
        driver.manage().window().maximize();
        driver.get("https://alm.vodafone.com/qcbin/rest/is-authenticated?login-form-required=y");
    }

    public static void Login(String Username, String Password) throws InterruptedException, IOException {
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

    public static void URlQuery(String Domain_Name, String Project_Name, String Status, String Device_Type, String Priority, String Severity, String Detected_by, String Component_name, String Creation_Time) {
        driver.navigate().to("https://alm.vodafone.com/qcbin/rest/domains/" + Domain_Name + "/projects/" + Project_Name + "/defects?query={status[" + Status + "];user-21[" + Device_Type + "];priority[" + Priority + "];severity[" + Severity + "];detected-by[" + Detected_by + "];user-11[" + Component_name + "];creation-time[" + Creation_Time + "]}");
        list();
    }

    public static void countFrequencies(ArrayList<String> list) {

        Map<String, Integer> Counter = new HashMap<String, Integer>();

        for (String i : list) {
            Integer j = Counter.get(i);
            Counter.put(i, (j == null) ? 1 : j + 1);
        }
        BaseTest.sortedMap = new LinkedHashMap<>();
        Counter.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));

    }

    public static void list() {
        List<WebElement> user11 = driver.findElements(By.xpath("//*[@Name='user-11']/Value"));
        ArrayList<String> list = new ArrayList<String>();
        for (WebElement Name : user11) {
            String module = Name.getAttribute("innerHTML");
            list.add(module);
        }

        BaseTest.countFrequencies(list);
        driver.quit();
    }


    public static void Report() throws IOException {
        try {
            StringBuilder htmlStringBuilder = new StringBuilder();

            Key = sortedMap.keySet().toArray();
            Value = sortedMap.values().toArray();
            array = sortedMap.entrySet().toArray();
            StringBuilder paramCollection = new StringBuilder();

            for (int i = 0; i < array.length; i++) {
                Keys = Key[i];
                Values = Value[i];
                arrays = array[i];

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

            //write html string content to a file
            ExportReport(htmlStringBuilder.toString(), "Report.html");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void ExportReport(String fileContent, String fileName) throws IOException {
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

}