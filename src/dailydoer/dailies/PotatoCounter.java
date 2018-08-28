package dailydoer.dailies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PotatoCounter
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Running Potato Counter...");
        driver.get("http://www.neopets.com/medieval/potatocounter.phtml");

        if (Utils.isElementPresentXP("//input[@value='Guess!']", driver)) {
            for (int n = 0; n < 3; n++) {
                long startTime = System.currentTimeMillis();
                int taterCounter = 0;
                for (int x = 1; x < 15; x++) {
                    for (int y = 1; y < 15; y++) {
                        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/table/tbody/tr[" + x + "]/td[" + y + "]/img", driver)) {
                            taterCounter++;
                        }
                    }
                }
                driver.findElement(By.name("guess")).sendKeys(String.valueOf(taterCounter));
                long endTime = System.currentTimeMillis();
                Utils.logMessage("PotatoCounter took " + (endTime - startTime) + " milliseconds for " + taterCounter + " potatoes");

                driver.findElement(By.xpath("//input[@value='Guess!']")).click();
                Utils.sleepMode(5000);
                if (Utils.isElementPresentXP("//input[@value='Play Again']", driver)) {
                    driver.findElement(By.xpath("//input[@value='Play Again']")).click();
                }
                else {
                    Utils.logMessage("Successfully ending runPotatoCounter1");
                    return true;
                }

                if (n == 2) {
                    Utils.logMessage("Successfully ending runPotatoCounter2");
                    return true;
                }
            }
        }
        else if (Utils.isElementPresentXP("//input[@value='Back to Meridell']", driver)) {
            Utils.logMessage("Successfully ending runPotatoCounter3");
            return true;
        }

        Utils.logMessage("Failed ending runPotatoCounter");
        return false;
    }
}