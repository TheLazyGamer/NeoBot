package dailydoer.handlers;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.stream.IntStream;

public class OncePerSchedule
{
    public static void run(WebDriver driver, String xmlNode, int[] validHours) throws IOException
    {
        GregorianCalendar gc = new GregorianCalendar();
        int hour = gc.get(Calendar.HOUR_OF_DAY);
        Utils.logMessage("oncePerSchedule hour: " + hour);

        if (xmlNode.equals("Snowager")) {
            if (IntStream.of(validHours).anyMatch(x -> x == hour)) {
                driver.get("http://www.neopets.com/winter/snowager.phtml");
                if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td/p[4]/a", driver)) {
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td/p[4]/a")).click();
                    Utils.logMessage("Successfully ending Snowager");
                }
            }
        }

        else if (xmlNode.equals("Deposit")) {
            if (IntStream.of(validHours).anyMatch(x -> x == hour)) {
                Utils.logMessage("Starting Deposit");
                driver.get("http://www.neopets.com/bank.phtml");
                String npString = driver.findElement(By.xpath("//*[@id=\"npanchor\"]")).getText();
                int npToDeposit = Integer.parseInt(npString.replace(",", "").replace(" NP", ""));
                if (npToDeposit > 10000) {
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table[1]/tbody/tr/td[1]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/form/input[2]")).clear();
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/table[1]/tbody/tr/td[1]/div/table/tbody/tr[2]/td/table/tbody/tr[2]/td/div/form/input[2]")).sendKeys("" + (npToDeposit - 10000));
                    driver.findElement(By.xpath("//input[@value='Deposit']")).click();
                    Utils.handleAlert(driver);
                }

                Date dateCurrent = new Date();
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                String currentDate = format.format(dateCurrent);

                String currentNP = driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/table/tbody/tr[2]/td/table/tbody/tr[2]/td[2]")).getText();
                currentNP = currentNP.replace(",", "").replace("NP", "").trim();

                String depositHistory = "";
                String fileLine = "";
                int yesterdayNP = 0;
                try (BufferedReader br = new BufferedReader(new FileReader("dailydoer/resources/MoneyTracking.csv"))) {
                    while ((fileLine = br.readLine()) != null) {
                        if (!fileLine.contains(currentDate)) {
                            depositHistory = depositHistory + fileLine + "\n";
                            try {
                                yesterdayNP = Integer.parseInt(fileLine.split(",")[1]);
                            }
                            catch(NumberFormatException ex) {

                            }
                        }
                    }
                }

                File resultFile = new File("dailydoer/resources/MoneyTracking.csv");
                PrintWriter writer = new PrintWriter(resultFile);
                writer.close();

                @SuppressWarnings("resource")
                FileWriter resultWriter = new FileWriter(resultFile, true);
                resultWriter.append(depositHistory + currentDate + "," + currentNP + "," + (Integer.parseInt(currentNP) - yesterdayNP) + "\n");
                resultWriter.flush();

                Utils.logMessage("Successfully ending Deposit");
            }
        }

        else if (xmlNode.equals("CollectWinnings")) {
            if (IntStream.of(validHours).anyMatch(x -> x == hour)) {
                Utils.logMessage("Starting CollectWinnings");
                driver.get("http://www.neopets.com/pirates/foodclub.phtml?type=collect");

                if (Utils.isElementPresentXP("//input[@value='Collect Your Winnings!']", driver)) {
                    driver.findElement(By.xpath("//input[@value='Collect Your Winnings!']")).click();
                }

                Utils.logMessage("Successfully ending CollectWinnings");
            }
        }

        Utils.sleepMode(5000);

    }
}