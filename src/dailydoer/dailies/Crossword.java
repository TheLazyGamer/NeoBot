package dailydoer.dailies;

import dailydoer.Utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Crossword
{
    public static boolean run(WebDriver driver) throws IOException
    {
        Utils.logMessage("Playing the Crossword...");
        String resultLine = "";

        ArrayList<String> acrossList = new ArrayList<String>();
        ArrayList<String> downList = new ArrayList<String>();

        try {

            URL apiUrl = new URL("http://www.jellyneo.net/?go=faerie_crossword");
            HttpURLConnection apiCon = (HttpURLConnection) apiUrl.openConnection();
            apiCon.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
            InputStream apiInStream = apiCon.getInputStream();
            InputStreamReader apiInStreamReader = new InputStreamReader(apiInStream);
            BufferedReader apiBufRead = new BufferedReader(apiInStreamReader);

            while ((resultLine = apiBufRead.readLine()) != null) {
                if (resultLine.contains("Across")) {
                    resultLine = apiBufRead.readLine().trim().replace("<br>", "");
                    try {
                        while (Character.isDigit(resultLine.charAt(0))) {
                            acrossList.add(resultLine);
                            Utils.logMessage("Adding this across: " + resultLine);
                            resultLine = apiBufRead.readLine().trim().replace("<br>", "");
                        }
                    }
                    catch(StringIndexOutOfBoundsException ex) {
                        //This error is normal. Don't need to do anything.
                    }
                }
                else if (resultLine.contains("Down")) {
                    resultLine = apiBufRead.readLine().trim().replace("<br>", "");
                    try {
                        while (Character.isDigit(resultLine.charAt(0))) {
                            downList.add(resultLine);
                            Utils.logMessage("Adding this down: " + resultLine);
                            resultLine = apiBufRead.readLine().trim().replace("<br>", "");
                        }
                    }
                    catch(StringIndexOutOfBoundsException ex) {
                        //This error is normal. Don't need to do anything.
                    }
                }
            }

        }
        catch (Exception ex) {
            return false;
        }

        driver.get("http://www.neopets.com/games/crossword/index.phtml");

        //Click the button to start the crossword
        driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center[2]/form/input")).click();

        for (int acrossEle = 1; Utils.isElementPresentXP("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[4]/td[1]/a[" + acrossEle + "]", driver); acrossEle++) {
            String acrossQ = driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[4]/td[1]/a[" + acrossEle + "]")).getText();
            for (int x = 0; x < acrossList.size(); x++) {
                String acrossListA = acrossList.get(x);
                if (acrossListA.substring(0, acrossListA.indexOf(".")).equals(acrossQ.substring(0, acrossQ.indexOf(".")))) {
                    driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[4]/td[1]/a[" + acrossEle + "]")).click();
                    driver.findElement(By.name("x_word")).clear();
                    driver.findElement(By.name("x_word")).sendKeys(acrossListA.substring(acrossListA.indexOf(".") + 1, acrossListA.length()).trim());
                    Utils.sleepMode(100);
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/form/input[2]")).click();
                    Utils.sleepMode(3000);
                }

            }
        }

        for (int downEle = 1; Utils.isElementPresentXP("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[4]/td[2]/a[" + downEle + "]", driver); downEle++) {
            String downQ = driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[4]/td[2]/a[" + downEle + "]")).getText();
            for (int x = 0; x < downList.size(); x++) {
                String downListA = downList.get(x);
                if (downListA.substring(0, downListA.indexOf(".")).equals(downQ.substring(0, downQ.indexOf(".")))) {
                    driver.findElement(By.xpath("//*[@id='content']/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[4]/td[2]/a[" + downEle + "]")).click();
                    driver.findElement(By.name("x_word")).clear();
                    driver.findElement(By.name("x_word")).sendKeys(downListA.substring(downListA.indexOf(".") + 1, downListA.length()).trim());
                    Utils.sleepMode(100);
                    driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/table/tbody/tr[3]/td/form/input[2]")).click();
                    Utils.sleepMode(3000);
                }

            }
        }

        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center/center/img", driver)) {
            Utils.logMessage("Finished Crossword.");
            return true;
        }
        Utils.logMessage("Failed ending Crossword.");
        return false;
    }
}