package dailydoer.hourlies;

import dailydoer.Utils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class Fishing
{
    public static boolean run(WebDriver driver)
    {
        Utils.logMessage("Starting runFishing");
        driver.get("http://www.neopets.com/water/fishing.phtml");

        if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[1]/form/input[2]", driver))
        {
            driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[1]/form/input[2]")).click();
            Utils.sleepMode(2000);

            if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[2]/form/input", driver))
            {
                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[2]/form/input")).click();
                Utils.logMessage("Successfully ending 1 runFishing");
                return true;
            }

            else if (Utils.isElementPresentXP("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[3]/form/input", driver))
            {
                driver.findElement(By.xpath("//*[@id=\"content\"]/table/tbody/tr/td[2]/div[2]/center[3]/form/input")).click();
                Utils.logMessage("Successfully ending 2 runFishing");
                return true;
            }
            Utils.logMessage("Failed ending 1 runFishing");
            return false;
        }
        Utils.logMessage("Failed ending 2 runFishing");
        return false;
    }
}