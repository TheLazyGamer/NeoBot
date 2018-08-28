package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Random;

public class KacheekSeek
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runKacheekSeek");
        driver.get("http://www.neopets.com/games/hidenseek/19.phtml");

        Random ran = new Random();

        for (int x = 1; x < 12; x++) {
            driver.findElement(By.xpath("/html/body/map/area[" + x + "]")).click();
            Utils.sleepMode(ran.nextInt(2000) + 1000);

            if (Utils.isElementPresentXP("/html/body/center/p[1]/b", driver)) {
                String winText = driver.findElement(By.xpath("/html/body/center/p[1]/b")).getText();
                if (winText.contains("SO BORED")) {
                    break;
                }
                else {
                    driver.get("http://www.neopets.com/games/hidenseek/19.phtml");
                    x = 0;
                }
            }
            else {
                driver.findElement(By.xpath("/html/body/center/p/a")).click();
            }
            Utils.sleepMode(ran.nextInt(2000) + 1000);
        }

        Utils.logMessage("Successfully ending runKacheekSeek");
        return true;
    }
}